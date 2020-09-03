import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {Observable} from "rxjs/index";
import { baseUrl, observeResponse } from './../../environments/environment';
import { Role, RolePageable } from './../model/role.model';

@Injectable()
export class ApiRoleService {

  private context: string = baseUrl + '/role';

  constructor(private http: HttpClient) { }

  get() : Observable<RolePageable> {
    return this.http.get<RolePageable>(this.context + '/find');
  }

}