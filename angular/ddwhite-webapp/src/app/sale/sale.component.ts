import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {Observable} from 'rxjs';
import {map, startWith} from 'rxjs/operators';
import {MatDialog} from '@angular/material/dialog';
import {Sale, SaleDetail} from './../model/sale.model';
import { AlertService, alertOptions } from '../_alert';
import { ApiCatalogService } from './../service/api.service.catalog';
import { ProductDialogSearchComponent } from './../product/dialog-search/product-dialog-search-component';

import { Client } from './../model/client.model';
import { Product } from './../model/product.model';

@Component({
  selector: 'app-sale',
  templateUrl: './sale.component.html',
  styleUrls: ['./sale.component.css']
})
export class SaleComponent implements OnInit {

  saleForm: FormGroup;
  client: Client = null; //new Client();
  product: Product = new Product();
  sale: Sale = new Sale();
  saleDetail: SaleDetail[] = [];
  quantityDefault: string = '1';
  iva: number = 0.16;

  // Auto compete de cliente
  autoCompleteClient = new FormControl();
  options: string[] = ['One', 'Two', 'Three'];
  filteredOptions: Observable<string[]>;

  constructor(
  	private formBuilder: FormBuilder,
  	private catalogService: ApiCatalogService,
  	public alertService:AlertService,
  	public dialog: MatDialog) { }

  ngOnInit(): void {
  	this.filteredOptions = this.autoCompleteClient.valueChanges.pipe(
      startWith(''),
      map(value => this.autoCompleteClientFilter(value))
    );
    this.saleForm = this.formBuilder.group({
  		quantity: [this.quantityDefault, [Validators.required,Validators.pattern("[0-9]{1,5}")]]
  	})
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

  remove(productId: number){
  	this.unTotalize(this.saleDetail.find(item => item.productId === productId));
    this.saleDetail = this.saleDetail.filter(item => item.productId !== productId);
  }

  tableValid(){
    return Array.isArray(this.saleDetail) && this.saleDetail.length;
  }

  private addProductToList(saleDetail: SaleDetail): void {
  	let finded = false;
    this.saleDetail.forEach( item => {
      if(item.productId === saleDetail.productId){
        finded = true;
        item.quantity += saleDetail.quantity;
        item.total = Number((item.quantity * item.price).toFixed(2));
        saleDetail.total = Number((saleDetail.quantity * saleDetail.price).toFixed(2));
      }
    } );
    if(!finded){
      saleDetail.total = Number((saleDetail.quantity * saleDetail.price).toFixed(2));
      this.saleDetail.push(saleDetail);
    }
    this.totalize(saleDetail);
  }

  private totalize(saleDetail: SaleDetail){  	
  	if(!this.sale.total) this.sale.total = 0;
  	if(!this.sale.subTotal) this.sale.subTotal = 0;
  	if(!this.sale.iva) this.sale.iva = 0;

  	if(this.client && this.client.id) { // Con impuesto
  		this.sale.iva = Number((this.sale.iva + (saleDetail.total * this.iva)).toFixed(2));
  		this.sale.subTotal = Number((this.sale.subTotal + saleDetail.total).toFixed(2));
  		this.sale.total = Number((this.sale.subTotal + this.sale.iva).toFixed(2));
  	} else { // Sin impuesto
  		this.sale.subTotal = Number((this.sale.subTotal + saleDetail.total).toFixed(2));
  		this.sale.total = this.sale.subTotal;
  	}
  }

  private unTotalize(saleDetail: SaleDetail){
  	if(this.client && this.client.id) { // Con impuesto
  		this.sale.iva = Number((this.sale.iva - (saleDetail.total * this.iva)).toFixed(2));
  		this.sale.subTotal = Number((this.sale.subTotal - saleDetail.total).toFixed(2));
  		this.sale.total = Number((this.sale.total - (saleDetail.total * this.iva) - saleDetail.total).toFixed(2));
  	} else { // Sin impuesto
  		this.sale.subTotal = Number((this.sale.subTotal - saleDetail.total).toFixed(2));
  		this.sale.total = this.sale.subTotal;
  	}
  }

  private autoCompleteClientFilter(value: string): string[] {
    const filterValue = value.toLowerCase();

    return this.options.filter(option => option.toLowerCase().indexOf(filterValue) === 0);
  }

  openDialogProductSearch() {
    const dialogRef = this.dialog.open(ProductDialogSearchComponent);
    dialogRef.afterClosed().subscribe( result =>{
      if( result && result.data ){
        this.product.id = result.data.id;
        this.product.sku = result.data.sku;
        this.product.nameLarge = result.data.nameLarge;
        this.product.cost = result.data.cost;
        this.product.price = result.data.price;
      }
    });
  }

}
