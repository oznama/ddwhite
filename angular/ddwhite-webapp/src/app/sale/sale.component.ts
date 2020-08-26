import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Observable} from 'rxjs';
import {MatDialog} from '@angular/material/dialog';
import {Sale, SaleDetail} from './../model/sale.model';
import { AlertService, alertOptions } from '../_alert';
//import { ApiCatalogService } from './../service/api.service.catalog';
import { ApiSaleService } from './../service/api.service.sale';
import { ProductDialogSearchComponent } from './../product/dialog-search/product-dialog-search-component';
import { ClientAddComponent } from './../client/client-add/client-add.component';
import { ClientDialogSearchComponent } from './../client/dialog-search/client-dialog-search-component';

import { Client } from './../model/client.model';
import { Product } from './../model/product.model';

@Component({
  selector: 'app-sale',
  templateUrl: './sale.component.html',
  styleUrls: ['./sale.component.css']
})
export class SaleComponent implements OnInit {

  saleForm: FormGroup;
  client: Client = new Client();
  product: Product = new Product();
  sale: Sale = new Sale();
  saleDetail: SaleDetail[] = [];
  quantityDefault: string = '1';
  iva: number = 1.16;
  decimals: number = 2;

  constructor(
  	private formBuilder: FormBuilder,
  	private apiService: ApiSaleService,
    public alertService:AlertService,
  	public dialog: MatDialog) { }

  ngOnInit(): void {
    this.saleForm = this.formBuilder.group({
  		quantity: [, [Validators.required,Validators.pattern("[0-9]{1,5}")]]
  	})
  }

  unSelectClient(){
    this.client = new Client();
  }

  private setSaleDetail(): SaleDetail {
    return <SaleDetail> {
      productId: this.product.id,
      productName: this.product.nameLarge,
      quantity: +this.saleForm.controls.quantity.value,
      price: this.product.price
    };
  }

  formInvalid(){
    return !(this.product.id && this.saleForm.controls.quantity.value);
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

  remove(productId: number){
  	this.unTotalize(this.saleDetail.find(item => item.productId === productId));
    this.saleDetail = this.saleDetail.filter(item => item.productId !== productId);
  }

  tableValid(){
    return Array.isArray(this.saleDetail) && this.saleDetail.length;
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

  openDialogProductSearch() {
    const dialogRef = this.dialog.open(ProductDialogSearchComponent, { data: { mode: 'sale'} });
    dialogRef.afterClosed().subscribe( result =>{
      if( result && result.data ){
        this.product.id = result.data.id;
        this.product.sku = result.data.sku;
        this.product.nameLarge = result.data.nameLarge;
        this.product.cost = result.data.cost;
        this.product.price = result.data.inventory.price;
        this.product.inventory.quantity = result.data.inventory.quantity;
        this.saleForm.controls.quantity.setValue(this.quantityDefault);
      }
    });
  }

  openDialogClientSearch() {
    const dialogRef = this.dialog.open(ClientDialogSearchComponent);
    dialogRef.afterClosed().subscribe( result =>{
      if( result && result.data ){
        this.setClient(result.data);
      }
    });
  }

  openDialogClientAdd() {
    const dialogRef = this.dialog.open(ClientAddComponent);
    dialogRef.afterClosed().subscribe( result =>{
      if( result && result.data ){
        this.setClient(result.data);
      }
    });
  }

  private setClient(client: Client): void {
    this.client.id = client.id;
    this.client.name = client.name;
    this.client.midleName = client.midleName;
    this.client.lastName = client.lastName;
    this.client.rfc = client.rfc;
  }


  pagar(){
    this.sale.userId = +window.localStorage.getItem("userId");
    if(this.client.id)
      this.sale.clientId = this.client.id;
    this.sale.detail = this.saleDetail;
    this.apiService.create(this.sale)
      .subscribe( data => {
        this.alertService.success('Venta completada', alertOptions);
        // imprimir ticket
        this.reset();
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
  }
}
