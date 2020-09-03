import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { BehaviorSubject } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import {Observable} from "rxjs/index";
import { baseUrl, observeResponse } from './../../environments/environment';
import { User, UserPageable } from './../model/user.model';

@Injectable()
export class ApiUserService {

  private loggedIn = new BehaviorSubject<boolean>(false);
  private userFullName = new BehaviorSubject<string>('');

  set LoggedIn(b: boolean){
    this.loggedIn.next(b);
  }

  get isLoggedIn() {
    return this.loggedIn.asObservable();
  }

  set UserFullName(userFullName: string){
    this.userFullName.next(userFullName);
  }

  get fullName(){
    return this.userFullName.asObservable();
  }

  private context: string = baseUrl + '/user';

  constructor(private router:Router, private http: HttpClient) { }

  login(loginPayload): Observable<any> {
    return this.http.post<any>(this.context +'/login', loginPayload, observeResponse);
  }

  get(page: number, size: number, sort: string) : Observable<UserPageable> {
    console.log('A egg');
    return this.http.get<UserPageable>(this.context + '/find?page='+page+'&size='+size+'&sort='+sort);
  }

  getById(id: number): Observable<User> {
    return this.http.get<User>(this.context + '/find/' + id);
  }

  create(user: User): Observable<any> {
    return this.http.post<any>(this.context + '/save', user);
  }

  update(user: User): Observable<any> {
    return this.http.put<any>(this.context + '/update', user, observeResponse);
  }

  delete(id: number) {
    return this.http.delete<any>(this.context + '/delete/' + id);
  }

  logout() {
    this.loggedIn.next(false);
    this.router.navigate(['/login']);
  }
}
