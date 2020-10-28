import { Component, OnInit, Inject } from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {Observable, of} from 'rxjs';
import {map} from 'rxjs/operators';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Product, Inventory, ModeProductDialog } from './../../model/product.model';
import { ApiProductService, pageSize } from './../../service/module.service';

@Component({
  selector: 'product-dialog-search',
  templateUrl: 'product-dialog-search.component.html',
  styleUrls: ['./product-dialog-search.component.css']
})
export class ProductDialogSearchComponent implements OnInit {
  searchForm: FormGroup;
  products: Observable<Product[]>;
  carrito: Product[] = [];
  totals= [];
  totalAmount: number = 0;
  
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
    if( this.data.productsSelected ){
      this.carrito = this.data.productsSelected;
      this.loadTotals();
    }
    this.searchForm = this.formBuilder.group({
      sku: [],
      name: [],
    });
    //this.loadProducts(this.page);
  }

  private loadTotals(){
    if(this.data.mode === 'sale'){
      this.carrito.forEach( pAdded => {
        if(!this.updateTotal(pAdded.inventory.unityDesc, pAdded.inventory.quantity))
          this.addTotal(pAdded.inventory.unityDesc,pAdded.inventory.quantity);
        this.totalAmount += pAdded.inventory.quantity * pAdded.inventory.price;
      })
    }
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
        this.substractFromCar();
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
      this.apiService.getInventory(this.page, pageSize, this.sort, sku, name).subscribe( data => {
        this.products = of(data.content)
        this.substractFromCar();
        if(!sku && !name) this.totalPage = data.totalPages;
      });
    } else if( this.data.mode === 'sale' ){
      this.apiService.getProductsForSale(this.page, pageSize, this.sort, sku, name).subscribe( data => {
        this.products = of(data.content);
        this.substractFromCar();
        if(!sku && !name) this.totalPage = data.totalPages;
      });
    } else if( this.data.mode == 'all' ){
      if( sku && name ){
        this.apiService.findBySkuAndName(sku, name).subscribe( data => {
          this.products = of(data);
          this.substractFromCar();
        });
      } else if( sku ){
        this.apiService.findBySku(sku).subscribe( data => {
          this.products = of(data);
          this.substractFromCar();
        });
      } else if( name ){
        this.apiService.findByName(name).subscribe( data => {
          this.products = of(data);
          this.substractFromCar();
        });
      } else {
        this.loadProducts(this.page);
      }
    }
    this.totalPage = 1;
  }

  private substractFromCar(){
    if(this.data.mode === 'sale' && this.products && this.carrito){
      this.products.forEach( items => items.forEach( product => {
        this.carrito.forEach( pAdded => {
          if( product.id === pAdded.id && product.inventory.unity === pAdded.inventory.unity && product.inventory.numPiece === pAdded.inventory.numPiece ){
            product.inventory.quantity -= pAdded.inventory.quantity;
          }
        })
      }));
    }
  }

  clearFilter(): void{
    this.searchForm.reset();
    this.loadProducts(this.page);
  }

  private getInventory(i: Inventory, quantity: number): Inventory{
    return <Inventory> {
      quantity: quantity,
      averageCost: i.averageCost,
      currentCost: i.currentCost,
      price: i.price,
      unityDesc: i.unityDesc,
      unity: i.unity,
      numPiece: i.numPiece
    }
  }

  private getProduct(p: Product, quantity: number): Product {
    return <Product> {
      id: p.id,
      nameLarge: p.nameLarge,
      nameShort: p.nameShort,
      sku: p.sku,
      description: p.description,
      percentage: p.percentage,
      group: p.group,
      groupDesc: p.groupDesc,
      dateCreated: p.dateCreated,
      userId: p.userId,
      inventory: p.inventory ? this.getInventory(p.inventory, quantity) : null
    }
  }

  add(product: Product, quantity: number){
    if( this.data.mode==='sale' && quantity <= product.inventory.quantity )
      this.addProductSale(product, quantity);
    else this.addProductPurchase(product, quantity);
  }

  private addProductSale(product: Product, quantity: number){
    let finded = false;
    this.carrito.forEach( p => {
      if(p.id === product.id && p.inventory.unity === product.inventory.unity && p.inventory.numPiece === product.inventory.numPiece){
        finded = true;
        p.inventory.quantity = p.inventory.quantity + quantity;
        this.updateTotal(p.inventory.unityDesc, quantity)
      }
    });
    if( !finded ) {
      this.carrito.push(this.getProduct(product, quantity));
      if(!this.updateTotal(product.inventory.unityDesc, quantity))
        this.addTotal(product.inventory.unityDesc, quantity);
    }
    product.inventory.quantity -= quantity;
    this.totalAmount += quantity * product.inventory.price;
  }

  private addProductPurchase(product: Product, quantity: number){
    const finded = this.carrito.find( p => p.id === product.id);
    if( !finded )
      this.carrito.push(this.getProduct(product, quantity));
  }

  remove(product: Product){
    if(this.data.mode==='sale'){
      this.carrito = this.carrito.filter( p => !(p.id === product.id && p.inventory.unity === product.inventory.unity && p.inventory.numPiece === product.inventory.numPiece));
      this.totals.forEach( t => {
        if( t.unity === product.inventory.unityDesc ) t.quantity -= product.inventory.quantity;
      });
      this.totals = this.totals.filter( t => t.quantity > 0 )
      this.totalAmount -= product.inventory.quantity * product.inventory.price;
      this.products.forEach(items => {
        items.forEach( item => {
          if(item.id === product.id && item.inventory.unity === product.inventory.unity && item.inventory.numPiece === product.inventory.numPiece)
            item.inventory.quantity += product.inventory.quantity;
        })
      });
    } else {
      this.carrito = this.carrito.filter( p => p.id !== product.id);
    }
  }

  private addTotal(unity: string, quantity: number){
    const total = {
      'unity': unity,
      'quantity': quantity
    };
    this.totals.push(total);
  }

  private updateTotal(unity: string, quantity: number): boolean{
    let updated = false;
    this.totals.forEach( t => {
      if( t.unity === unity ){
        t.quantity += quantity;
        updated = true;
      }
    })
    return updated;
  }

  /*
  selectAll(): void{
    this.carrito = [];
    this.products.pipe(map(items => this.carrito = items));
  }
  */

  showFinished(): boolean {
    return this.carrito.length>0;
  }

  finish(){
    this.dialogRef.close({ event: 'close', data: this.carrito });
  }

  cssClass(){
    return this.data.mode !== 'sale' ? 'hidden' : 'input-quantity';
  }

}