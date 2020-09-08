import { Component, OnInit, Inject } from '@angular/core';
import {Router} from "@angular/router";
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Observable, of } from 'rxjs/index';
import {first} from "rxjs/operators";
import { AlertService, alertOptions } from './../../_alert';
import { Sale } from './../../model/sale.model';
import { ApiSaleService, pageSize } from "../../service/module.service";

@Component({
  selector: 'invoice-sale',
  templateUrl: 'invoice.component.html',
  styleUrls: ['./invoice.component.css']
})
export class InvoiceSaleComponent implements OnInit {
  searchForm: FormGroup;

  primSales: Sale[];
  sales: Observable<Sale[]>;
  page: number = 0;
  sort: string = 'id,asc';
  totalPage: number;
  
  constructor(private formBuilder: FormBuilder,
    private router: Router, 
    private apiService: ApiSaleService,
    public alertService:AlertService) {
  }

  ngOnInit() {
    this.searchForm = this.formBuilder.group({
      startDate: ['', Validators.required],
      endDate: ['', Validators.required],
    });
    this.searchForm.controls.startDate.disable;
    this.searchForm.controls.endDate.disable;
  }

  search(page: number) {
    this.page = page;
    this.apiService.get(this.searchForm.controls.startDate.value, this.searchForm.controls.endDate.value, this.page, pageSize, this.sort)
    .subscribe( data => {
      if( data.content.length > 0 ){
        this.totalPage = data.totalPages;
        this.primSales = data.content;
        this.sales = of(data.content);
      } else{
        this.page = 0;
        this.totalPage = 0;
        this.sales = of([]);
        this.primSales = [];
        this.alertService.warn('No hay ventas en el periodo selecionado', alertOptions);
      }
    }, error => {
      this.alertService.error(error, alertOptions);
    });
  }

  pagination(page:number): void {
    if( page >= 0 && page < this.totalPage && page != this.page ) {
      this.page = page;
      this.search(this.page);
    }
  }

  rowValid(id: number, invoice: string): boolean{
    const finded = this.primSales.find( e => e.id === id && invoice !== '' && e.invoice !== invoice );
    if(finded) return true;
    else return false;
  }

  private updateTable(id: number, invoice: string): void{
    this.primSales.find( e => { if(e.id === id) e.invoice = invoice } );
  }

  update(id: number, invoice: string){
    var body = new Sale();
    body.id = id;
    body.invoice = invoice;
    this.apiService.update(body).pipe(first()).subscribe(
        data => {
          if(data.status === 200) {
            this.updateTable(id, invoice);
            this.alertService.success('Folio de factura actualizada', alertOptions);
          }else {
            this.alertService.error(data.message, alertOptions);
          }
        },
        error => {
          this.alertService.error('El registro no ha sido actualizado: ' + error.error, alertOptions);
        }
    );
  }

}