import { Component, OnInit, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import {CatalogItem} from '../../model/catalog.model';
import {Withdrawall} from '../../model/cashout.model';
import { ApiReportService, ApiCatalogService, Privileges, CAT_CONST } from '../../service/module.service';
import { AlertService, alertOptions } from '../../_alert';

@Component({
  selector: 'withdrawall-dialog',
  templateUrl: 'withdrawall-dialog.component.html'
})
export class WithdrawallDialogComponent implements OnInit {
  excedent: number;
  captured: number = 0;
  catalogDenominations: CatalogItem[];
  denominations: Withdrawall[] = [];
  private decimals: number = 2;
  
  constructor(
    public dialogRef: MatDialogRef<WithdrawallDialogComponent>, 
    @Inject(MAT_DIALOG_DATA) public data: number,
    private catalogService: ApiCatalogService,
    private reportService: ApiReportService,
    public alertService:AlertService,
    public privileges:Privileges) {
  }

  ngOnInit() {
    this.excedent = this.data;
    this.loadDiscounts();
  }

  private loadDiscounts(): void{
    this.catalogService.getByName(CAT_CONST.DENOMINATION).subscribe(
      response => {
        this.catalogDenominations = response.items;
        this.catalogDenominations.forEach(cD => {
          const denomination = <Withdrawall> {
            denominationId: cD.id,
            denomination: cD.description,
            denominationValue: +cD.name,
            quantity: 0
          };
          this.denominations.push(denomination);
        })
      }, error => console.error(error));
  }

  isDisabledButton(){
    return this.privileges.isAdmin() ? (this.captured <= 0 || this.captured > this.excedent) : this.captured != this.excedent;
  }

  add(denomination: string){
    this.denominations.find( d => {
      if(d.denomination === denomination){
        d.quantity = d.quantity+1;
        this.captured += d.denominationValue;
        this.captured = Number(this.captured.toFixed(this.decimals));
      }
    });
  }

  substract(denomination: string){
    this.denominations.find( d => {
      if(d.denomination === denomination && d.quantity > 0){
        d.quantity = d.quantity-1;
        this.captured -= d.denominationValue;
        this.captured = Number(this.captured.toFixed(this.decimals));
      }
    });
  }

  getQuantity(denomination: string): number {
      const d = this.denominations.find( item => item.denomination === denomination);
      return d.quantity;
  }

  withdrawall(){
    this.reportService.withdrawall(+window.localStorage.getItem("userId"), this.denominations.filter( d => d.quantity > 0)).subscribe(
      response => this.dialogRef.close({ event: 'close' }),
      error => this.alertService.error('Error generando retiro, error:' + error.error, alertOptions)
    );
  }


}