import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {Observable} from "rxjs/index";
import { baseUrl, observeResponse } from './api.service';
import {ApiResponse} from "../model/api.response";
import {Provider, ProviderPageable} from "../model/provider.model";

@Injectable()
export class ApiProviderService {

  constructor(private http: HttpClient) { }
  context: string = baseUrl + '/provider';

  get() : Observable<ProviderPageable> {
    return this.http.get<ProviderPageable>(this.context + '/find');
  }

  getById(id: number): Observable<Provider> {
    return this.http.get<Provider>(this.context + '/find/' + id);
  }

  create(provider: Provider): Observable<any> {
    return this.http.post<any>(this.context + '/save', provider);
  }

  update(provider: Provider): Observable<any> {
    return this.http.put<any>(this.context + '/update', provider, observeResponse);
  }

  delete(id: number) {
    return this.http.delete<any>(this.context + '/delete/' + id);
  }
}