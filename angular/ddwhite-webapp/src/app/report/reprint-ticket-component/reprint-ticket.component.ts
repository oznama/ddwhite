import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ApiReportService, ab2str } from '../../service/module.service';
import { AlertService, alertOptions } from '../../_alert';

@Component({
  selector: 'reprint-ticket',
  templateUrl: 'reprint-ticket.component.html',
})
export class ReprintTicketDialogComponent implements OnInit {
  searchForm: FormGroup;
  
  constructor(
    public dialogRef: MatDialogRef<ReprintTicketDialogComponent>, 
    private formBuilder: FormBuilder,
    private reportService: ApiReportService,
    public alertService:AlertService) {
  }

  ngOnInit() {
    this.searchForm = this.formBuilder.group({
      saleId: [, Validators.required]
    });
  }

  reprint() {
    this.reportService.reprintTicket(+this.searchForm.controls.saleId.value).subscribe(
      data => {}, error => this.alertService.error(ab2str(error.error), alertOptions));
  }

}