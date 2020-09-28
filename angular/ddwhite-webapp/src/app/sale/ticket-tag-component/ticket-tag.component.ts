import { Component, OnInit, Inject } from '@angular/core';
import { Sale, SalePayment } from '../../model/sale.model';
import {CatalogItem} from '../../model/catalog.model';
import { Company } from '../../model/company.model';
import { ApiCatalogService } from '../../service/module.service';

@Component({
  selector: 'sale-ticket-tag',
  templateUrl: './ticket-tag.component.html',
  styleUrls: ['./ticket-tag.component.css']
})
export class TicketTagComponent implements OnInit {

  date: Date = new Date();
  company: Company = new Company();
  userName: string;
  public data: Sale;

  constructor(private catalogService: ApiCatalogService) { }

  ngOnInit(): void {
    this.loadCompanyData();
    this.userName = window.localStorage.getItem('userFullName');
    this.data = JSON.parse(window.localStorage.getItem('currentSale'));
    localStorage.removeItem('currentSale');
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
