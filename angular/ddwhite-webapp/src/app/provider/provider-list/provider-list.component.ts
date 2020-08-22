import { Component, OnInit } from '@angular/core';
import {Router} from "@angular/router";
import {FormBuilder, FormGroup} from "@angular/forms";
import {Provider} from "../../model/provider.model";
import { ApiProviderService } from "../../service/api.service";
import { AlertService, alertOptions } from '../../_alert';
import { Observable, of, combineLatest } from 'rxjs/index';
import { map, withLatestFrom, startWith, tap } from 'rxjs/operators';

@Component({
  selector: 'app-provider-list',
  templateUrl: './provider-list.component.html',
  styleUrls: ['./provider-list.component.css']
})
export class ProviderListComponent implements OnInit {

  searchForm: FormGroup;
  //providers: Provider[];
  providers: Observable<Provider[]>;
  providersFiltred$: Observable<Provider[]>;

  constructor(
    private router:Router, 
    private apiService:ApiProviderService, 
    public alertService:AlertService,
    private formBuilder: FormBuilder) {
  }

  ngOnInit() {
    this.searchForm = this.formBuilder.group({
      bussinesName: [],
      contactName: [],
    });
    this.loadProviders();
  }

  private loadProviders() {
    this.apiService.get().subscribe( data => {
        //this.providers = data.content;
        this.providers = of(data.content);
        this.providersFiltred$ = of(data.content);
      }
    )
  }

  delete(provider:Provider): void{
  	this.apiService.delete(provider.id)
  	  .subscribe( response => {
  	  	//this.providers = this.providers.filter( p => p != provider);
        this.providers = this.providers.pipe(map( items => items.filter( p => p != provider)));
        this.loadProviders();
        this.alertService.success('Proveedor eliminado', alertOptions);
  	  }, error => {
        console.log(error);
        this.alertService.error(error.error, alertOptions);
      }
    );
  }

  edit(provider:Provider): void{
    window.localStorage.removeItem("editProviderId");
    window.localStorage.setItem("editProviderId", provider.id.toString());
  	this.router.navigate(['provider-edit']);
  }

  add(): void{
  	this.router.navigate(['provider-add']);	
  }

  doFilter(): void{
    var bussinesName = this.searchForm.get('bussinesName').value;
    var contactName = this.searchForm.get('contactName').value;
    if(bussinesName || contactName){
      if( bussinesName && contactName ){
        this.providersFiltred$ = this.providers.pipe(map( 
          items => items.filter( 
            provider => (provider.bussinesName.toLowerCase().includes(bussinesName) 
                      && provider.contactName.toLowerCase().includes(contactName))
          )
        ));
      } else if( bussinesName ){
        this.providersFiltred$ = this.providers.pipe(map( 
          items => items.filter( 
            provider => provider.bussinesName.toLowerCase().includes(bussinesName)
          )
        ));
      } else if( contactName ){
        this.providersFiltred$ = this.providers.pipe(map( 
          items => items.filter( 
            provider => provider.contactName.toLowerCase().includes(contactName)
          )
        ));
      }
    }
  }

  clearFilter(): void{
    this.searchForm.reset();
    this.providersFiltred$ = this.providers;
  }

}
