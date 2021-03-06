import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {Observable} from "rxjs/index";
import { baseUrl, observeResponse } from './../../environments/environment';
import {Provider, ProviderPageable} from "../model/provider.model";

@Injectable()
export class ApiProviderService {

  constructor(private http: HttpClient) { }
  context: string = baseUrl + '/provider';

  get(page: number, size: number, sort: string) : Observable<ProviderPageable> {
    return this.http.get<ProviderPageable>(this.context + '/find?page='+page+'&size='+size+'&sort='+sort);
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