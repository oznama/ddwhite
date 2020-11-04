import { Component, OnInit } from '@angular/core';
import { ApiCatalogService, CAT_CONST } from "../service/module.service";
import { AlertService, alertOptions } from '../_alert';
import {Catalog, CatalogItem} from '../model/catalog.model';

@Component({
  selector: 'app-catalog',
  templateUrl: './catalog.component.html',
  styleUrls: ['./catalog.component.css']
})
export class CatalogComponent implements OnInit {

  constructor(
    private apiService: ApiCatalogService,
    public alertService:AlertService) { }

  private catalogId: number;
  catalogs: Catalog[];
  catalogItems: CatalogItem[];
  private catalogSelected: Catalog;

  ngOnInit(): void {
    this.loadCatalogs();
  }

  private loadCatalogs(): void {
  	this.apiService.get().subscribe( response => {
  		this.catalogs = response;
  		if( this.catalogs.length > 0 )
  			this.catalogId = this.catalogs[0].id;
  			this.onChange(this.catalogId);
  	} )
  }

  add(n:string, d:string){
  	if( n && d ){
  		let ciNew = new CatalogItem();
	  	ciNew.name = n;
	  	ciNew.description = d;
	  	this.catalogItems.push(ciNew);
  	}
  }

  isUpdatable(id: number, n:string, d:string): boolean{
  	let updetable = false;
  	this.catalogItems.find( catalogItem => {
  		if (catalogItem.id === id && (catalogItem.name !== n || catalogItem.description !== d)){
  			updetable = true;
  		}
  	});
  	return updetable;
  }

  update(id: number, n:string, d:string){
  	this.catalogItems.find( catalogItem => {
  		if(catalogItem.id === id){
  			catalogItem.name = n;
  			catalogItem.description = d;
  		}
  	});
  }

  remove(n:string, d:string){
  	this.catalogItems = this.catalogItems.filter( ci => ci.name !== n && ci.description !== d );
  }

  onChange(id: number) {
  	this.catalogSelected = this.catalogs.find( catalog => catalog.id === id);
  	this.catalogId = this.catalogSelected.id;
  	if(this.catalogSelected)
  		this.catalogItems = this.catalogSelected.items;
  }

  nameDissabled(name: string): boolean {
    return this.catalogSelected.name === CAT_CONST.CONST || name === CAT_CONST.PAYMENT_METHOD_CASH ||
      (this.catalogSelected.name === CAT_CONST.UNITIES && (name === CAT_CONST.UNITY_PZA || name === CAT_CONST.UNITY_BOX));
  }

  tableValid() {
    return Array.isArray(this.catalogItems) && this.catalogItems.length;
  }

  builkSave(){
    if( this.tableValid() ){
      this.apiService.updateItems(this.catalogId, this.catalogItems)
        .subscribe( data => {
          this.alertService.success('Catalogo actualizado', alertOptions);
          this.loadCatalogs();
        }, error => {
          const errMsg = 'Ha ocurrido un error en la transaccion: ';
          this.alertService.error(errMsg + error.message, alertOptions);
        }
      );
    }
    
  }

}
