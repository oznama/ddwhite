import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {Observable} from "rxjs/index";
import { baseUrl } from './../../environments/environment';
import {Purchase} from "../model/purchase.model";

@Injectable()
export class ApiPurchaseService {

  constructor(private http: HttpClient) { }
  context: string = baseUrl + '/purchase';

  create(purchase: Purchase): Observable<any> {
    return this.http.post<any>(this.context + '/save', purchase);
  }

}