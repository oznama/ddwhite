import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {Observable} from "rxjs/index";
import { baseUrl, observeResponse } from './../../environments/environment';
import {Session} from "../model/user.model";
import {Withdrawal} from "../model/cashout.model";

@Injectable()
export class ApiSessionService {

	private context: string = baseUrl + '/session';

	constructor(private http: HttpClient) { }

	getByRange(startDate: Date, endDate: Date) : Observable<Session[]> {
		const queryParam = startDate && endDate ? '?startDate='+ startDate +'&endDate='+ endDate : '';
		return this.http.get<Session[]>(this.context + '/findByRange' + queryParam);
	}

	getById(id: number): Observable<Session> {
		return this.http.get<Session>(this.context + '/find/' + id);
	}

	getCurrentSession(userId: number): Observable<Session> {
		return this.http.get<Session>(this.context + '/findCurrentSession?userId=' + userId);
	}

	create(session: Session): Observable<any> {
		return this.http.post<any>(this.context + '/save', session);
	}

	close(id: number, finalAmount: number): Observable<any> {
		return this.http.put<any>(this.context + '/close?id=' + id + '&finalAmount=' + finalAmount, observeResponse);
	}

	getWithdrawals(id: number): Observable<Withdrawal[]> {
		return this.http.get<Withdrawal[]>(this.context + '/withdrawals?id=' + id);
	}

	update(session: Session): Observable<any> {
		return this.http.put<any>(this.context + '/update', session);
	}

}