import { Component, OnInit, Inject } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {CatalogItem} from './../model/catalog.model';
import { ApiCatalogService, CAT_CONST } from './../service/api.service.catalog';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
  selector: 'dialog-report-filter',
  templateUrl: 'dialog-report-filter.component.html',
})
export class ReportFilterDialogComponent implements OnInit {
  searchForm: FormGroup;
  
  reports: string[] = ['Compras', 'Ventas', 'Reimpresion Ticket', 'Pagos'];
  reportSelected: string;
  catalogPayment: CatalogItem[];
  
  constructor(
    public dialogRef: MatDialogRef<ReportFilterDialogComponent>, 
    @Inject(MAT_DIALOG_DATA) public data: string,
    private formBuilder: FormBuilder,
    private catalogService: ApiCatalogService,) {
  }

  ngOnInit() {
    this.searchForm = this.formBuilder.group({
      report:[''],
      startDate: [],
      endDate: [],
      saleId: [0],
      payment: [],
    });
    this.loadCatalogPayment();
    this.searchForm.controls.startDate.disable;
    this.searchForm.controls.endDate.disable;
  }

  private loadCatalogPayment(): void{
    this.catalogService.getByName(CAT_CONST.PAYMENT_METHOD).subscribe( response => {
      this.catalogPayment = response.items;
      this.searchForm.controls.payment.setValue(this.catalogPayment[0].id);
    }, error =>{
      console.error(error);
    });
  }

  onChange(value: string){
    this.reportSelected = value;
  }

  showDates(): boolean {
    return this.reportSelected && this.reportSelected !== 'Reimpresion Ticket';
  }

  showTicketId(): boolean {
    return this.reportSelected && this.reportSelected === 'Reimpresion Ticket';
  }

  showPayments(): boolean {
    return this.reportSelected && this.reportSelected === 'Pagos';
  }

  search() {
    this.dialogRef.close({ event: 'close', data: this.searchForm.value });
  }

}