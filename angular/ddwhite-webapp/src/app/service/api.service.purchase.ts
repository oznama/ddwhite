import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {Observable} from "rxjs/index";
import { baseUrl, observeResponse } from './../../environments/environment';
import {Purchase, PurchaseReasign} from "../model/purchase.model";

@Injectable()
export class ApiPurchaseService {

  constructor(private http: HttpClient) { }
  context: string = baseUrl + '/purchase';

  create(purchase: Purchase): Observable<any> {
    return this.http.post<any>(this.context + '/save', purchase);
  }

  createBulk(purchases: Purchase[]): Observable<any> {
    return this.http.post<any>(this.context + '/saveBulk', purchases);
  }

  findByProduct(id:number, productId: number): Observable<Purchase[]> {
    return this.http.get<Purchase[]>(this.context + '/findByProduct/' + id + '/' + productId);
  }

  findForReasign(): Observable<Purchase[]> {
    return this.http.get<Purchase[]>(this.context + '/findReasign');
  }

  reasign(purchaseReasign: PurchaseReasign): Observable<any> {
    return this.http.put<any>(this.context + '/reasign', purchaseReasign, observeResponse);
  }

}