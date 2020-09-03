import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { ApiLoginService, Privileges } from './../service/module.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  isLoggedIn$: Observable<boolean>;
  userFullName$: Observable<string>;

  constructor(
    private apiService: ApiLoginService, 
    public privileges: Privileges) { }

  ngOnInit() {
    this.isLoggedIn$ = this.apiService.isLoggedIn;
    this.userFullName$ = this.apiService.fullName;
  }

  onLogout(){
    this.apiService.logout();
  }
}