import { Component, OnInit } from '@angular/core';

import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router'; // Router to components
import { environment } from './../../environments/environment';
import { GlobalVar, alertOptions } from './../../app/app.component';
import { AlertService } from '../_alert';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  global = GlobalVar;

  constructor(private http:HttpClient, private router:Router, public alertService:AlertService){}

  ngOnInit(): void {
  }

  public login(username,password){

    var msgError = '';

    if(!username) 
      msgError += '- Username is requerid<br>';
    if(!password)
      msgError += '- Password is requerid<br>';
    
    if(msgError)
      this.alertService.warn(msgError, alertOptions);
    else {
      var uri = environment.apiUrl+environment.apiUrlLogin;
      var body = {
        "username": username,
        "password": password
      };
      this.http.post<any>(uri,body,{observe:'response'}).subscribe(response =>{
        if(response.status == 200){
          this.global.isAunthenticated = true;
          this.router.navigateByUrl('/home');
        }
      }, error =>{
        if(error.status == 404)
          this.alertService.warn('Username or password invalid', alertOptions);
        else
          this.alertService.error(error.message, alertOptions);
      })
    }

  }

}
