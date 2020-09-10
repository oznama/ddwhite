import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {Observable} from "rxjs/index";
import { baseUrl, observeResponse } from './../../environments/environment';
import {Company} from "../model/company.model";

@Injectable()
export class ApiCompanyService {

  constructor(private http: HttpClient) { }
  context: string = baseUrl + '/company';

  update(company: Company): Observable<any> {
    return this.http.put<any>(this.context + '/update', company, observeResponse);
  }

}