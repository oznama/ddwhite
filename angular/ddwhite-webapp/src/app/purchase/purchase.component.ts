import { Component, OnInit, Optional, Inject } from '@angular/core';
import {FormBuilder, FormGroup, FormControl, Validators} from '@angular/forms';
import {Router} from "@angular/router";
import {MatDialog} from '@angular/material/dialog';
import {CatalogItem} from './../model/catalog.model';
import {Purchase} from './../model/purchase.model';
import { AlertService, alertOptions } from '../_alert';
import { ApiCatalogService } from './../service/api.service.catalog';
import { ApiPurchaseService } from './../service/api.service.purchase';

import {Observable, of} from 'rxjs';
import {map, startWith} from 'rxjs/operators';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Provider } from './../model/provider.model';
import { Product } from './../model/product.model';
import { ApiProviderService } from './../service/api.service.provider';
import { ApiProductService } from './../service/api.service.product';


@Component({
  selector: 'app-purchase',
  templateUrl: './purchase.component.html',
  styleUrls: ['./purchase.component.css']
})
export class PurchaseComponent implements OnInit {

  purchaseForm: FormGroup;
  catalogUnity: CatalogItem[];
  purchases: Purchase[] = [];

  provider: Provider = new Provider();
  product: Product = new Product();

  constructor(
  	private formBuilder: FormBuilder,
  	private router: Router, 
  	private catalogService: ApiCatalogService,
  	private purchaseService: ApiPurchaseService,
  	public alertService:AlertService,
  	public dialog: MatDialog) { 
  }

  ngOnInit(): void {
  	this.purchaseForm = this.formBuilder.group({
  		quantity: ['', [Validators.required,Validators.pattern("[0-9]{1,5}")]],
  		unitPrice: ['', [Validators.required,Validators.pattern("[0-9]{0,6}(\.[0-9]{1,2})?")]],
  		unity: []
  	})
  	this.loadCatalogUnity();
  }

  cancelar(){
    this.router.navigate(['home']);
  }

  private setPurchase(): Purchase {
    return <Purchase> {
      product: this.product,
      providerId: this.provider.id,
      quantity: +this.purchaseForm.controls.quantity.value,
      unitPrice: +this.purchaseForm.controls.unitPrice.value,
      unity: +this.purchaseForm.controls.unity.value,
      userId: +window.localStorage.getItem("userId")
    };
  }

  onSubmit() {
  	const body = this.setPurchase();
    this.purchaseService.create(body)
      .subscribe( data => {
        this.alertService.success('Compra registrada', alertOptions);
        this.purchaseForm.reset();
      }, error => {
        this.alertService.error('La compra no ha sido guardada: ' + error.error, alertOptions);
      }
    );
  }

  private loadCatalogUnity(): void{
  	this.catalogService.getByName('UNIDADES').subscribe( response => {
    	this.catalogUnity = response.items;
    }, error =>{
    	console.log(error);
    });
  }

  formInvalid(){
    return !(this.provider.id && this.product.id && 
            this.purchaseForm.controls.quantity.value &&
            this.purchaseForm.controls.unitPrice.value &&
            this.purchaseForm.controls.unity.value);
  }

  agregar(){
    const purch = this.setPurchase();
    let finded = false;
    this.purchases.forEach( item => {
      if(item.providerId === purch.providerId && 
          item.product.id === purch.product.id && 
          item.unitPrice === purch.unitPrice){
        finded = true;
        item.quantity += purch.quantity;
      }
    } );
    if(!finded)
      this.purchases.push(purch);
    //else
      //this.alertService.error('Ya existe un producto registrado con SKU: ' + sku, alertOptions);
  }

  remove(purch: Purchase){
    this.purchases = this.purchases.filter(item => !(item.providerId === purch.providerId && 
                                                  item.product.id === purch.product.id && 
                                                  item.unitPrice === purch.unitPrice));
  }

  tableValid(){
    return Array.isArray(this.purchases) && this.purchases.length;
  }

  builkSave(){
    if( this.tableValid() ){
      const userId = +window.localStorage.getItem("userId");
      this.purchases.filter(item => item.userId = userId);
      this.purchaseService.createBulk(this.purchases)
        .subscribe( data => {
          this.alertService.success('Compras registradas', alertOptions);
          this.purchases = [];
          this.product = new Product();
          this.provider = new Provider();
          this.purchaseForm.reset();
        }, error => {
          const errMsg = 'Ha ocurrido un error en la transaccion: ';
          console.error(error);
          if(error.status === 500)
            this.alertService.error(errMsg + error.message, alertOptions);
          else
            this.alertService.error(errMsg + error.error, alertOptions);

        }
      );
    }
  }

