import { Component, OnInit, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Sale, SalePayment } from './../../model/sale.model';
import {CatalogItem} from './../../model/catalog.model';
import { ApiCatalogService, ApiUserService } from './../../service/module.service';

@Component({
  selector: 'sale-ticket',
  templateUrl: './ticket.component.html',
  styleUrls: ['./ticket.component.css']
})
export class TicketComponent implements OnInit {

  date: Date = new Date();

  bussinesName: string;
  address: string;
  phone: string;
  website: string;
  email: string
  message: string;
  fiscalName: string;
  rfc: string;
  userName: string;

  constructor(public dialogRef: MatDialogRef<TicketComponent>, @Inject(MAT_DIALOG_DATA) public data: Sale,
    private catalogService: ApiCatalogService,
    private userService: ApiUserService ) { }

  ngOnInit(): void {
    this.userService.fullName.subscribe( value => this.userName = value.toUpperCase());
    this.loadCompanyData();
  }

  imprimir(){
    window.print();
  }

  private loadCompanyData(): void{
    this.catalogService.getByName('COMPANY').subscribe( response => {
      if(response && response.items){
        response.items.forEach( (value, index) => {
          switch(value.name){
            case 'NOMBRE':
              this.bussinesName = value.description;
              break;
            case 'DIRECCION':
              this.address = value.description;
              break;
            case 'TELEFONO':
              this.phone = value.description;
              break;
            case 'PAGINA':
              this.website = value.description;
              break;
            case 'EMAIL':
              this.email = value.description;
              break;
            case 'NOMBRE_FISCAL':
              this.fiscalName = value.description;
              break;
            case 'RFC':
              this.rfc = value.description;
              break;
            case 'MESSAGE_TICKET':
              this.message = value.description;
              break;
            default:
          }
        });
      }
    }, error =>{
      console.error(error);
    });
  }

}
