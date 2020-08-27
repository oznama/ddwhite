import { Component, OnInit } from '@angular/core';
import {Router} from "@angular/router";
import {FormBuilder, FormGroup} from "@angular/forms";
import {User} from "../../model/user.model";
import { ApiUserService } from "../../service/module.service";
import { AlertService, alertOptions } from '../../_alert';
import { Observable, of, combineLatest } from 'rxjs/index';
import { map, withLatestFrom, startWith, tap } from 'rxjs/operators';

@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.css']
})
export class UserListComponent implements OnInit {

  searchForm: FormGroup;
  users: Observable<User[]>;
  usersFiltred$: Observable<User[]>;

  constructor(
  	private router:Router, 
    private apiService:ApiUserService, 
    public alertService:AlertService,
    private formBuilder: FormBuilder) { }

  ngOnInit(): void {
  	this.searchForm = this.formBuilder.group({
      username: [],
      fullName: []
    });
    this.loadUsers();
  }

  private loadUsers() {
    this.apiService.get().subscribe( data => {
        this.users = of(data.content);
        this.usersFiltred$ = of(data.content);
      }
    )
  }

  delete(user:User): void{
  	this.apiService.delete(user.id)
  	  .subscribe( response => {
        this.users = this.users.pipe(map( items => items.filter( u => u != user)));
        this.loadUsers();
        this.alertService.success('Usuario eliminado', alertOptions);
  	  }, error => {
        this.alertService.error(error.error, alertOptions);
      }
    );
  }

  add(): void{
    this.router.navigate(['user-add']);  
  }

  edit(user:User): void{
    window.localStorage.removeItem("editUserId");
    window.localStorage.setItem("editUserId", user.id.toString());
  	this.router.navigate(['user-edit']);
  }

  doFilter(): void{
    var username = this.searchForm.get('username').value;
    var fullName = this.searchForm.get('fullName').value;
    if(username || fullName){
      if( username && fullName){
        this.usersFiltred$ = this.users.pipe(map( 
          items => items.filter( 
            user => (user.username.toLowerCase().includes(username) 
                      && user.fullName.toLowerCase().includes(fullName))
          )
        ));
      } else if( username ){
        this.usersFiltred$ = this.users.pipe(map( 
          items => items.filter( 
            user => user.username.toLowerCase().includes(username)
          )
        ));
      } else if( fullName ){
        this.usersFiltred$ = this.users.pipe(map( 
          items => items.filter( 
            user => user.fullName.toLowerCase().includes(fullName)
          )
        ));
      }
    }
  }

  clearFilter(): void{
    this.searchForm.reset();
    this.usersFiltred$ = this.users;
  }

}
