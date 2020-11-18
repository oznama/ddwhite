import { Component, OnInit } from '@angular/core';
import {Router} from "@angular/router";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Session} from '../../../model/user.model';
import {Withdrawal} from "../../../model/cashout.model";
import { ApiSessionService, Privileges, CAT_CONST } from '../../../service/module.service';
import { AlertService, alertOptions } from '../../../_alert';

@Component({
  selector: 'app-session-edit',
  templateUrl: './session-edit.component.html',
  styleUrls: ['./session-edit.component.css']
})
export class SessionEditComponent implements OnInit {

  editForm: FormGroup;
  withdrawals: Withdrawal[];

  constructor(
  	private formBuilder: FormBuilder,
    private router: Router, 
    public alertService:AlertService,
    private apiService: ApiSessionService) { 
  }

  ngOnInit(): void {
  	this.editForm = this.formBuilder.group({
      id: [],
      userId: [],
      userFullname: [],
      inDate: [],
      outDate: [, [Validators.required,Validators.pattern("[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}")]],
      initialAmount: [, [Validators.required,Validators.pattern("[0-9]{0,6}(\.[0-9]{1,2})?")]],
      finalAmount: [, [Validators.required,Validators.pattern("[0-9]{0,6}(\.[0-9]{1,2})?")]]
    });
    let editSessionId = +window.localStorage.getItem("editSessionId");
    this.apiService.getById(editSessionId).subscribe(data => {
      this.editForm.setValue(data);
      if(data.outDate) {
        this.editForm.controls.outDate.disable();
      } else {
        this.setCurrentOutDate();
      }
    });
    this.apiService.getWithdrawals(editSessionId).subscribe(data => this.withdrawals = data);
    this.editForm.controls.userFullname.disable();
    this.editForm.controls.inDate.disable();
  }

  private setCurrentOutDate(){
    var date = new Date();
    const month = (date.getMonth()+1);
    const strMonth = (month < 10 ? '0' : '') + month;
    const hour = (date.getHours());
    const strHour = (hour < 10 ? '0' : '') + hour;
    const min = (date.getMinutes());
    const strMin = (min < 10 ? '0' : '') + min;
    const sec = (date.getSeconds());
    const strSec = (sec < 10 ? '0' : '') + sec;
    const strDate = date.getFullYear() + '-' + strMonth + '-' + date.getDate() + ' ' + strHour + ':' + strMin + ':' + strSec;
    this.editForm.controls.outDate.setValue(strDate);
  }

  onSubmit(){
  	this.apiService.update(this.editForm.value)
      .subscribe( data => {
        this.alertService.success('Datos de session actualizados', alertOptions);
        this.regresar();
      }, error => this.alertService.error('El registro no ha sido actualizado: ' + error.error, alertOptions));
  }

  regresar(){
  	this.router.navigate(['session-list']);
  }

}
