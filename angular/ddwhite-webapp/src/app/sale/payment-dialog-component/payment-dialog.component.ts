import { Component, OnInit, Inject } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { AlertService, alertOptions } from '../../_alert';
import {CatalogItem} from '../../model/catalog.model';
import { Sale, SalePayment } from '../../model/sale.model';
import { ApiCatalogService, CAT_CONST } from '../../service/api.service.catalog';

@Component({
  selector: 'payment-dialog',
  templateUrl: 'payment-dialog.component.html',
  styleUrls: ['../sale.component.css']
})
export class PaymentDialogComponent implements OnInit {
  paymentForm: FormGroup;
  catalogPayment: CatalogItem[];
  catalogComisiones: CatalogItem[];
  payments: SalePayment[] = [];
  totalAmount: number = 0;
  decimals: number = 2;
  efectivoId: number;
  tax: number;

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
      voucherFolio:[],
      comision: []
    });
    this.loadCatalogs();
    this.paymentForm.controls.amount.setValue(Math.ceil(this.data));
  }

  private loadCatalogs(): void{
    this.catalogService.getByName(CAT_CONST.PAYMENT_METHOD).subscribe( response => {
      this.catalogPayment = response.items;
      this.efectivoId = this.catalogPayment[0].id;
      this.paymentForm.controls.payment.setValue(this.efectivoId);
    }, error => console.error(error));
    this.catalogService.getByName(CAT_CONST.PIN_PAD).subscribe( response => this.catalogComisiones = response.items, error =>console.error(error));
    this.catalogService.getByName(CAT_CONST.TAX).subscribe( response => this.tax = +response.description, error =>console.error(error));
  }

  showCreditDataExtra(){
    return +this.paymentForm.controls.payment.value !== this.efectivoId;
  }

  showComision(){
    return this.catalogPayment.find( p => p.id === +this.paymentForm.controls.payment.value ).description === CAT_CONST.PAYMENT_METHOD_COM;
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

  private getValueOfPercentage(val: number): number {
    return val/100;
  }

  add(): void {
    const paymentId: number = +this.paymentForm.controls.payment.value;
    var $amount: number = +this.paymentForm.controls.amount.value;
    const voucherFolio: string = this.paymentForm.controls.voucherFolio.value;
    const comisionId: number = +this.paymentForm.controls.comision.value;
    var $comision = 0;
    if( this.showComision() ){
      const comision = this.catalogComisiones.find(item => item.id === comisionId).description;
      const comisions = comision.split('%+');
      comisions.forEach(c => $comision = $comision + ((c === 'IVA') ? 
        ( $comision * this.getValueOfPercentage(this.tax)) : 
        ( $amount * this.getValueOfPercentage(+c)))
      )
    }
    const catPayment = this.catalogPayment.find(item => item.id === paymentId);
    const payment = <SalePayment> {
      payment: catPayment.id,
      paymentDesc: catPayment.name,
      amount: +$amount,
      voucherFolio: voucherFolio,
      comision: ($comision===0?null:this.round($comision))
    };
    if( this.payments.filter( item => item.payment === payment.payment).length > 0 ){
      this.payments.find( item => {
        if(item => item.payment === payment.payment){
          item.amount = item.amount += $amount;
        }
      } )
    } else {
      //this.remove(this.payments.find(item => item.payment === payment.payment));
      this.payments.push(payment);
    }

    this.totalAmount = this.round(this.totalAmount+payment.amount);
    //this.paymentForm.reset();
    this.paymentForm.controls.payment.setValue(this.efectivoId);
    this.paymentForm.controls.amount.setValue(this.data - this.totalAmount);
    this.paymentForm.controls.voucherFolio.setValue(null);
    this.paymentForm.controls.comision.setValue(null);
  }

  remove(payment: SalePayment){
    if(payment && this.payments.length > 0){
      this.totalAmount = this.round(this.totalAmount-payment.amount);
      this.paymentForm.controls.amount.setValue(this.paymentForm.controls.amount.value+payment.amount);
      this.payments = this.payments.filter(item => !(item.payment === payment.payment));
    }
  }

  save(): void{
    this.dialogRef.close({ event: 'close', data: { payments: this.payments, change: this.round(this.totalAmount-this.data) } });
  }

}