import { Component, OnInit, Inject } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
  selector: 'dialog-report-filter',
  templateUrl: 'dialog-report-filter.component.html',
})
export class ReportFilterDialogComponent implements OnInit {
  searchForm: FormGroup;
  
  reports: string[] = ['Compras', 'Ventas', 'Reimpresion Ticket'];
  reportSelected: string;
  
  constructor(
    public dialogRef: MatDialogRef<ReportFilterDialogComponent>, 
    @Inject(MAT_DIALOG_DATA) public data: string,
    private formBuilder: FormBuilder) {
  }

  ngOnInit() {
    this.searchForm = this.formBuilder.group({
      report:[''],
      startDate: [],
      endDate: [],
      saleId: [0]
    });
    this.searchForm.controls.startDate.disable;
    this.searchForm.controls.endDate.disable;
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

  search() {
    this.dialogRef.close({ event: 'close', data: this.searchForm.value });
  }

}