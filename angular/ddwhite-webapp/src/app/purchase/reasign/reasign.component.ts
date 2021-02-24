import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup} from "@angular/forms";
import {Purchase, PurchaseReasign} from '../../model/purchase.model';
import { AlertService, alertOptions } from '../../_alert';
import { ApiPurchaseService } from '../../service/module.service';
import {PurchaseDialogCostComponent} from '../purchase-dialog-cost/purchase-dialog-cost-component';

@Component({
  selector: 'purchase-reasign',
  templateUrl: './reasign.component.html',
  styleUrls: ['./reasign.component.css']
})
export class PurchaseReasignComponent implements OnInit {

  searchForm: FormGroup;
  purchases: Purchase[];
  purchasesFiltred$: Purchase[];
  purchasesByProduct: Purchase[];

  purchaseOrigin: number;
  purchaseSelected: Purchase;
  
  constructor(
  	private purchaseService: ApiPurchaseService,
  	public alertService:AlertService,
    private formBuilder: FormBuilder) { 
  }

  ngOnInit(): void {
    this.searchForm = this.formBuilder.group({
      productName: []
    });
  	this.loadPurchases();
  }

  private setPurchaseReasign(pD: number, q: number): PurchaseReasign {
    return <PurchaseReasign> {
      purchasesOrigin: this.purchaseOrigin,
      purchaseDestity: pD,
      quantity: q
    };
  }

  private loadPurchases(): void{
  	this.purchaseService.findForReasign().subscribe( response => {
    	this.purchases = response;
      this.purchasesFiltred$ = response;
    }, error =>{
    	console.error(error);
    });
  }

  doFilter(): void{
    var productName = this.searchForm.controls.productName.value;
    if( productName ){
      this.purchasesFiltred$ = this.purchases.filter(purchase => purchase.product.nameLarge.toLowerCase().includes(productName))
    }
  }

  clearFilter(): void{
    this.searchForm.reset();
    this.purchasesFiltred$ = this.purchases;
  }

  cancelar() {
  	this.purchaseSelected=null;
  	this.purchasesByProduct = null;
  	this.loadPurchases();
  }

  purchaseNotSelected(): boolean {
  	return !this.purchaseSelected;
  }

  showQuantityInput(): boolean {
  	return this.purchasesByProduct && this.purchasesByProduct.length && this.purchasesByProduct.length>0;
  }

  seleccionar(purchaseId:number, productId: number, purchase: Purchase): void{
  	this.purchaseService.findByProduct(purchaseId, productId).subscribe( response => {
    	this.purchasesByProduct = response;
    	if(this.showQuantityInput()){
			  this.purchaseOrigin = purchaseId;
    		this.purchaseSelected = purchase;
    	} else {
        this.alertService.warn('La compra del producto selecionado, no tiene presentación distinta a cual reasignar', alertOptions);
      }
    }, error =>{
    	console.error(error);
    });
  }

  reasign(purchaseDestiny: number, quantity: number) {
  	if( quantity && quantity > 0 && quantity <= this.purchaseSelected.quantity ) {
  		const body = this.setPurchaseReasign(purchaseDestiny, quantity);
	    this.purchaseService.reasign(body)
	      .subscribe( data => {
	        this.alertService.success('Compra reasignada correctamente', alertOptions);
	        this.cancelar();
	      }, error => {
	        this.alertService.error('La compra no ha sido reasignada: ' + error.error, alertOptions);
	      }
	    );
  	} else
  		this.alertService.warn('Cantidad a reasingar invalida', alertOptions);
  	
  }

}
