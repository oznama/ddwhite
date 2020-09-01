import { Component, OnInit } from '@angular/core';
import {Router} from "@angular/router";
import {FormBuilder, FormGroup} from "@angular/forms";
import {Product} from "../../model/product.model";
import { ApiProductService } from "../../service/module.service";
import { AlertService, alertOptions } from '../../_alert';
import { Observable, of, combineLatest } from 'rxjs/index';
import { map, withLatestFrom, startWith, tap } from 'rxjs/operators';

@Component({
  selector: 'app-product-list',
  templateUrl: './product-list.component.html',
  styleUrls: ['./product-list.component.css']
})
export class ProductListComponent implements OnInit {

  searchForm: FormGroup;
  products: Observable<Product[]>;
  productsFiltered$: Observable<Product[]>;

  constructor(
    private router:Router,
    private apiService:ApiProductService,
    public alertService:AlertService,
    private formBuilder: FormBuilder) {
  }

  ngOnInit(): void {
    this.searchForm = this.formBuilder.group({
      sku: [],
      name: [],
    });
    this.loadProducts();
  }

  private loadProducts(): void {
    this.apiService.getInventory().subscribe( data => {
      this.products = of(data);
      this.productsFiltered$ = of(data);
    });
  }

  delete(product:Product): void{
  	this.apiService.delete(product.id)
  	  .subscribe( response => {
  	  	this.products = this.products.pipe(map( items => items.filter( p => p != product)));
        this.loadProducts();
        this.alertService.success('Producto eliminado', alertOptions);
  	  }, error => {
        console.error(error);
        this.alertService.error(error.error, alertOptions);
      }
    );
  }

  edit(product:Product): void{
    window.localStorage.removeItem("editProductId");
    window.localStorage.setItem("editProductId", product.id.toString());
  	this.router.navigate(['product-edit']);
  }

  add(): void{
  	this.router.navigate(['product-add']);	
  }

  doFilter(): void{
    var sku = this.searchForm.get('sku').value;
    var name = this.searchForm.get('name').value;
    if(sku || name){
      if( sku && name ){
        this.productsFiltered$ = this.products.pipe(map( 
          items => items.filter( 
            product => (product.sku.toLowerCase().includes(sku) 
                      && product.nameLarge.toLowerCase().includes(name))
          )
        ));
      } else if( sku ){
        this.productsFiltered$ = this.products.pipe(map( 
          items => items.filter( 
            product => product.sku.toLowerCase().includes(sku)
          )
        ));
      } else if( name ){
        this.productsFiltered$ = this.products.pipe(map( 
          items => items.filter( 
            product => product.nameLarge.toLowerCase().includes(name)
          )
        ));
      }
    }
  }

  clearFilter(): void{
    this.searchForm.reset();
    this.productsFiltered$ = this.products;
  }

}
