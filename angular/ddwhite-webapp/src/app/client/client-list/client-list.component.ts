import { Component, OnInit } from '@angular/core';
import {Router} from "@angular/router";
import {FormBuilder, FormGroup} from "@angular/forms";
import {Client} from "../../model/client.model";
import { ApiClientService, pageSize, Privileges } from "../../service/module.service";
import { AlertService, alertOptions } from '../../_alert';
import { Observable, of, combineLatest } from 'rxjs/index';
import { map, withLatestFrom, startWith, tap } from 'rxjs/operators';

@Component({
  selector: 'app-client-list',
  templateUrl: './client-list.component.html',
  styleUrls: ['./client-list.component.css']
})
export class ClientListComponent implements OnInit {
  
  searchForm: FormGroup;
  clients: Observable<Client[]>;

  page: number = 0;
  sort: string = 'midleName,lastName,name,asc';
  totalPage: number;

  constructor(
  	private router:Router, 
    private apiService:ApiClientService, 
    public alertService:AlertService,
    public privileges:Privileges,
    private formBuilder: FormBuilder) {
  }

  ngOnInit(): void {
    this.searchForm = this.formBuilder.group({
      name: [],
      midleName: [],
      lastName: []
    });
    this.loadClients(this.page);
  }

  private loadClients(page: number) {
    this.apiService.get(page, pageSize, this.sort).subscribe( data => {
      this.totalPage = data.totalPages;
      this.clients = of(data.content);
    })
  }

  pagination(page:number): void {
    if( page >= 0 && page < this.totalPage && page != this.page ) {
      this.page = page;
      this.loadClients(this.page);
    }
  }

  delete(client:Client): void{
  	this.apiService.delete(client.id)
  	  .subscribe( response => {
        this.clients = this.clients.pipe(map( items => items.filter( c => c != client)));
        this.loadClients(this.page);
        this.alertService.success('Cliente eliminado', alertOptions);
  	  }, error => {
        this.alertService.error(error.error, alertOptions);
      }
    );
  }

  edit(client:Client): void{
    window.localStorage.removeItem("editClientId");
    window.localStorage.setItem("editClientId", client.id.toString());
  	this.router.navigate(['client-edit']);
  }

  doFilter(): void{
    const name = this.searchForm.controls.name.value;
    const midleName = this.searchForm.controls.midleName.value;
    const lastName = this.searchForm.controls.lastName.value;
    const fullName = (name ? name + ' ' : '')  + ( midleName ? midleName + ' ' : '') + ( lastName ? lastName : '');
    this.apiService.findByName(fullName).subscribe( data => {
      this.clients = of(data)
      this.totalPage = 1;
    })
  }

  clearFilter(): void{
    this.searchForm.reset();
    this.loadClients(this.page);
  }

}
