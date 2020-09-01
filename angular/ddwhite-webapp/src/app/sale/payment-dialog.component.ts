import { Component, OnInit, Inject } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { AlertService, alertOptions } from '../_alert';
import {CatalogItem} from './../model/catalog.model';
import { Sale, SalePayment } from './../model/sale.model';
import { ApiCatalogService } from './../service/api.service.catalog';

@Component({
  selector: 'payment-dialog',
  templateUrl: 'payment-dialog.component.html',
  styleUrls: ['./sale.component.css']
})
export class PaymentDialogComponent implements OnInit {
  paymentForm: FormGroup;
  catalogPayment: CatalogItem[];
  payments: SalePayment[] = [];
  totalAmount: number = 0;
  decimals: number = 2;

  constructor(
    public dialogRef: MatDialogRef<PaymentDialogComponent>, 
    @Inject(MAT_DIALOG_DATA) public data: number,
    public alertService:AlertService,
    private catalogService: ApiCatalogService, 
    private formBuilder: FormBuilder) {
  }

  ngOnInit() {
    this.paymentForm = this.formBuilder.group({
      payment: [],
      amount: ['', [Validators.required,Validators.pattern("[0-9]{0,6}(\.[0-9]{1,2})?")]],
    });
    this.loadCatalogPayment();
    this.paymentForm.controls.amount.setValue(Math.ceil(this.data));
  }

  private loadCatalogPayment(): void{
    this.catalogService.getByName('METODPAG').subscribe( response => {
      this.catalogPayment = response.items;
      this.paymentForm.controls.payment.setValue(this.catalogPayment[0].id);
    }, error =>{
      console.error(error);
    });
  }

  private round(n: number): number {
    return Number(n.toFixed(this.decimals));
  }

  finalize(): boolean{
    return this.totalAmount >= this.data;
  }

  isFormValid(){
    return this.paymentForm.valid && +this.paymentForm.controls.amount.value>0;
  }

  add(): void {
    const paymentId: number = +this.paymentForm.controls.payment.value;
    const $amount: number = +this.paymentForm.controls.amount.value;
    const catPayment = this.catalogPayment.find(item => item.id === paymentId);
    const payment = <SalePayment> {
      payment: catPayment.id,
      paymentDesc: catPayment.name,
      amount: +$amount
    };
    this.remove(this.payments.find(item => item.payment === payment.payment));
    this.payments.push(payment);
    this.totalAmount = this.round(this.totalAmount+payment.amount);
    this.paymentForm.reset();
    this.paymentForm.controls.payment.setValue(this.catalogPayment[0].id);
  }

  remove(payment: SalePayment){
    if(payment && this.payments.length > 0){
      this.totalAmount = this.round(this.totalAmount-payment.amount);
      this.payments = this.payments.filter(item => !(item.payment === payment.payment));
    }
  }

  save(): void{
    this.dialogRef.close({ event: 'close', data: { payments: this.payments, change: this.round(this.totalAmount-this.data) } });
  }

}