import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AlertService, alertOptions } from './_alert';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {

  //logged:boolean = false;

  constructor(private router:Router, public alertService:AlertService) { }

  ngOnInit(): void {
  	/*let userLogged = window.localStorage.getItem("user");
    if(!userLogged) {
      this.alertService.warn("Iniciar sesion", alertOptions);
      this.router.navigate(['']);
      return;
    } else {
    	this.logged = true;
    }*/
  }

  logout(){
  	window.localStorage.removeItem("token");
  	window.localStorage.removeItem("id");
    this.router.navigate(['']);
  }
  
}
