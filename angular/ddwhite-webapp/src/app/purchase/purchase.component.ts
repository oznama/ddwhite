import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Router} from "@angular/router";
import {MatDialog} from '@angular/material/dialog';
import {CatalogItem} from './../model/catalog.model';
import {Purchase} from './../model/purchase.model';
import { AlertService, alertOptions } from '../_alert';
import { ApiCatalogService, ApiPurchaseService, CAT_CONST } from './../service/module.service';
import { ProductDialogSearchComponent } from './../product/dialog-search/product-dialog-search.component';
import { ProviderDialogSearchComponent } from './../provider/dialog-search/provider-dialog-search.component';
import { Provider } from './../model/provider.model';
import { Product } from './../model/product.model';
import {PurchaseDialogCostComponent} from './purchase-dialog-cost/purchase-dialog-cost-component';

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
  //product: Product = new Product();
  purchasesOk: boolean[] = [];
  private pzId: number;
  private boxId: number;

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
  		quantity: ['', [Validators.required,Validators.pattern("[0-9]{0,6}(\.[0-9]{1,3})?")]],
  		cost: ['', [Validators.required,Validators.pattern("[0-9]{0,6}(\.[0-9]{1,4})?")]],
  		unity: [],
      numPiece: []
  	})
  	this.loadCatalogUnity();
  }

  cancelar(){
    this.router.navigate(['home']);
  }

  /*
  private setPurchase(): Purchase {
    const np = +this.purchaseForm.controls.numPiece.value;
    const currentUnity = +this.purchaseForm.controls.unity.value;
    return <Purchase> {
      productId: this.product.id,
      productName: this.product.nameLarge,
      providerId: this.provider.id,
      providerName: this.provider.bussinesName,
      quantity: +this.purchaseForm.controls.quantity.value,
      cost: +this.purchaseForm.controls.cost.value,
      unity: currentUnity,
      unityDesc: this.catalogUnity.find(c => c.id === currentUnity).name,
      numPiece:  (np === 0 || currentUnity !== this.boxId) ? null : np,
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
  */

  private loadCatalogUnity(): void{
  	this.catalogService.getByName(CAT_CONST.UNITIES).subscribe( response => {
    	this.catalogUnity = response.items;
      this.pzId = this.catalogUnity.find( ci => ci.name.toUpperCase() === CAT_CONST.UNITY_PZA ).id;
      this.boxId = this.catalogUnity.find( ci => ci.name.toUpperCase() === CAT_CONST.UNITY_BOX ).id;
    }, error =>{
    	console.error(error);
    });
  }

  /*
  formInvalid(){
    return !(this.provider.id && this.product.id && 
            this.purchaseForm.controls.quantity.value &&
            this.purchaseForm.controls.cost.value &&
            this.purchaseForm.controls.unity.value);
  }

  agregar(){
    const purch = this.setPurchase();
    
    /*let isAddeable = true;
    if(this.product.cost === 0)
      this.product.cost = purch.cost;
    else if(purch.cost !== this.product.cost){
      this.openDialogCost(purch);
      isAddeable = false;
    }

    if(isAddeable){*/
      //this.addPurchaseToList(purch);
    //}
    
  //}

  private setPurchase(product: Product): Purchase {
    return <Purchase> {
      productId: product.id,
      productName: product.nameLarge,
      providerId: this.provider.id,
      providerName: this.provider.bussinesName,
      unity: this.pzId,
      unityDesc: this.catalogUnity.find(c => c.id === this.pzId).name
    };
  }

  private addPurchase(product: Product){
    const purchase = this.setPurchase(product);
    this.addPurchaseToList(purchase);
  }

  private addPurchaseToList(purch: Purchase): void {
    let finded = false;
    this.purchases.find(item => finded = item.providerId === purch.providerId && item.productId === purch.productId);
    if(!finded){
      this.purchases.push(purch);
      this.purchasesOk.push(false);
    }
  }

  remove(purch: Purchase){
    this.purchases = this.purchases.filter(item => !(
      item.providerId === purch.providerId && 
      item.productId === purch.productId && 
      item.cost === purch.cost && 
      item.unity === purch.unity
    ));
    this.purchasesOk.pop();
  }

  duplicate(purch: Purchase){
    const pDuplicated = <Purchase> {
      productId: purch.productId,
      productName: purch.productName,
      providerId: purch.providerId,
      providerName: purch.providerName,
      unity: purch.unity,
      unityDesc: purch.unityDesc
    }
    this.purchases.push(pDuplicated);
    this.purchasesOk.push(false);
  }

  tableValid(){
    return Array.isArray(this.purchases) && this.purchases.length && 
      this.purchases.length === this.purchasesOk.filter( pOk => pOk).length;
  }

  isUpdatable(i: number, q: number, c:number, u: number, np: number): boolean{
    const purch = this.purchases[i];
    let updated = purch.quantity !== q || purch.cost !== c || purch.unity !== u;
    if(u === this.boxId)
      updated = updated || purch.numPiece !== np;
    this.purchasesOk[i] = !updated;
    return updated;
  }

  update(i: number, q: number, c:number, u: number, np: number){
    const purch = this.purchases[i];
    purch.quantity = q;
    purch.cost = c;
    purch.unity = u;
    purch.unityDesc = this.catalogUnity.find(c => c.id === u).name;
    purch.numPiece = (np === 0 || u !== this.boxId) ? null : np;
    this.purchasesOk[i] = true;
  }
  
  showNumPiece(unity: number){
    return unity === this.boxId;
  }

  builkSave(){
    if( this.tableValid() ){
      const userId = +window.localStorage.getItem("userId");
      this.purchases.filter(item => item.userId = userId);
      this.purchaseService.createBulk(this.purchases)
        .subscribe( data => {
          this.alertService.success('Compras registradas', alertOptions);
          this.purchases = [];
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
    const dialogRef = this.dialog.open(ProviderDialogSearchComponent);
    dialogRef.afterClosed().subscribe( result =>{
      if( result && result.data ){
        this.provider.id = result.data.id;
        this.provider.bussinesName = result.data.bussinesName;
      }
    });
  }

  openDialogProductSearch() {
    //const dialogRef = this.dialog.open(ProductDialogSearchComponent, { data: { mode: 'inventory'} });
    const dialogRef = this.dialog.open(ProductDialogSearchComponent, { data: { mode: 'all'} });
    dialogRef.afterClosed().subscribe( result =>{
      if( result && result.data ){
        /*
        this.product.id = result.data.id;
        this.product.sku = result.data.sku;
        this.product.nameLarge = result.data.nameLarge;
        this.product.inventory.currentCost = result.data.cost;
        */
        //this.purchases = [];
        result.data.forEach( p => this.addPurchase(p));
      }
    });
  }

/*
  openDialogCost(purch: Purchase): void {
    const dialogRef = this.dialog.open(PurchaseDialogCostComponent, {
      width: '600px',
      data: {currentCost: this.product.cost, newCost: purch.cost}
    });

    dialogRef.afterClosed().subscribe(result => {
      if(result){
        this.product.cost = purch.cost;
      }
      this.addPurchaseToList(purch);
    });
  }
  */

}