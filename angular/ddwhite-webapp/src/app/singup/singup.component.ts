import { Component, OnInit } from '@angular/core';

import { HttpClient } from '@angular/common/http';
import { environment } from './../../environments/environment';
import { alertOptions } from './../../app/app.component';
import { AlertService } from '../_alert';

@Component({
  selector: 'app-singup',
  templateUrl: './singup.component.html',
  styleUrls: ['./singup.component.css']
})
export class SingupComponent implements OnInit {

  constructor(private http:HttpClient, public alertService:AlertService) { }

  ngOnInit(): void {
  	
  }

  public createUser(fullName,username,password){
  	var uri = environment.apiUrl+environment.apiUrlCreateUser;
  	var body = {
  		"username": username,
  		"password": password,
  		"fullName": fullName
  	};
  	this.http.post<any>(uri,body,{observe:'response'}).subscribe(response =>{
  		if(response.status == 200){
        this.alertService.success('Usuario registrado', alertOptions);
  		}
  	}, error =>{
  		this.alertService.error(error.message, alertOptions);
  	})
  }

}
