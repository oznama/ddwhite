import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {Observable} from "rxjs/index";
import { baseUrl, observeResponse } from './../../environments/environment';
import {Client, ClientPageable} from "../model/client.model";

@Injectable()
export class ApiClientService {

  constructor(private http: HttpClient) { }
  context: string = baseUrl + '/client';

  get() : Observable<ClientPageable> {
    return this.http.get<ClientPageable>(this.context + '/find');
  }

  getById(id: number): Observable<Client> {
    return this.http.get<Client>(this.context + '/find/' + id);
  }

  create(client: Client): Observable<any> {
    return this.http.post<any>(this.context + '/save', client);
  }

  update(client: Client): Observable<any> {
    return this.http.put<any>(this.context + '/update', client, observeResponse);
  }

  delete(id: number) {
    return this.http.delete<any>(this.context + '/delete/' + id);
  }
}