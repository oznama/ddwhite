import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { ApiLoginService, ApiReportService, Privileges } from './../service/module.service';
import {MatDialog} from '@angular/material/dialog';
import { ReportFilterDialogComponent } from '../report/dialog-report-filter.component';
import { CashoutComponent } from '../report/cashout-component/cashout.component';
import { saveAs } from 'file-saver';

const CSV_EXTENSION = '.csv';

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
    public privileges: Privileges,
    public dialog: MatDialog) { }

  ngOnInit() {
    this.isLoggedIn$ = this.apiService.isLoggedIn;
    this.userFullName$ = this.apiService.fullName;
  }

  onLogout(){
    this.apiService.logout();
  }

  getWarehouseCSV(){
    this.reportService.getWarehouseCSV('sku').subscribe(data => this.exportFile(data, 'almacen_'));
  }

  openDialogProductSearch() {
    const dialogRef = this.dialog.open(ReportFilterDialogComponent, { data: 'datefilter' });
    dialogRef.afterClosed().subscribe( result =>{
      if(result && result.data){
        const startDate = result.data.startDate;
        const endDate = result.data.endDate;
        this.reportService.getGeneralCSV(startDate, endDate).subscribe(data => this.exportFile(data, 'general_'));
      }
    });
  }

  makeCashout() {
    /*
    const dialogRef = this.dialog.open(CashoutComponent);
    dialogRef.afterClosed().subscribe(result =>{
      window.localStorage.removeItem('sessionStart');
      window.localStorage.setItem('sessionStart', (new Date()).toString());
    });
    */
    const userId = +window.localStorage.getItem('userId');
    const startDate = new Date(window.localStorage.getItem('sessionStart'));
    this.reportService.printCashout(userId, startDate, new Date()).subscribe(data => {
      console.log(data);
    }, error => {
      console.error(error);
    });
  }

  private exportFile(data: ArrayBuffer, name: string){
    const blob = new Blob([data], { type: 'application/octet-stream' });
    const now = new Date();
    const month = (now.getMonth()+1);
    var strMonth = (month < 10 ? '0' : '') + month;
    const strNow = now.getDate()+ strMonth +now.getFullYear()+'_'+now.getHours()+''+now.getMinutes()+''+now.getSeconds();
    const fileName = name + strNow + CSV_EXTENSION;
    saveAs(blob, fileName);
  }
}