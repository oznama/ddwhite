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

  constructor(
    private formBuilder: FormBuilder, 
    private router:Router, 
    private apiService:ApiUserService, 
    public alertService:AlertService
   ){}

  ngOnInit(): void {
    this.loginForm = this.formBuilder.group({
      username: ['', [Validators.required,Validators.pattern("[a-zA-Z0-9]{1,}")]],
      password: ['', [Validators.required,Validators.pattern("[a-zA-Z0-9]{4,}")]]
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
      if(response.status === 200){
        this.alertService.success('Bienvenido ' + response.body.fullName, alertOptions);
        window.localStorage.setItem('userId', response.body.id);
        this.apiService.LoggedIn = true;
        this.router.navigate(['home']);
      } else {
        this.alertService.warn(response.message, alertOptions);
      }
    }, error =>{
      if(error.status == 403)
        this.alertService.warn(error.error, alertOptions);
      else
        this.alertService.error(error.error, alertOptions);
    })
  }

}
