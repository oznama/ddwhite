import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Observable} from 'rxjs';
import {MatDialog} from '@angular/material/dialog';
import {Sale, SaleDetail, SalePayment} from './../model/sale.model';
import { AlertService, alertOptions } from '../_alert';
import { ApiSaleService,Privileges } from './../service/module.service';
import { ProductDialogSearchComponent } from './../product/dialog-search/product-dialog-search.component';
import { ClientAddComponent } from './../client/client-add/client-add.component';
import { ClientDialogSearchComponent } from './../client/dialog-search/client-dialog-search.component';
import { PaymentDialogComponent } from './payment-dialog.component';
import { TicketComponent } from './ticket-component/ticket.component';

import { Client } from './../model/client.model';
import { Product } from './../model/product.model';

@Component({
  selector: 'app-sale',
  templateUrl: './sale.component.html',
  styleUrls: ['./sale.component.css']
})
export class SaleComponent implements OnInit {

  date: Date;
  saleForm: FormGroup;
  client: Client = new Client();
  product: Product = new Product();
  sale: Sale = new Sale();
  saleDetail: SaleDetail[] = [];
  salePayment: SalePayment[] = [];
  quantityDefault: string = '1';
  iva: number = 1.16;
  decimals: number = 2;

  constructor(
  	private formBuilder: FormBuilder,
  	private apiService: ApiSaleService,
    public privileges:Privileges,
    public alertService:AlertService,
  	public dialog: MatDialog) {
    setInterval(() => { this.date =  new Date()}, 1000)
  }

  ngOnInit(): void {
    this.saleForm = this.formBuilder.group({
  		quantity: [this.quantityDefault, [Validators.required,Validators.pattern("[0-9]{1,5}")]]
  	})
  }

  unSelectClient(){
    this.client = new Client();
  }

  private setSaleDetail(): SaleDetail {
    return <SaleDetail> {
      productId: this.product.id,
      productName: this.product.nameLarge,
      productShortName: this.product.nameShort,
      quantity: +this.saleForm.controls.quantity.value,
      price: this.product.price
    };
  }

  formInvalid(){
    return !(this.product.id && this.saleForm.controls.quantity.value);
  }

  isToPay(){
    return this.sale && this.sale.total && this.sale && this.sale.change >= 0;
  }

  isToPayments(){
    return this.sale && this.sale.total;
  }

  agregar(){
    const saleDetail = this.setSaleDetail();
    this.addProductToList(saleDetail);
  }

  private checkQuantity(quantity: number): boolean{
    if(quantity > this.product.inventory.quantity){
      this.alertService.error('No hay suficientes productos en existencia', alertOptions);
      return false;
    }
    return true;
  }

  private round(n: number): number {
    return Number(n.toFixed(this.decimals));
  }

  private addProductToList(saleDetail: SaleDetail): void {
  	let finded = false;
    this.saleDetail.forEach( item => {
      if(item.productId === saleDetail.productId){
        finded = true;
        const quantity = item.quantity + saleDetail.quantity;
        if(this.checkQuantity(quantity)){
          item.quantity = quantity;
          item.total = this.round(item.quantity * item.price);
          saleDetail.total = this.round(saleDetail.quantity * saleDetail.price);
          this.totalize(saleDetail);
        }
      }
    } );
    if(!finded && this.checkQuantity(saleDetail.quantity)){
      saleDetail.total = this.round(saleDetail.quantity * saleDetail.price);
      this.saleDetail.push(saleDetail);
      this.totalize(saleDetail);
    }
  }

  private totalize(saleDetail: SaleDetail){
  	if(!this.sale.total) this.sale.total = 0;
  	if(!this.sale.subTotal) this.sale.subTotal = 0;
  	if(!this.sale.tax) this.sale.tax = 0;

    const total = saleDetail.total;

  	//if(this.client.rfc) { // Con impuesto
      const subtotal = total / this.iva;
      const iva = total - subtotal;
      this.sale.total = this.round(this.sale.total + total);
      this.sale.subTotal = this.round(this.sale.subTotal + subtotal);
      this.sale.tax = this.round(this.sale.tax + iva);
  	/*} else { // Sin impuesto
  		this.sale.subTotal = this.round(this.sale.subTotal + total);
  		this.sale.total = this.sale.subTotal;
  	}*/
    this.saleForm.controls.quantity.setValue(this.quantityDefault);
  }

