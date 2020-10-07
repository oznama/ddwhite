import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';
import { AlertService, alertOptions } from '../../_alert';
import { ApiSessionService, ApiCatalogService, CAT_CONST } from "../../service/module.service";
import { Session } from '../../model/user.model';

@Component({
  selector: 'app-session',
  templateUrl: './session.component.html',
  styleUrls: ['./session.component.css']
})
export class SessionComponent implements OnInit {

  sessionForm: FormGroup;
  userFullName: string;
  amountMin: number = 0;

  constructor(
  	public dialogRef: MatDialogRef<SessionComponent>, 
    private formBuilder: FormBuilder,
    private apiService: ApiSessionService,
    private catalogService: ApiCatalogService,
    public alertService:AlertService) { 
  }

  ngOnInit(): void {
  	this.userFullName = window.localStorage.getItem('userFullName');
  	this.sessionForm = this.formBuilder.group({
  		amount: ['', [Validators.required,Validators.pattern("[0-9]{0,6}(\.[0-9]{1,2})?")]],
  	})
    this.loadAmountMin();
  }

  private loadAmountMin() {
    this.catalogService.getByName(CAT_CONST.AMOUNT_MIN_BOX).subscribe(data => this.amountMin = +data.description);
  }

  save(){
    const amount = +this.sessionForm.controls.amount.value;
    console.log('Monto capturado', amount, 'Monto minimo', this.amountMin);
    if( amount >= this.amountMin) {
      const session = <Session> {
        userId: +window.localStorage.getItem("userId"),
        initialAmount: amount
      };
      this.apiService.create(session).subscribe(
        data => this.dialogRef.close({event: 'close'}), 
        error => this.alertService.error('No se ha podido abrir la caja, error: ' + error.error, alertOptions)
      );
      } else
        this.alertService.warn('El monto minimo de la caja es de ' + this.amountMin, alertOptions)
  }
}
