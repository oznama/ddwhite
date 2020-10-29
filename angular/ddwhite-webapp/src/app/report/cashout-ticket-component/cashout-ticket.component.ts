import { Component, OnInit, Inject } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ApiReportService, ApiUserService } from '../../service/module.service';
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
    @Inject(MAT_DIALOG_DATA) public data: boolean,
    private formBuilder: FormBuilder,
    private reportService: ApiReportService,
    private userService: ApiUserService,
    public alertService:AlertService) { }

  ngOnInit(): void {
    this.searchForm = this.formBuilder.group({
      user:[],
      startDate: [],
      startTime: [],
      endDate: [],
      endTime: [],
      amount: [, [Validators.required,Validators.pattern("[0-9]{0,6}(\.[0-9]{1,2})?")]],
    });
    this.loadData();
  }

  private loadData(){
    this.userService.get(0, 100, 'id,asc').subscribe( data => this.users = data.content);
    this.reportService.currentWithdrawal(+window.localStorage.getItem('userId')).subscribe( data => this.withdrawals = data);
  }

  imprimir(){
    var userId = +window.localStorage.getItem('userId'), startTime = null, startDate = null, endTime = null, endDate = null;
    if( this.data ) {
      userId = +this.searchForm.controls.user.value;
      startTime = this.searchForm.controls.startTime.value;
      startDate = this.searchForm.controls.startDate.value;
      endTime = this.searchForm.controls.endTime.value;
      endDate = this.searchForm.controls.endDate.value;
    }
    if( startTime && startDate ){
      startDate.setHours(startTime.split(":")[0]);
      startDate.setMinutes(startTime.split(":")[1]);
    }
    if( endTime && endDate ){
      endDate.setHours(endTime.split(":")[0]);
      endDate.setMinutes(endTime.split(":")[1]);
    }
    this.reportService.printCashout(userId, startDate, endDate, +this.searchForm.controls.amount.value).subscribe(
      data => {
        if( !this.data )
          this.dialogRef.close({ event: 'close', data: +this.searchForm.controls.amount.value });
      }, error => this.alertService.error('Error al generar corte de caja, error: ' + error.message, alertOptions));
  }

}