  openDialogProviderSearch() {
    const dialogRef = this.dialog.open(DialogProviderSearch);
    dialogRef.afterClosed().subscribe( result =>{
      if( result && result.data ){
        this.provider.id = result.data.id;
        this.provider.bussinesName = result.data.bussinesName;
      }
    });
  }

  openDialogProductSearch() {
    const dialogRef = this.dialog.open(DialogoProductSearch);
    dialogRef.afterClosed().subscribe( result =>{
      if( result && result.data ){
        this.product.id = result.data.id;
        this.product.sku = result.data.sku;
        this.product.nameLarge = result.data.nameLarge;
      }
    });
  }

}


/* ####################################################################################*/
/* #############################   DIALOG PROVIDER CLASS ##############################*/
/* ####################################################################################*/


@Component({
  selector: 'dialog-provider-search',
  templateUrl: 'dialog-provider-search.html',
})
export class DialogProviderSearch implements OnInit {
  searchForm: FormGroup;
  providers: Observable<Provider[]>;
  providersFiltred$: Observable<Provider[]>;

  constructor(
    public dialogRef: MatDialogRef<DialogProviderSearch>, @Optional() @Inject(MAT_DIALOG_DATA) public dProvider: Provider,
    private router:Router, 
    private apiService: ApiProviderService, 
    private purchaseService: ApiPurchaseService,
    public alertService:AlertService,
    private formBuilder: FormBuilder) {
  }

  ngOnInit() {
    this.searchForm = this.formBuilder.group({
      bussinesName: []
    });
    this.loadProviders();
  }

  private loadProviders() {
    this.apiService.get().subscribe( data => {
        this.providers = of(data.content);
        this.providersFiltred$ = of(data.content);
      }
    )
  }

  doFilter(): void{
    var bussinesName = this.searchForm.get('bussinesName').value;
    if( bussinesName ){
      this.providersFiltred$ = this.providers.pipe(map( 
        items => items.filter( 
          provider => provider.bussinesName.toLowerCase().includes(bussinesName)
        )
      ));
    }
  }

  clearFilter(): void{
    this.searchForm.reset();
    this.providersFiltred$ = this.providers;
  }

  select(provider: Provider): void{
    this.dialogRef.close({ event: 'close', data: provider });
  }

}

/* ####################################################################################*/
/* #############################   DIALOG PRODUCT CLASS ###############################*/
/* ####################################################################################*/


@Component({
  selector: 'dialog-product-search',
  templateUrl: 'dialog-product-search.html',
})
export class DialogoProductSearch implements OnInit {
  searchForm: FormGroup;
  products: Observable<Product[]>;
  productFiltred$: Observable<Product[]>;

  constructor(
    public dialogRef: MatDialogRef<DialogoProductSearch>, @Optional() @Inject(MAT_DIALOG_DATA) public dProduct: Product,
    private router:Router, 
    private apiService: ApiProductService, 
    private purchaseService: ApiPurchaseService,
    public alertService:AlertService,
    private formBuilder: FormBuilder) {
  }

  ngOnInit() {
    this.searchForm = this.formBuilder.group({
      sku: [],
      name: [],
    });
    this.loadProducts();
  }

  private loadProducts(): void{
    this.apiService.get().subscribe( data => {
      this.products = of(data.content);
      this.productFiltred$ = of(data.content);
    });
  }

  doFilter(): void{
    var sku = this.searchForm.get('sku').value;
    var name = this.searchForm.get('name').value;
    if(sku || name){
      if( sku && name ){
        this.productFiltred$ = this.products.pipe(map( 
          items => items.filter( 
            product => (product.sku.toLowerCase().includes(sku) 
                      && product.nameLarge.toLowerCase().includes(name))
          )
        ));
      } else if( sku ){
        this.productFiltred$ = this.productFiltred$.pipe(map( 
          items => items.filter( 
            product => product.sku.toLowerCase().includes(sku)
          )
        ));
      } else if( name ){
        this.productFiltred$ = this.products.pipe(map( 
          items => items.filter( 
            product => product.nameLarge.toLowerCase().includes(name)
          )
        ));
      }
    }
  }

  clearFilter(): void{
    this.searchForm.reset();
    this.productFiltred$ = this.products;
  }

  select(product: Product): void{
    this.dialogRef.close({ event: 'close', data: product });
  }

}