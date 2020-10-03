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
      this.apiService.getInventory(page, pageSize, this.sort, null, null).subscribe( data => {
        this.totalPage = data.totalPages;
        this.products = of(data.content);
      });
    } else if( this.data.mode === 'sale' ){
      this.apiService.getProductsForSale(page, pageSize, this.sort, null, null).subscribe( data => {
        this.totalPage = data.totalPages;
        this.products = of(data.content);
      });
    } else if( this.data.mode == 'all' ){
      this.apiService.get(page, pageSize, this.sort).subscribe( data => {
        this.totalPage = data.totalPages;
        this.products = of(data.content);
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
    var sku = this.searchForm.controls.sku.value;
    var name = this.searchForm.controls.name.value;
    if(sku === '') sku = null;
    if(name === '') name = null;
    if( this.data.mode === 'inventory' ){
        this.apiService.getInventory(this.page, pageSize, this.sort, sku, name).subscribe( data => this.products = of(data.content));
      } else if( this.data.mode === 'sale' ){
        this.apiService.getProductsForSale(this.page, pageSize, this.sort, sku, name).subscribe( data => this.products = of(data.content));
      } else if( this.data.mode == 'all' ){
        if( sku && name ){
          this.apiService.findBySkuAndName(sku, name).subscribe( data => this.products = of(data));
        } else if( sku ){
          this.apiService.findBySku(sku).subscribe( data => this.products = of(data));
        } else if( name ){
          this.apiService.findByName(name).subscribe( data => this.products = of(data));
        }
    }
    this.totalPage = 1;
  }

  clearFilter(): void{
    this.searchForm.reset();
    this.loadProducts(this.page);
  }

  select(product: Product): void{
    this.dialogRef.close({ event: 'close', data: product });
  }

}