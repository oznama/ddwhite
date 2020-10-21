import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {Observable} from "rxjs/index";
import { baseUrl, observeResponse } from './../../environments/environment';
import {Sale, SalePageable} from "../model/sale.model";

@Injectable()
export class ApiSaleService {

  constructor(private http: HttpClient) { }
  context: string = baseUrl + '/sale';

  get(startDate: string, endDate: string, page: number, size: number, sort: string) : Observable<SalePageable> {
    return this.http.get<SalePageable>(this.context + '/find/bydates?start='+ startDate +'&end='+ endDate + '&page='+page+'&size='+size+'&sort='+sort);
  }

  create(sale: Sale): Observable<any> {
    return this.http.post<any>(this.context + '/save', sale);
  }

  update(sale: Sale): Observable<any> {
    return this.http.put<any>(this.context + '/update', sale, observeResponse);
  }

  getExcedent(userId: number) {
    return this.http.get<number>(this.context + '/getExcedent?userId='+ userId);
  }

  getChasInRegister(userId: number) {
    return this.http.get<number>(this.context + '/getChasInRegister?userId='+ userId);
  }

  getLastSaleId() {
    return this.http.get<number>(this.context + '/getLastSaleId');
  }

}