import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, FormControl, Validators} from '@angular/forms';
import {Router} from "@angular/router";
import {MatDialog} from '@angular/material/dialog';
import {CatalogItem} from './../model/catalog.model';
import {Purchase} from './../model/purchase.model';
import { AlertService, alertOptions } from '../_alert';
import { ApiCatalogService } from './../service/api.service.catalog';
import { ApiPurchaseService } from './../service/api.service.purchase';
/*
import {Observable} from 'rxjs';
import {map, startWith} from 'rxjs/operators';
import { Provider } from './../model/provider.model';
import { Product } from './../model/product.model';
import { ApiProviderService } from './../service/api.service.provider';
import { ApiProductService } from './../service/api.service.product';
*/

@Component({
  selector: 'app-purchase',
  templateUrl: './purchase.component.html',
  styleUrls: ['./purchase.component.css']
})
export class PurchaseComponent implements OnInit {

  purchaseForm: FormGroup;
  provider = new FormControl();
  product = new FormControl();
  catalogUnity: CatalogItem[];
  /*
  providers: Provider[];
  products: Product[];
  providerFiltred$: Observable<Provider[]>;
  productFiltred$: Observable<Product[]>;
  */

  constructor(
  	private formBuilder: FormBuilder,
  	private router: Router, 
  	private catalogService: ApiCatalogService,
  	/*private providerService: ApiProviderService, 
  	private productService: ApiProductService*/
  	private purchaseService: ApiPurchaseService,
  	public alertService:AlertService,
  	public dialog: MatDialog) { 
  }

  ngOnInit(): void {

  	this.purchaseForm = this.formBuilder.group({
  		providerName: [],
  		productName: [],
  		quantity: ['', [Validators.required,Validators.pattern("[0-9]{1,5}")]],
  		unitPrice: ['', [Validators.required,Validators.pattern("[0-9]{0,6}(\.[0-9]{1,2})?")]],
  		unity: []
  	})
  	//this.purchaseForm.controls.providerName.disable();
  	//this.purchaseForm.controls.productName.disable();

  	this.loadCatalogUnity();
  	/*
  	this.loadProviders();
  	this.loadProducts();
    this.providerFiltred$ = this.provider.valueChanges.pipe(
    	startWith(''),
    	map( value => this.filterProviders(value))
    );
    this.productFiltred$ = this.product.valueChanges.pipe(
    	startWith(''),
    	map( value => this.filterProducts(value))
    );
    */
  }

  cancelar(){
    this.router.navigate(['home']);
  }

  onSubmit() {
  	const body = <Purchase> {
  		product:{
  			id: +this.purchaseForm.controls.productName.value, // TODO: Temporal
  		},
  		providerId: +this.purchaseForm.controls.providerName.value, // TOFO: Temporal
  		quantity: +this.purchaseForm.controls.quantity.value,
  		unitPrice: +this.purchaseForm.controls.unitPrice.value,
  		unity: +this.purchaseForm.controls.unity.value,
  		userId: +window.localStorage.getItem("userId")
  	}
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
  	this.catalogService.getById(1).subscribe( response => {
    	this.catalogUnity = response.items;
    }, error =>{
    	console.log(error);
    });
  }

  /*
  private loadProviders(): void{
  	this.providerService.get().subscribe( response => {
    	this.providers = response.content;
    });
  }

  private loadProducts(): void{
  	this.productService.get().subscribe( response => {
  		this.products = response.content;
  	});
  }

  private filterProviders(bussinesName: string): Provider[] {
    return this.providers.filter(provider => provider.bussinesName.toLowerCase().includes(bussinesName.toLowerCase()));
  }

  private filterProducts(nameLarge: string): Product[] {
  	return this.products.filter(product => product.nameLarge.toLowerCase().includes(nameLarge.toLowerCase()));
  }
  */

  openDialogProviderSearch() {
    this.dialog.open(DialogProviderSearch);
  }

}


@Component({
  selector: 'dialog-provider-search',
  templateUrl: 'dialog-provider-search.html',
})
export class DialogProviderSearch {}