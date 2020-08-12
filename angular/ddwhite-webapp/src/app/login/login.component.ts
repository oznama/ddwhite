import { Component, OnInit } from '@angular/core';

import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router'; // Router to components
import { environment } from './../../environments/environment';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  constructor(private http:HttpClient, private router:Router){}

  ngOnInit(): void {

  }

  public login(username,password){
  	var uri = environment.apiUrl+environment.apiUrlLogin;
  	var body = {
  		"username": username,
  		"password": password
  	};
  	this.http.post<any>(uri,body,{observe:'response'}).subscribe(response =>{
  		console.log(response.status);
  		if(response.status == 200){
  			alert('Usuario valido, bienvenido');
  			this.router.navigateByUrl('/home');
  		}
  	}, error =>{
  		console.log(error);
  	})
  }

}
