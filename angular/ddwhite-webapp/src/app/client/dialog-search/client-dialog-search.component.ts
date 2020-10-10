import { Component, OnInit, Optional, Inject } from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {Observable, of} from 'rxjs';
import {map} from 'rxjs/operators';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Client } from './../../model/client.model';
import { ApiClientService,pageSize } from './../../service/module.service';

@Component({
  selector: 'client-dialog-search',
  templateUrl: 'client-dialog-search.component.html',
})
export class ClientDialogSearchComponent implements OnInit {
  searchForm: FormGroup;
  clients: Observable<Client[]>;

  page: number = 0;
  sort: string = 'midleName,lastName,name,asc';
  totalPage: number;

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
    //this.loadClients(this.page);
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

  doFilter(): void{
    const rfc = this.searchForm.controls.rfc.value;
    if( rfc ) {
      this.apiService.findByRfc(rfc).subscribe( data => this.clients = of(data))
    } else {
      const name = this.searchForm.controls.name.value;
      const midleName = this.searchForm.controls.midleName.value;
      const lastName = this.searchForm.controls.lastName.value;
      const fullName = (name ? name + ' ' : '')  + ( midleName ? midleName + ' ' : '') + ( lastName ? lastName : '');
      this.apiService.findByName(fullName).subscribe( data => this.clients = of(data))
    }
    this.totalPage = 1;
  }

  clearFilter(): void{
    this.searchForm.reset();
    this.loadClients(this.page);
  }

  select(client: Client): void{
    this.dialogRef.close({ event: 'close', data: client });
  }

}