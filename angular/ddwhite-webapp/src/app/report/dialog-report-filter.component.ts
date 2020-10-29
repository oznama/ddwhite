import { Component, OnInit, Inject } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {CatalogItem} from './../model/catalog.model';
import { ApiCatalogService, ApiReportService, CAT_CONST, exportFile, CSV_EXTENSION } from './../service/module.service';
import { MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'dialog-report-filter',
  templateUrl: 'dialog-report-filter.component.html',
})
export class ReportFilterDialogComponent implements OnInit {
  searchForm: FormGroup;
  
  reports: string[] = ['Compras', 'Ventas', 'Pagos'];
  reportSelected: string;
  catalogPayment: CatalogItem[];
  
  constructor(
    public dialogRef: MatDialogRef<ReportFilterDialogComponent>,
    private formBuilder: FormBuilder,
    private catalogService: ApiCatalogService,
    private reportService: ApiReportService) {
  }

  ngOnInit() {
    this.searchForm = this.formBuilder.group({
      report:[''],
      startDate: [],
      endDate: [],
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

  showPayments(): boolean {
    return this.reportSelected && this.reportSelected === 'Pagos';
  }

  search() {
    //this.dialogRef.close({ event: 'close', data: this.searchForm.value });
    const startDate = this.searchForm.controls.startDate.value;
    const endDate = this.searchForm.controls.endDate.value;
    const report = this.searchForm.controls.report.value;
    const payment = this.searchForm.controls.payment.value;
    switch (report) {
      case "Compras":
        this.reportService.getPurchasesCSV(startDate, endDate).subscribe(data => exportFile(data, 'compras_', CSV_EXTENSION));
        break;
      case "Ventas":
        this.reportService.getSalesCSV(startDate, endDate).subscribe(data => exportFile(data, 'ventas_', CSV_EXTENSION));
        break;
      case "Pagos":
        this.reportService.payments(payment, startDate, endDate).subscribe(data => exportFile(data, 'pagos_', CSV_EXTENSION));
          break;
      default:
        this.reportService.getGeneralCSV(startDate, endDate).subscribe(data => exportFile(data, 'general_', CSV_EXTENSION));
        break;
    }
  }

  print(){
    const userId = +window.localStorage.getItem("userId");
    const startDate = this.searchForm.controls.startDate.value;
    const endDate = this.searchForm.controls.endDate.value;
    this.reportService.printGeneral(userId, startDate, endDate).subscribe(data => {}, error => console.log(error));
  }

}