import { Component, OnInit, Inject } from '@angular/core';
import {Router} from "@angular/router";
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import {MatDialog} from '@angular/material/dialog';
import { Observable, of } from 'rxjs/index';
import {first} from "rxjs/operators";
import { AlertService, alertOptions } from './../../_alert';
import { Sale } from './../../model/sale.model';
import { ApiSaleService, pageSize } from "../../service/module.service";
import { ClientDialogSearchComponent } from '../../client/dialog-search/client-dialog-search.component';
import { ClientAddComponent } from '../../client/client-add/client-add.component';

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
    public alertService:AlertService,
    public dialog: MatDialog) {
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

  rowValid(id: number, invoice: string, clientId: number): boolean{
    const finded = this.primSales.find( e => e.id === id && invoice !== '' && (e.invoice !== invoice || e.clientId !== clientId));
    if(finded) return true;
    else return false;
  }

  private updateTable(id: number, invoice: string): void{
    this.primSales.find( e => { if(e.id === id) e.invoice = invoice } );
  }

  update(id: number, invoice: string, clientId: number){
    var body = new Sale();
    body.id = id;
    body.invoice = invoice;
    if( clientId ) body.clientId = clientId;
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

  openDialogClientSearch(saleId: number) {
    const dialogRef = this.dialog.open(ClientDialogSearchComponent);
    dialogRef.afterClosed().subscribe( result => this.setClient(saleId, result));
  }

  private setClient(saleId: number, result: any): void {
    if( result && result.data ){
      this.primSales.find( e => { 
        if(e.id === saleId) {
          e.clientId = result.data.id;
          e.clientName = result.data.name + ' ' + result.data.midleName + ' ' + result.data.lastName;
          e.clientRfc = result.data.rfc;
        }
      });
      this.sales = of(this.primSales);
    }
  }

  openDialogClientAdd() {
    const dialogRef = this.dialog.open(ClientAddComponent);
    dialogRef.afterClosed().subscribe(result => console.log());
  }

}