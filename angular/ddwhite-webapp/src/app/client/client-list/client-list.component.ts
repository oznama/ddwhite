import { Component, OnInit } from '@angular/core';
import {Router} from "@angular/router";
import {FormBuilder, FormGroup} from "@angular/forms";
import {Client} from "../../model/client.model";
import { ApiClientService, pageSize } from "../../service/module.service";
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
  clientsFiltred$: Observable<Client[]>;

  page: number = 0;
  sort: string = 'midleName,lastName,name,asc';
  totalPage: number;

  constructor(
  	private router:Router, 
    private apiService:ApiClientService, 
    public alertService:AlertService,
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
      this.clientsFiltred$ = of(data.content);
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
    var name = this.searchForm.get('name').value;
    var midleName = this.searchForm.get('midleName').value;
    var lastName = this.searchForm.get('lastName').value;
    if(name || midleName || lastName){
      if( name && midleName && lastName){
        this.clientsFiltred$ = this.clients.pipe(map( 
          items => items.filter( 
            client => (client.name.toLowerCase().includes(name) 
                      && client.midleName.toLowerCase().includes(midleName)
                      && client.lastName.toLowerCase().includes(lastName))
          )
        ));
      } else if( name && midleName){
        this.clientsFiltred$ = this.clients.pipe(map( 
          items => items.filter( 
            client => (client.name.toLowerCase().includes(name) 
                      && client.midleName.toLowerCase().includes(midleName))
          )
        ));
      } else if( name && lastName){
        this.clientsFiltred$ = this.clients.pipe(map( 
          items => items.filter( 
            client => (client.name.toLowerCase().includes(name) 
                      && client.lastName.toLowerCase().includes(lastName))
          )
        ));
      } else if( midleName && lastName){
        this.clientsFiltred$ = this.clients.pipe(map( 
          items => items.filter( 
            client => (client.midleName.toLowerCase().includes(midleName)
                      && client.lastName.toLowerCase().includes(lastName))
          )
        ));
      } else if( name ){
        this.clientsFiltred$ = this.clients.pipe(map( 
          items => items.filter( 
            client => client.name.toLowerCase().includes(name)
          )
        ));
      } else if( midleName ){
        this.clientsFiltred$ = this.clients.pipe(map( 
          items => items.filter( 
            client => client.midleName.toLowerCase().includes(midleName)
          )
        ));
      } else if( lastName ){
        this.clientsFiltred$ = this.clients.pipe(map( 
          items => items.filter( 
            client => client.lastName.toLowerCase().includes(lastName)
          )
        ));
      }
    }
  }

  clearFilter(): void{
    this.searchForm.reset();
    this.clientsFiltred$ = this.clients;
  }

}
