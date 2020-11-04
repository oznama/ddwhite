import { Component, OnInit } from '@angular/core';
import {Router} from "@angular/router";
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import { Observable, of } from 'rxjs/index';
import {first, map} from "rxjs/operators";
import {Session} from '../../../model/user.model';
import { ApiSessionService, Privileges, CAT_CONST } from '../../../service/module.service';

@Component({
  selector: 'app-session-list',
  templateUrl: './session-list.component.html',
  styleUrls: ['./session-list.component.css']
})
export class SessionListComponent implements OnInit {

  searchForm: FormGroup;
  sessions: Observable<Session[]>;
  sessionFiltred: Observable<Session[]>;
  hasElements: boolean = false;

  constructor(private formBuilder: FormBuilder,
  	private router:Router,
  	private apiService: ApiSessionService,
  	public privileges:Privileges) { }

  ngOnInit(): void {
  	this.searchForm = this.formBuilder.group({
      startDate: [],
      endDate: [],
      userFullname: []
    });
    /*
    var now = new Date();
    now.setHours(0,0,0,0);
    this.loadSessionByDates(now, now);
    */
    this.loadSession();
  }

  private loadSessionByDates(startDate: Date, endDate: Date){
  	this.apiService.getByRange(startDate, endDate).subscribe(response => {
    	this.sessions = of(response);
      this.sessionFiltred = of(response);
      this.hasElements = true;
    }, error => this.hasElements = false);
  }

  private loadSession(){
    this.apiService.getByRange(null, null).subscribe(response => {
      this.sessions = of(response);
      this.sessionFiltred = of(response);
      this.hasElements = true;
    }, error => this.hasElements = false);
  }

  search() {
    this.loadSessionByDates(this.searchForm.controls.startDate.value, this.searchForm.controls.endDate.value);
  }

  edit(sessionId: number){
  	window.localStorage.removeItem("editSessionId");
    window.localStorage.setItem("editSessionId", sessionId.toString());
  	this.router.navigate(['session-edit']);
  }

  doFilter(): void{
    var userFullname = this.searchForm.controls.userFullname.value;
    if( userFullname ){
      this.sessionFiltred = this.sessions.pipe(map( 
        items => items.filter(session => session.userFullname.toUpperCase().includes(userFullname.toUpperCase()))
      ));
    }
  }

  clearFilter(): void{
    this.searchForm.controls.userFullname.setValue(null);
    this.sessionFiltred = this.sessions;
  }

}
