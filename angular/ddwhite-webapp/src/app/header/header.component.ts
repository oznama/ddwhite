import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { ApiUserService } from './../service/api.service.user';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  isLoggedIn$: Observable<boolean>;

  constructor(private apiService: ApiUserService) { }

  ngOnInit() {
    this.isLoggedIn$ = this.apiService.isLoggedIn;
  }

  onLogout(){
    this.apiService.logout();
  }
}