import { Component, OnInit } from '@angular/core';
import {Router} from "@angular/router";
import {Observable} from 'rxjs';
import {MatDialog} from '@angular/material/dialog';
import {Sale, SaleDetail, SalePayment} from './../model/sale.model';
import { AlertService, alertOptions } from '../_alert';
import { ApiSaleService,Privileges, ApiCatalogService, CAT_CONST } from './../service/module.service';
import { ProductDialogSearchComponent } from './../product/dialog-search/product-dialog-search.component';
import { ClientAddComponent } from './../client/client-add/client-add.component';
import { ClientDialogSearchComponent } from './../client/dialog-search/client-dialog-search.component';
import { PaymentDialogComponent } from './payment-dialog-component/payment-dialog.component';
import { DiscountDialogComponent } from './discount-dialog-component/discount-dialog.component';
import { WithdrawallDialogComponent } from './withdrawall-dialog-component/withdrawall-dialog.component';
import { Client } from './../model/client.model';
import { Product } from './../model/product.model';
//import { TicketComponent } from './ticket-component/ticket.component';

@Component({
  selector: 'app-sale',
  templateUrl: './sale.component.html',
  styleUrls: ['./sale.component.css']
})
export class SaleComponent implements OnInit {

  date: Date;
  client: Client = new Client();
  sale: Sale = new Sale();
  saleDetail: SaleDetail[] = [];
  salePayment: SalePayment[] = [];
  productsSelected: Product[];
  tax: number;
  private decimals: number = 2;
  hasDiscount: boolean = false;
  totals= [];
  totalAmount: number = 0;
  lastIdSale: number;

  constructor(
  	private apiService: ApiSaleService,
    public catalogService:ApiCatalogService,
    public privileges:Privileges,
    public alertService:AlertService,
  	public dialog: MatDialog,
    private router: Router) {
    setInterval(() => { this.date =  new Date()}, 1000)
  }

  ngOnInit(): void {
    this.loadData();
  }

  private loadData(){
    this.apiService.getLastSaleId().subscribe(response => this.lastIdSale = response);
    this.catalogService.getByName(CAT_CONST.DISCOUNT_ENABLED).subscribe(response =>this.hasDiscount = response.description.trim().toUpperCase()==='SI');
    this.catalogService.getByName(CAT_CONST.TAX).subscribe( response => {
      this.tax = +response.description;
      this.tax = this.tax/100+1;
    }, error => this.tax = 0);
  }

  unSelectClient(){
    this.client = new Client();
  }

  private setSaleDetail(product: Product): SaleDetail {
    return <SaleDetail> {
      productId: product.id,
      productName: product.nameLarge,
      productShortName: product.nameShort,
      quantity: product.inventory.quantity,
      price: product.inventory.price,
      unity: product.inventory.unity,
      unityDesc: product.inventory.unityDesc,
      numPiece: product.inventory.numPiece
    };
  }

  isToPay(){
    let totalPayment = this.salePayment ? 
      this.salePayment.reduce(function(total, currentItem){return total + (currentItem.amount + currentItem.comision);}, 0) : 0;
    return this.sale && this.sale.total && this.sale 
      && this.sale.change >= 0 && totalPayment >= this.sale.total;
  }

  isToPayments(){
    return this.sale && this.sale.total;
  }

  private addProduct(product: Product){
    const saleDetail = this.setSaleDetail(product);
    this.addProductToList(saleDetail);
    if(!this.updateTotal(product.inventory.unityDesc, product.inventory.quantity))
        this.addTotal(product.inventory.unityDesc, product.inventory.quantity);
    this.totalAmount += product.inventory.quantity * product.inventory.price;
  }

  private round(n: number): number {
    return Number(n.toFixed(this.decimals));
  }

