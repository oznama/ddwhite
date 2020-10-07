import { Component, OnInit, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Sale, SalePayment } from '../../model/sale.model';
import {CatalogItem} from '../../model/catalog.model';
import { Company } from '../../model/company.model';
import { ApiCatalogService, ApiLoginService, CAT_CONST } from '../../service/module.service';

@Component({
  selector: 'sale-ticket',
  templateUrl: './ticket.component.html',
  styleUrls: ['./ticket.component.css']
})
export class TicketComponent implements OnInit {

  date: Date = new Date();
  company: Company = new Company();
  userName: string;

  constructor(public dialogRef: MatDialogRef<TicketComponent>, @Inject(MAT_DIALOG_DATA) public data: Sale,
    private catalogService: ApiCatalogService,
    private loginService: ApiLoginService ) { }

  ngOnInit(): void {
    this.loginService.fullName.subscribe( value => this.userName = value.toUpperCase());
    this.loadCompanyData();
  }

  imprimir(){
    window.print();
  }

  private loadCompanyData(): void{
    this.catalogService.getByName(CAT_CONST.DATA_COMPANY).subscribe( response => {
      if(response && response.items){
        response.items.forEach( (value, index) => {
          switch(value.name){
            case 'NOMBRE':
              this.company.name = value.description;
              break;
            case 'DIRECCION':
              this.company.address = value.description;
              break;
            case 'TELEFONO':
              this.company.phone = value.description;
              break;
            case 'PAGINA':
              this.company.website = value.description;
              break;
            case 'EMAIL':
              this.company.email = value.description;
              break;
            case 'NOMBRE_FISCAL':
              this.company.bussinessName = value.description;
              break;
            case 'RFC':
              this.company.rfc = value.description;
              break;
            case 'MESSAGE_TICKET':
              this.company.messageTicket = value.description;
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
