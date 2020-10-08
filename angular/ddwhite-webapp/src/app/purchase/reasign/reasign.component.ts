import { Component, OnInit } from '@angular/core';
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

  purchases: Purchase[];
  purchasesByProduct: Purchase[];

  purchaseOrigin: number;
  purchaseSelected: Purchase;
  
  constructor(
  	private purchaseService: ApiPurchaseService,
  	public alertService:AlertService) { 
  }

  ngOnInit(): void {
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
    	this.purchases = response
    }, error =>{
    	console.error(error);
    });
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
	        this.alertService.success('Compra registrada', alertOptions);
	        this.cancelar();
	      }, error => {
	        this.alertService.error('La compra no ha sido guardada: ' + error.error, alertOptions);
	      }
	    );
  	} else
  		this.alertService.warn('Cantidad a reasingar invalida', alertOptions);
  	
  }

}
