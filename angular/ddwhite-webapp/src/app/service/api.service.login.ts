import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { BehaviorSubject } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import {Observable} from "rxjs/index";
import { baseUrl, observeResponse } from './../../environments/environment';

@Injectable()
export class ApiLoginService {

	private loggedIn = new BehaviorSubject<boolean>(false);
	private userFullName = new BehaviorSubject<string>('');
	private role: string;
	private privileges: string[];

	private context: string = baseUrl + '/user';

	constructor(private router:Router, private http: HttpClient) { }

	login(loginPayload): Observable<any> {
		return this.http.post<any>(this.context +'/login', loginPayload, observeResponse);
	}

	logout() {
		this.loggedIn.next(false);
		this.router.navigate(['/login']);
	}

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

	set Role(role: string) {
		this.role = role;
	}

	get Role(){
		return this.role;
	}

	set Privileges(privileges: string[]){
		this.privileges = privileges
	}

	get Privileges(){
		return this.privileges;
	}

}