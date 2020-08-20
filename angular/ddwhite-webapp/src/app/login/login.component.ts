import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import { Router } from '@angular/router';
import { AlertService, alertOptions } from '../_alert';
import { ApiUserService } from "../service/api.service";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  loginForm: FormGroup;
  invalidLogin: boolean = false;

  constructor(private formBuilder: FormBuilder, private router:Router, private apiService:ApiUserService, public alertService:AlertService){}

  ngOnInit(): void {
    this.loginForm = this.formBuilder.group({
      id: [],
      username: ['', Validators.required],
      password: ['', Validators.required]
    });
  }

  login(){

    if (this.loginForm.invalid) {
      return;
    }

    const body = {
      username: this.loginForm.controls.username.value,
      password: this.loginForm.controls.password.value
    }
    this.apiService.login(body).subscribe(response => {
      var token = response.headers.get('token');
      if(response.status === 200 && token){
        window.localStorage.setItem('token', token);
        window.localStorage.setItem('id', response.body.id);
        this.alertService.success('Bienvenido ' + response.body.fullName, alertOptions);
        this.router.navigate(['home']);
      } else {
        this.invalidLogin = true;
        this.alertService.warn(response.message, alertOptions);
      }
    }, error =>{
      console.log(error);
      if(error.status == 404)
        this.alertService.warn('Username or password invalid', alertOptions);
      else
        this.alertService.error(error.message, alertOptions);
    })

  }

}
