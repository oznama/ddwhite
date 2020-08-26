import { Component, OnInit, Optional, Inject } from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {Observable, of} from 'rxjs';
import {map} from 'rxjs/operators';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Client } from './../../model/client.model';
import { ApiClientService } from './../../service/api.service.client';

@Component({
  selector: 'client-dialog-search-component',
  templateUrl: 'client-dialog-search-component.html',
})
export class ClientDialogSearchComponent implements OnInit {
  searchForm: FormGroup;
  clients: Observable<Client[]>;
  clientsFiltred$: Observable<Client[]>;

  constructor(
    public dialogRef: MatDialogRef<ClientDialogSearchComponent>, @Optional() @Inject(MAT_DIALOG_DATA) public dClient: Client,
    private apiService: ApiClientService, 
    private formBuilder: FormBuilder) {
  }

  ngOnInit() {
    this.searchForm = this.formBuilder.group({
      name: [],
      midleName: [],
      lastName: [],
      rfc: [],
    });
    this.loadClients();
  }

  private loadClients() {
    this.apiService.get().subscribe( data => {
        this.clients = of(data.content);
        this.clientsFiltred$ = of(data.content);
      }
    )
  }

  doFilter(): void{
    var name = this.searchForm.get('name').value;
    var midleName = this.searchForm.get('midleName').value;
    var lastName = this.searchForm.get('lastName').value;
    var rfc = this.searchForm.get('rfc').value;
    if( rfc ) {
      this.clientsFiltred$ = this.clients.pipe(map( 
        items => items.filter( 
          client => client.rfc.toLowerCase().includes(rfc)
        )
      ));
    } else if(name || midleName || lastName){
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

  select(client: Client): void{
    this.dialogRef.close({ event: 'close', data: client });
  }

}