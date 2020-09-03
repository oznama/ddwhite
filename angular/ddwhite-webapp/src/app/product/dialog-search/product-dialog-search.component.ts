import { Component, OnInit, Inject } from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {Observable, of} from 'rxjs';
import {map} from 'rxjs/operators';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Product, ModeProductDialog } from './../../model/product.model';
import { ApiProductService, pageSize } from './../../service/module.service';

@Component({
  selector: 'product-dialog-search',
  templateUrl: 'product-dialog-search.component.html',
})
export class ProductDialogSearchComponent implements OnInit {
  searchForm: FormGroup;
  products: Observable<Product[]>;
  productFiltred$: Observable<Product[]>;

  page: number = 0;
  sort: string = 'sku,asc';
  totalPage: number;

  constructor(
    public dialogRef: MatDialogRef<ProductDialogSearchComponent>, 
    @Inject(MAT_DIALOG_DATA) public data: ModeProductDialog,
    private apiService: ApiProductService, 
    private formBuilder: FormBuilder) {
  }

  ngOnInit() {
    this.searchForm = this.formBuilder.group({
      sku: [],
      name: [],
    });
    this.loadProducts(this.page);
  }

  loadProducts(page: number): void{
    if( this.data.mode === 'inventory' ){
      this.apiService.getInventory(page, pageSize, this.sort).subscribe( data => {
        this.totalPage = data.totalPages;
        this.products = of(data.content);
        this.productFiltred$ = of(data.content);
      });
    } else if( this.data.mode === 'sale' ){
      this.apiService.getProductsForSale(page, pageSize, this.sort).subscribe( data => {
        this.totalPage = data.totalPages;
        this.products = of(data.content);
        this.productFiltred$ = of(data.content);
      });
    }
  }

  pagination(page:number): void {
    if( page >= 0 && page < this.totalPage && page != this.page ) {
      this.page = page;
      this.loadProducts(this.page);
    }
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