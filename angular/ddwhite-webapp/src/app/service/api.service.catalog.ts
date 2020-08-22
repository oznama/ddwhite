import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {Observable} from "rxjs/index";
import { baseUrl, observeResponse } from './../../environments/environment';
import {ApiResponse} from "../model/api.response";
import {Catalog} from "../model/catalog.model";

@Injectable()
export class ApiCatalogService {

  constructor(private http: HttpClient) { }
  context: string = baseUrl + '/catalog';

  get() : Observable<Catalog[]> {
    return this.http.get<Catalog[]>(this.context + '/find');
  }

  getById(id: number): Observable<Catalog> {
    return this.http.get<Catalog>(this.context + '/find/' + id);
  }

  getByName(name: string): Observable<Catalog> {
    return this.http.get<Catalog>(this.context + '/findByName/' + name);
  }

}