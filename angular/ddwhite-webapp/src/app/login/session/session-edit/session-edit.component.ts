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
      outDate: [],
      initialAmount: [, [Validators.required,Validators.pattern("[0-9]{0,6}(\.[0-9]{1,2})?")]],
      finalAmount: [, [Validators.required,Validators.pattern("[0-9]{0,6}(\.[0-9]{1,2})?")]]
    });
    this.editForm.controls.userFullname.disable();
    this.editForm.controls.inDate.disable();
    this.editForm.controls.outDate.disable();
    let editSessionId = +window.localStorage.getItem("editSessionId");
    this.apiService.getById(editSessionId).subscribe(data => this.editForm.setValue(data));
    this.apiService.getWithdrawals(editSessionId).subscribe(data => this.withdrawals = data);
  }

  onSubmit(){
  	this.apiService.updateAmounts(this.editForm.value).subscribe( data => {
        this.alertService.success('Montos actualizados', alertOptions);
        this.regresar();
      }, error => this.alertService.error('El registro no ha sido actualizado: ' + error.error, alertOptions));
  }

  regresar(){
  	this.router.navigate(['session-list']);
  }

}
