import { Component, OnInit, Inject } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ApiReportService, ApiSaleService, ab2str } from '../../service/module.service';
import { AlertService, alertOptions } from '../../_alert';
import { ModeSaleTicketDialog } from '../../model/sale.model';

@Component({
  selector: 'reprint-ticket',
  templateUrl: 'reprint-ticket.component.html',
})
export class ReprintTicketDialogComponent implements OnInit {
  searchForm: FormGroup;
  cardTitle: string;
  color: string;
  matIcon: string;
  
  constructor(public dialogRef: MatDialogRef<ReprintTicketDialogComponent>, 
    @Inject(MAT_DIALOG_DATA) public data: ModeSaleTicketDialog,
    private formBuilder: FormBuilder,
    private reportService: ApiReportService,
    private saleService: ApiSaleService,
    public alertService:AlertService) {
  }

  ngOnInit() {
    this.searchForm = this.formBuilder.group({
      saleId: [, Validators.required]
    });
    if(this.data.mode === 'reprint') {
      this.cardTitle = 'Reempresion de Ticket';
      this.color = 'primary';
      this.matIcon = 'receipt'
    }else if (this.data.mode === 'cancel'){
      this.cardTitle = 'Cancelación de venta';
      this.color = 'warn';
      this.matIcon = 'clear'
    }
  }

  onSubmit(){
    if(this.data.mode === 'reprint') this.reprint();
    else if (this.data.mode === 'cancel') this.cancel();
  }

  private reprint() {
    this.reportService.reprintTicket(+this.searchForm.controls.saleId.value).subscribe(
      data => {}, error => this.alertService.error(ab2str(error.error), alertOptions));
  }

  private cancel() {
    let saleId = +this.searchForm.controls.saleId.value;
    this.saleService.delete(saleId).subscribe(
      data => this.alertService.info('Venta con folio '+ saleId +' cancelada satisfactoriamente', alertOptions), 
      error => this.alertService.error(error.error, alertOptions));
  }

}