  private addProductToList(saleDetail: SaleDetail): void {
    let finded = false;
    this.saleDetail.forEach( item => {
      if(item.productId === saleDetail.productId && item.unity === saleDetail.unity && item.numPiece === saleDetail.numPiece){
        finded = true;
        const quantity = item.quantity + saleDetail.quantity;
        item.quantity = quantity;
        item.total = this.round(item.quantity * item.price);
        saleDetail.total = this.round(saleDetail.quantity * saleDetail.price);
        this.totalize(saleDetail);
      }
    } );
    if(!finded){
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
      const subtotal = total / this.tax;
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
      const subtotal = total / this.tax;
      const iva = total - subtotal;
  		this.sale.tax = this.round(this.sale.tax - iva);
  		this.sale.subTotal = this.round(this.sale.subTotal - subtotal);
  		this.sale.total = this.round(this.sale.total - total);
  	/*} else { // Sin impuesto
  		this.sale.subTotal = this.round(this.sale.subTotal - total);
  		this.sale.total = this.round(this.sale.subTotal);
  	}*/
  }

  private reTotalize(discount: number){
    this.sale.total = discount;
    const subtotal = this.sale.total / this.tax;
    const iva = this.sale.total - subtotal;
    this.sale.subTotal = this.round(subtotal);
    this.sale.tax = this.round(iva);
  }

  remove(saleDetail: SaleDetail){
    this.unTotalize(this.saleDetail.find(item => 
      item.productId === saleDetail.productId && item.unity === saleDetail.unity && item.numPiece === saleDetail.numPiece));
    this.saleDetail = this.saleDetail.filter(item => 
      !(item.productId === saleDetail.productId && item.unity === saleDetail.unity && item.numPiece === saleDetail.numPiece));
    this.productsSelected = this.productsSelected.filter(product => 
      !(product.id === saleDetail.productId && product.inventory.unity === saleDetail.unity && product.inventory.numPiece === saleDetail.numPiece));
    this.totals.forEach( t => {
      if( t.unity === saleDetail.unityDesc ) t.quantity -= saleDetail.quantity;
    });
    this.totals = this.totals.filter( t => t.quantity > 0 )
    this.totalAmount -= saleDetail.quantity * saleDetail.price;
  }

  tableValid(){
    return Array.isArray(this.saleDetail) && this.saleDetail.length;
  }

  openDialogProductSearch() {
    this.reset(false);
    const dialogRef = this.dialog.open(ProductDialogSearchComponent, { data: { mode: 'sale', productsSelected: this.productsSelected} });
    dialogRef.afterClosed().subscribe( result =>{
      if( result && result.data && result.data.length > 0 ){
        this.productsSelected = result.data;
        this.saleDetail = [];
        result.data.forEach( p => this.addProduct(p) );
      }
    });
  }

  openDialogClientSearch() {
    const dialogRef = this.dialog.open(ClientDialogSearchComponent);
    dialogRef.afterClosed().subscribe( result => this.setClient(result));
  }

  openDialogClientAdd() {
    const dialogRef = this.dialog.open(ClientAddComponent);
    dialogRef.afterClosed().subscribe( result => this.setClient(result));
  }

  openDialogPayments(){
    if( this.isToPayments() ){
      const dialogRef = this.dialog.open(PaymentDialogComponent, { data: this.sale.total });
      dialogRef.afterClosed().subscribe( result =>{
        if( result && result.data ){
          this.sale.change = result.data.change;
          this.salePayment = result.data.payments;
          this.sale.total += +(this.salePayment.reduce(function(total, currentItem){return total + currentItem.comision;}, 0)).toFixed(2);
        }
      });
    }
  }

  openDiscountDialog(){
    this.removeDiscount();
    const dialogRef = this.dialog.open(DiscountDialogComponent, {data: this.sale.total});
    dialogRef.afterClosed().subscribe(result => {
      if(result && result.data) {
        this.sale.discount = result.data.discount;
        this.reTotalize(result.data.amountDiscounted);
      } else {
        this.removeDiscount();
      }
    });
  }

  private checkWithdrall(){
    this.apiService.getExcedent(+window.localStorage.getItem("userId")).subscribe( response => {
      if(response && response > 0) {
        const dialogRef = this.dialog.open(WithdrawallDialogComponent, {data: response});
      }
    });
  }

  private removeDiscount(){
    if( this.sale.discount ) {
      this.sale.discount = null;
      this.salePayment = []; 
      this.sale.change = null;
      this.sale.total = null;
      this.sale.subTotal = null;
      this.sale.tax = null;
      this.saleDetail.forEach(sd => this.totalize(sd));
    }
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

  pay(){
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
      this.lastIdSale = this.sale.id;
        //this.openPrintTicket();
        //this.newTagTicket();
      this.alertService.success('Venta completada', alertOptions);
      this.reset(true);
      this.checkWithdrall();
    }, error => {
      this.alertService.error('La venta no ha sido registrada: ' + error.error, alertOptions);
    });
  }

/* Discard
  openPrintTicket() {
    const dialogRef = this.dialog.open(TicketComponent, {data: this.sale});
    dialogRef.afterClosed().subscribe(result =>{
      this.reset(); 
      this.alertService.success('Venta completada', alertOptions);
    });
  }

  private newTagTicket(){
    window.localStorage.setItem('currentSale', JSON.stringify(this.sale));
    this.router.navigate([]).then(result => window.open( window.location.origin + '/ticket-tag', '_blank'));
  }
*/

  private reset(complete: boolean): void{
    if(complete){
      this.client = new Client();
      this.productsSelected = null;
    }
    this.sale = new Sale();
    this.saleDetail = [];
    this.salePayment = [];
    this.totals = [];
    this.totalAmount = 0;
  }

  private addTotal(unity: string, quantity: number){
    const total = {
      'unity': unity,
      'quantity': quantity
    };
    this.totals.push(total);
  }

  private updateTotal(unity: string, quantity: number): boolean{
    let updated = false;
    this.totals.forEach( t => {
      if( t.unity === unity ){
        t.quantity += quantity;
        updated = true;
      }
    })
    return updated;
  }
}