  private unTotalize(saleDetail: SaleDetail){
    const total = saleDetail.total;
  	//if(this.client.rfc) { // Con impuesto
      const subtotal = total / this.iva;
      const iva = total - subtotal;
  		this.sale.tax = this.round(this.sale.tax - iva);
  		this.sale.subTotal = this.round(this.sale.subTotal - subtotal);
  		this.sale.total = this.round(this.sale.total - total);
  	/*} else { // Sin impuesto
  		this.sale.subTotal = this.round(this.sale.subTotal - total);
  		this.sale.total = this.round(this.sale.subTotal);
  	}*/
  }

  remove(productId: number){
    this.unTotalize(this.saleDetail.find(item => item.productId === productId));
    this.saleDetail = this.saleDetail.filter(item => item.productId !== productId);
  }

  tableValid(){
    return Array.isArray(this.saleDetail) && this.saleDetail.length;
  }

  openDialogProductSearch() {
    const dialogRef = this.dialog.open(ProductDialogSearchComponent, { data: { mode: 'sale'} });
    dialogRef.afterClosed().subscribe( result =>{
      if( result && result.data ){
        this.product.id = result.data.id;
        this.product.sku = result.data.sku;
        this.product.nameLarge = result.data.nameLarge;
        this.product.nameShort = result.data.nameShort;
        this.product.cost = result.data.cost;
        this.product.price = result.data.inventory.price;
        this.product.inventory.quantity = result.data.inventory.quantity;
      }
    });
  }

  openDialogClientSearch() {
    const dialogRef = this.dialog.open(ClientDialogSearchComponent);
    dialogRef.afterClosed().subscribe( result =>{this.setClient(result);});
  }

  openDialogClientAdd() {
    const dialogRef = this.dialog.open(ClientAddComponent);
    dialogRef.afterClosed().subscribe( result =>{this.setClient(result);});
  }

  openDialogPayments(){
    if( this.isToPayments() ){
      const dialogRef = this.dialog.open(PaymentDialogComponent, { data: this.sale.total });
      dialogRef.afterClosed().subscribe( result =>{
        if( result && result.data ){
          this.sale.change = result.data.change;
          this.salePayment = result.data.payments;
        }
      });
    }
  }

  openPrintTicket() {
    const dialogRef = this.dialog.open(TicketComponent, {data: this.sale});
    dialogRef.afterClosed().subscribe(result =>{
      this.reset(); 
      this.alertService.success('Venta completada', alertOptions);
    });
  }

  private setClient(result: any): void {
    if( result && result.data ){
      this.client.id = result.data.id;
      this.client.name = result.data.name;
      this.client.midleName = result.data.midleName;
      this.client.lastName = result.data.lastName;
      this.client.rfc = result.data.rfc;
    }
  }

  pagar(){
    this.sale.userId = +window.localStorage.getItem("userId");
    if(this.client.id){
      this.sale.clientId = this.client.id;
      this.sale.clientName = this.client.name+ ' ' + this.client.lastName + ' ' + this.client.midleName;
      this.sale.clientRfc = this.client.rfc;
    }
    this.sale.detail = this.saleDetail;
    this.sale.payments = this.salePayment;
    this.apiService.create(this.sale).subscribe( data => {
      this.sale.id = data;
        this.openPrintTicket();
      }, error => {
        this.alertService.error('La venta no ha sido registrada: ' + error.error, alertOptions);
      }
    );
  }

  private reset(): void{
    this.client = new Client();
    this.product = new Product();
    this.saleForm.reset();
    this.sale = new Sale();
    this.saleDetail = [];
    this.salePayment = [];
    this.saleForm.controls.quantity.setValue(this.quantityDefault);
  }
}
