import { Component, OnInit } from '@angular/core';

import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { environment } from './../../environments/environment';

@Component({
  selector: 'app-singup',
  templateUrl: './singup.component.html',
  styleUrls: ['./singup.component.css']
})
export class SingupComponent implements OnInit {

  constructor(private http:HttpClient, private router:Router) { }

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
  		console.log(response.status);
  		if(response.status == 200){
  			alert('Usuario registrado');
  			this.router.navigateByUrl('/login');
  		}
  	}, error =>{
  		console.log(error);
  	})
  }

}
