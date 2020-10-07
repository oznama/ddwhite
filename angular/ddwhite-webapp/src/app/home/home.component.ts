import { Component, OnInit } from '@angular/core';
import {MatDialog} from '@angular/material/dialog';
import {SessionComponent} from '../login/session/session.component';
import { ApiSessionService, ApiLoginService, Privileges } from "../service/module.service";
import { Session } from '../model/user.model';
import { AlertService, alertOptions } from '../_alert';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  constructor(public dialog: MatDialog,
  	private apiService: ApiSessionService,
  	private loginService: ApiLoginService,
  	public alertService:AlertService,
    public privileges: Privileges){
  }

  ngOnInit(): void {
    this.cashIn();
  }

  private cashIn(){
    const userId = +window.localStorage.getItem("userId");
    this.apiService.getCurrentSession(userId).subscribe(data => {
      if(!data || !data.id){
        this.dialog.open(SessionComponent, { disableClose: !this.privileges.isAdmin() });
      }
    },error => {
      this.alertService.error('Error al recuperar session, error: ' + error.message, alertOptions)
      this.loginService.logout();
    });
  }

}
