import { Component, OnInit, Inject } from '@angular/core';
import {Router} from "@angular/router";
import {Provider} from "../../model/provider.model";
import { ApiProviderService } from "../../service/api.service";
import { AlertService, alertOptions } from '../../_alert';

@Component({
  selector: 'app-provider-list',
  templateUrl: './provider-list.component.html',
  styleUrls: ['./provider-list.component.css']
})
export class ProviderListComponent implements OnInit {

  providers: Provider[];

  constructor(private router:Router, private apiService:ApiProviderService, public alertService:AlertService) { }

  ngOnInit() {
  	this.apiService.get()
  	  .subscribe( data => {
  	  	this.providers = data.content;
  	  })
  }

  delete(provider:Provider): void{
  	this.apiService.delete(provider.id)
  	  .subscribe( response => {
  	  	this.providers = this.providers.filter( p => p != provider);
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

}
