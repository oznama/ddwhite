import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {Observable} from "rxjs/index";
import { baseUrl, observeResponse } from './../../environments/environment';
import {Session} from "../model/user.model";

@Injectable()
export class ApiSessionService {

	private context: string = baseUrl + '/session';

	constructor(private http: HttpClient) { }

	getByRange(startDate: Date, endDate: Date) : Observable<Session[]> {
		return this.http.get<Session[]>(this.context + '/findByRange?startDate='+ startDate +'&endDate='+ endDate);
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

	update(id: number): Observable<any> {
		return this.http.put<any>(this.context + '/close?id=' + id, observeResponse);
	}

}