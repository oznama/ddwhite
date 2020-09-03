import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {Observable} from "rxjs/index";
import { baseUrl, observeResponse } from './../../environments/environment';
import { User, UserPageable } from './../model/user.model';

@Injectable()
export class ApiUserService {

  private context: string = baseUrl + '/user';

  constructor(private http: HttpClient) { }

  get(page: number, size: number, sort: string) : Observable<UserPageable> {
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

}
