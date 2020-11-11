import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { ApiLoginService, ApiReportService, ApiSessionService, ApiSaleService, 
  Privileges, exportFile, CSV_EXTENSION } from './../service/module.service';
import {MatDialog} from '@angular/material/dialog';
import { ReportFilterDialogComponent } from '../report/dialog-report-filter.component';
import { ReprintTicketDialogComponent } from '../report/reprint-ticket-component/reprint-ticket.component';
//import { CashoutComponent } from '../report/cashout-component/cashout.component';
import { CashoutTicketComponent } from '../report/cashout-ticket-component/cashout-ticket.component';
import { WithdrawallDialogComponent } from '../sale/withdrawall-dialog-component/withdrawall-dialog.component';
import { AlertService, alertOptions } from '../_alert';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  isLoggedIn$: Observable<boolean>;
  userFullName$: Observable<string>;

  constructor(
    private apiService: ApiLoginService, 
    private reportService: ApiReportService,
    private sessionService: ApiSessionService,
    private saleService: ApiSaleService,
    public privileges: Privileges,
    public dialog: MatDialog,
    public alertService:AlertService) { }

  ngOnInit() {
    this.isLoggedIn$ = this.apiService.isLoggedIn;
    this.userFullName$ = this.apiService.fullName;
  }

  onLogout(){
    //this.saleService.getChasInRegister(+window.localStorage.getItem("userId")).subscribe( response => {
    this.saleService.getExcedent(+window.localStorage.getItem("userId")).subscribe( response => {
      if(response && response > 0) {
        //this.alertService.warn('Aun cuentas con $'+ response +' peso(s) de efectivo en caja', alertOptions)
        this.alertService.warn('Cuentas con efectivo excedente en caja', alertOptions)
      } else {
        const dialogRef = this.dialog.open(CashoutTicketComponent, { data: false, disableClose: !this.privileges.closeDialogs() });
        dialogRef.afterClosed().subscribe(result => {
          if(result) this.closeSession(result.data);
          else this.apiService.logout();
        });
      }
    });
  }

  private closeSession(finalAmmount: number){
    this.sessionService.getCurrentSession(+window.localStorage.getItem("userId")).subscribe(data => {
      if (data && data.id) {
        this.sessionService.close(data.id, finalAmmount).subscribe(
          data => this.apiService.logout(), 
          error => this.alertService.error('No se ha podido cerrar sesion, error: ' + error.error, alertOptions));
      } else {
        if( this.privileges.isAdmin() ) this.apiService.logout();
      }
    },error => this.alertService.error('Error al recuperar sesion, error: ' + error.message, alertOptions));
  }

  getWarehouseCSV(){
    this.reportService.getWarehouseCSV('sku').subscribe(data => exportFile(data, 'almacen_', CSV_EXTENSION));
  }

  openDialogReport() {
    this.dialog.open(ReportFilterDialogComponent);
  }

  actionSale(mode: string){
    this.dialog.open(ReprintTicketDialogComponent, { data: { mode: mode} });
  }

  makeCashout() {
    /*
    const dialogRef = this.dialog.open(CashoutComponent);
    dialogRef.afterClosed().subscribe(result =>{
      window.localStorage.removeItem('sessionStart');
      window.localStorage.setItem('sessionStart', (new Date()).toString());
    });
    */
    this.dialog.open(CashoutTicketComponent, {data: true});
  }

  checkWithdrall() {
    this.saleService.getExcedent(+window.localStorage.getItem("userId")).subscribe( response => {
      if(response && response > 0) {
        const dialogRef = this.dialog.open(WithdrawallDialogComponent, {data: response});
      } else {
        this.alertService.warn('No cuentas con efectivo excedente en caja', alertOptions)
      }
    });
  }

}