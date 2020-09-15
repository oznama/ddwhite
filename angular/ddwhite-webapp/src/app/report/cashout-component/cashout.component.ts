import { Component, OnInit, Inject } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Cashout } from '../../model/cashout.model';
import { ApiReportService, ApiLoginService } from '../../service/module.service';

@Component({
  selector: 'cashout-ticket',
  templateUrl: './cashout.component.html',
  styleUrls: ['./cashout.component.css']
})
export class CashoutComponent implements OnInit {

  date: Date = new Date();
  cashout: Cashout = new Cashout();
  userName: string;
  cashoutResult: string;

  addForm: FormGroup;

  constructor(public dialogRef: MatDialogRef<CashoutComponent>,
    private formBuilder: FormBuilder,
    private apiService: ApiReportService,
    private loginService: ApiLoginService ) { }

  ngOnInit(): void {
    this.addForm = this.formBuilder.group({
      total: [, Validators.required]
    });
    this.loginService.fullName.subscribe( value => this.userName = value.toUpperCase());
    this.loadCashout();
  }

  private loadCashout(){
    this.apiService.getCashout(new Date(window.localStorage.getItem('sessionStart')), this.date).subscribe( data => {
        this.cashout = data;
        this.cashoutResult = 'Monto faltante $' + this.cashout.total;
      }
    )
  }

  onChange(event): void {
    let diff = event.target.value;
    diff = Number((diff - this.cashout.total).toFixed(2));
    this.cashoutResult = (diff === 0 ? 'Monto exacto' : ( diff < 0 ? 'Monto faltante $' : 'Monto excedente $' ) + diff);
  }

  imprimir(){
    window.print();
  }

}
