import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {Observable} from "rxjs/index";
import { baseUrl, observeResponse } from './../../environments/environment';
import {Expense, ExpensePageable} from "../model/expense.model";

@Injectable()
export class ApiExpenseService {

  constructor(private http: HttpClient) { }
  context: string = baseUrl + '/expense';

  get() : Observable<ExpensePageable> {
    return this.http.get<ExpensePageable>(this.context + '/find');
  }

  getById(id: number): Observable<Expense> {
    return this.http.get<Expense>(this.context + '/find/' + id);
  }

  create(expense: Expense): Observable<any> {
    return this.http.post<any>(this.context + '/save', expense);
  }
}