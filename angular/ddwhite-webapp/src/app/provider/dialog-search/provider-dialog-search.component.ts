import { Component, OnInit, Optional, Inject } from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {Observable, of} from 'rxjs';
import {map} from 'rxjs/operators';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Provider } from './../../model/provider.model';
import { ApiProviderService } from './../../service/api.service.provider';

@Component({
  selector: 'provider-dialog-search',
  templateUrl: 'provider-dialog-search.component.html',
})
export class ProviderDialogSearchComponent implements OnInit {
  searchForm: FormGroup;
  providers: Observable<Provider[]>;
  providersFiltred$: Observable<Provider[]>;

  constructor(
    public dialogRef: MatDialogRef<ProviderDialogSearchComponent>, @Optional() @Inject(MAT_DIALOG_DATA) public dProvider: Provider,
    private apiService: ApiProviderService, 
    private formBuilder: FormBuilder) {
  }

  ngOnInit() {
    this.searchForm = this.formBuilder.group({
      bussinesName: []
    });
    this.loadProviders();
  }

  private loadProviders() {
    this.apiService.get().subscribe( data => {
        this.providers = of(data.content);
        this.providersFiltred$ = of(data.content);
      }
    )
  }

  doFilter(): void{
    var bussinesName = this.searchForm.get('bussinesName').value;
    if( bussinesName ){
      this.providersFiltred$ = this.providers.pipe(map( 
        items => items.filter( 
          provider => provider.bussinesName.toLowerCase().includes(bussinesName)
        )
      ));
    }
  }

  clearFilter(): void{
    this.searchForm.reset();
    this.providersFiltred$ = this.providers;
  }

  select(provider: Provider): void{
    this.dialogRef.close({ event: 'close', data: provider });
  }

}