import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { BehaviorSubject } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import {Observable} from "rxjs/index";
import { baseUrl, observeResponse } from './../../environments/environment';

@Injectable()
export class ApiUserService {

  private loggedIn = new BehaviorSubject<boolean>(false);

  set LoggedIn(b: boolean){
    this.loggedIn.next(b);
  }

  get isLoggedIn() {
    return this.loggedIn.asObservable();
  }

  private context: string = baseUrl + '/user';

  constructor(private router:Router, private http: HttpClient) { }

  login(loginPayload): Observable<any> {
    return this.http.post<any>(this.context +'/login', loginPayload, observeResponse);
  }

  logout() {
    this.loggedIn.next(false);
    this.router.navigate(['/login']);
  }
}
