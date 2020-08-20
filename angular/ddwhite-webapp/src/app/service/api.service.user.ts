import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {Observable} from "rxjs/index";
import { baseUrl, observeResponse } from './api.service';

@Injectable()
export class ApiUserService {

  constructor(private http: HttpClient) { }
  context: string = baseUrl + '/user';

  login(loginPayload): Observable<any> {
    return this.http.post<any>(this.context +'/login', loginPayload, observeResponse);
  }
}
