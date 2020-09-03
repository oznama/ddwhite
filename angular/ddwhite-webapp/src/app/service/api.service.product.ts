import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {Observable} from "rxjs/index";
import { baseUrl, observeResponse } from './../../environments/environment';
import {ApiResponse} from "../model/api.response";
import {Product, ProductPageable, Inventory} from "../model/product.model";

@Injectable()
export class ApiProductService {

  constructor(private http: HttpClient) { }
  context: string = baseUrl + '/product';

  /*
  get() : Observable<ProductPageable> {
    return this.http.get<ProductPageable>(this.context + '/find');
  }
  */

  getById(id: number): Observable<Product> {
    return this.http.get<Product>(this.context + '/find/' + id);
  }

  create(provider: Product): Observable<any> {
    return this.http.post<any>(this.context + '/save', provider);
  }

  createBulk(providers: Product[]): Observable<any> {
    return this.http.post<any>(this.context + '/saveBulk', providers);
  }

  update(provider: Product): Observable<any> {
    return this.http.put<any>(this.context + '/update', provider, observeResponse);
  }

  delete(id: number) {
    return this.http.delete<any>(this.context + '/delete/' + id);
  }

  getInventory(page: number, size: number, sort: string) : Observable<ProductPageable> {
    return this.http.get<ProductPageable>(baseUrl + '/inventory/find/product?page='+page+'&size='+size+'&sort='+sort);
  }

  /*
  getInventoryByProduct(productId: number) : Observable<Inventory> {
    return this.http.get<Inventory>(baseUrl + '/inventory/find/' + productId);
  }
  */

  getProductsForSale(page: number, size: number, sort: string) : Observable<ProductPageable> {
    return this.http.get<ProductPageable>(baseUrl + '/inventory/find/sale?page='+page+'&size='+size+'&sort='+sort);
  }

}