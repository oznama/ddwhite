import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

export const GlobalVar = {
	isAunthenticated: false
};

export const alertOptions = {
    autoClose: true,
    keepAfterRouteChange: false
  };

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {

  global = GlobalVar;

  constructor(private router:Router){}

  ngOnInit(): void {
  }

   public logout(){
    this.global.isAunthenticated = false;
    this.router.navigateByUrl('/');
  }
  
}
