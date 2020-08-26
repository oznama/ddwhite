import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { ApiUserService } from './../service/module.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  isLoggedIn$: Observable<boolean>;
  userFullName: string;

  constructor(private apiService: ApiUserService) { }

  ngOnInit() {
    this.isLoggedIn$ = this.apiService.isLoggedIn;
    this.userFullName = window.localStorage.getItem("userFullName"); 
  }

  onLogout(){
    this.apiService.logout();
  }
}