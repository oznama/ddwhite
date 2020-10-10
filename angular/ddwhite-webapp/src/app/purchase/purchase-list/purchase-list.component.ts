import { Component, OnInit } from '@angular/core';
//import {Router} from "@angular/router";
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import { Observable, of } from 'rxjs/index';
import {first, map} from "rxjs/operators";
import {PurchaseList, Purchase} from '../../model/purchase.model';
import { ApiPurchaseService, ApiCatalogService, Privileges, CAT_CONST } from '../../service/module.service';
import {CatalogItem} from './../../model/catalog.model';
import { AlertService, alertOptions } from '../../_alert';

@Component({
  selector: 'app-purchase-list',
  templateUrl: './purchase-list.component.html',
  styleUrls: ['./purchase-list.component.css']
})
export class PurchaseListComponent implements OnInit {

  searchForm: FormGroup;
  purchases: Observable<PurchaseList[]>;
  catalogUnity: CatalogItem[];
  boxId: number;
  purchasesFiltred: Observable<PurchaseList[]>;
  hasElements: boolean = false;

  constructor(private formBuilder: FormBuilder,
  	/*private router:Router,*/
  	private purchaseService: ApiPurchaseService,
    private catalogService: ApiCatalogService,
  	public privileges:Privileges,
    public alertService:AlertService) { }

  ngOnInit(): void {
  	this.searchForm = this.formBuilder.group({
      startDate: [],
      endDate: [],
      productName: []
    });
    this.loadCatalogUnity();
  }

  private loadCatalogUnity(): void{
    this.catalogService.getByName(CAT_CONST.UNITIES).subscribe( response => {
      this.catalogUnity = response.items;
      this.boxId = this.catalogUnity.find( ci => ci.name.toUpperCase() === 'CAJA' ).id;
    }, error =>{
      console.error(error);
    });
  }

  search() {
    this.purchaseService.getList(this.searchForm.controls.startDate.value, this.searchForm.controls.endDate.value).subscribe(response => {
    	this.purchases = of(response);
      this.purchasesFiltred = of(response);
      this.hasElements = true;
    }, error => this.hasElements = false);
  }

/*
  edit(purchaseId: number){
  	window.localStorage.removeItem("editPurchaseId");
    window.localStorage.setItem("editPurchaseId", purchaseId.toString());
  	this.router.navigate(['purchase-edit']);
  }
  */

  modify(id: number, quantity: number, unity: number, cost: number, numPiece: number) {
    const body = this.setPurchase(id, quantity, unity, cost, numPiece);
    this.purchaseService.update(body).pipe(first()).subscribe(
        data => {
          if(data.status === 200) {
            this.alertService.success('Compra actualizada', alertOptions);
          }else {
            this.alertService.error(data.message, alertOptions);
          }
        },
        error => {
          this.alertService.error('El registro no ha sido actualizado: ' + error.error, alertOptions);
        }
    );
  }

  private setPurchase(id: number, quantity: number, unity: number, cost: number, numPiece: number): Purchase {
    return <Purchase> {
      id: id,
      quantity: quantity,
      cost: cost,
      unity: unity,
      numPiece:  (numPiece === 0 || unity !== this.boxId) ? null : numPiece,
      userId: +window.localStorage.getItem("userId")
    };
  }

  doFilter(): void{
    var productName = this.searchForm.controls.productName.value;
    if( productName ){
      this.purchasesFiltred = this.purchases.pipe(map( 
        items => items.filter(purchase => purchase.product.toUpperCase().includes(productName.toUpperCase()))
      ));
    }
  }

  clearFilter(): void{
    this.searchForm.controls.productName.setValue(null);
    this.purchasesFiltred = this.purchases;
  }

}
