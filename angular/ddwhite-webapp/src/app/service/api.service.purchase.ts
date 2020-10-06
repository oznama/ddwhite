import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {Observable} from "rxjs/index";
import { baseUrl, observeResponse } from './../../environments/environment';
import {Purchase, PurchaseReasign, PurchaseList} from "../model/purchase.model";

@Injectable()
export class ApiPurchaseService {

  constructor(private http: HttpClient) { }
  context: string = baseUrl + '/purchase';

  create(purchase: Purchase): Observable<any> {
    return this.http.post<any>(this.context + '/save', purchase);
  }

  update(purchase: Purchase): Observable<any> {
    return this.http.put<any>(this.context + '/update', purchase, observeResponse);
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

  getList(startDate: Date, endDate: Date) : Observable<PurchaseList[]> {
    return this.http.get<PurchaseList[]>(this.context + '/getList?startDate='+ startDate +'&endDate='+ endDate);
  }

  getOne(id: number) : Observable<PurchaseList> {
    return this.http.get<PurchaseList>(this.context + '/getOne/'+ id);
  }

}