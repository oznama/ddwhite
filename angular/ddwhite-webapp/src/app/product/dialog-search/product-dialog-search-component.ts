import { Component, OnInit, Optional, Inject } from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {Observable, of} from 'rxjs';
import {map} from 'rxjs/operators';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Product } from './../../model/product.model';
import { ApiProductService } from './../../service/api.service.product';

@Component({
  selector: 'product-dialog-search',
  templateUrl: 'product-dialog-search-component.html',
})
export class ProductDialogSearchComponent implements OnInit {
  searchForm: FormGroup;
  products: Observable<Product[]>;
  productFiltred$: Observable<Product[]>;

  constructor(
    public dialogRef: MatDialogRef<ProductDialogSearchComponent>, @Optional() @Inject(MAT_DIALOG_DATA) public dProduct: Product,
    private apiService: ApiProductService, 
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