import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ApiReportService, ApiUserService, Privileges } from '../../service/module.service';
import {User} from "../../model/user.model";
import {Withdrawal} from "../../model/cashout.model";
import { AlertService, alertOptions } from '../../_alert';

@Component({
  selector: 'cashout-ticket',
  templateUrl: './cashout-ticket.component.html'
})
export class CashoutTicketComponent implements OnInit {

  searchForm: FormGroup;
  users: User[];
  withdrawals: Withdrawal[];

  constructor(public dialogRef: MatDialogRef<CashoutTicketComponent>,
    private formBuilder: FormBuilder,
    private reportService: ApiReportService,
    private userService: ApiUserService,
    public alertService:AlertService,
    public privileges: Privileges) { }

  ngOnInit(): void {
    this.searchForm = this.formBuilder.group({
      user:[],
      startDate: [],
      startTime: [],
      endDate: [],
      endTime: [],
      amount: ['', [Validators.required,Validators.pattern("[0-9]{0,6}(\.[0-9]{1,2})?")]],
    });
    this.loadData();
  }

  private loadData(){
    this.userService.get(0, 100, 'id,asc').subscribe( data => this.users = data.content);
    this.reportService.currentWithdrawal(+window.localStorage.getItem('userId')).subscribe( data => this.withdrawals = data);
  }

  imprimir(){
    const userId = this.privileges.isAdmin() ? +this.searchForm.controls.user.value : +window.localStorage.getItem('userId');
    const startTime = this.privileges.isAdmin() ? this.searchForm.controls.startTime.value : null;
    var startDate = this.privileges.isAdmin() ? this.searchForm.controls.startDate.value : null;
    if( startTime && startDate ){
      startDate.setHours(startTime.split(":")[0]);
      startDate.setMinutes(startTime.split(":")[1]);
    }
    const endTime = this.privileges.isAdmin() ? this.searchForm.controls.endTime.value : null;
    var endDate = this.privileges.isAdmin() ? this.searchForm.controls.endDate.value : null;
    if( endTime && endDate ){
      endDate.setHours(endTime.split(":")[0]);
      endDate.setMinutes(endTime.split(":")[1]);
    }
    this.reportService.printCashout(userId, startDate, endDate, +this.searchForm.controls.amount.value).subscribe(
      data => {
        if( !this.privileges.isAdmin() )
          this.dialogRef.close({ event: 'close' });
      }, error => this.alertService.error('Error al generar corte de caja, error: ' + error.message, alertOptions));
  }

}
