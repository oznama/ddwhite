import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {Observable} from "rxjs/index";
import { baseUrl } from './../../environments/environment';
import {Sale} from "../model/sale.model";

@Injectable()
export class ApiSaleService {

  constructor(private http: HttpClient) { }
  context: string = baseUrl + '/sale';

  create(sale: Sale): Observable<any> {
    return this.http.post<any>(this.context + '/save', sale);
  }

}