import { Component, OnInit, Inject } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import {CatalogItem} from '../../model/catalog.model';
import { ApiCatalogService, CAT_CONST } from '../../service/module.service';

@Component({
  selector: 'discount-dialog',
  templateUrl: 'discount-dialog.component.html',
  styleUrls: ['./discount-dialog.component.css']
})
export class DiscountDialogComponent implements OnInit {
  discountForm: FormGroup;
  amountDiscounted: number;
  amount: number;
  discount: number
  catalogDiscounts: CatalogItem[];
  
  constructor(
    public dialogRef: MatDialogRef<DiscountDialogComponent>, 
    @Inject(MAT_DIALOG_DATA) public data: number,
    private formBuilder: FormBuilder,
    private catalogService: ApiCatalogService, ) {
  }

  ngOnInit() {
    this.discountForm = this.formBuilder.group({
      percentage: [0, [Validators.required,Validators.pattern("[0-9]{0,3}")]]
    });
    this.amount = this.data;
    this.amountDiscounted = this.amount;
    this.loadDiscounts();
  }

  private loadDiscounts(): void{
    this.catalogService.getByName(CAT_CONST.DISCOUNT).subscribe(
      response => this.catalogDiscounts = response.items, 
      error => console.error(error));
  }

  onChangePercentage(value: number){
    this.discount = value;
    let discount = this.discount/100*this.amount;
    let newPrice = this.amount - discount;
    this.amountDiscounted = +newPrice.toFixed();
  }

  onButtonClicked(value: number){
    this.onChangePercentage(value);
    this.dialogRef.close({ event: 'close', data: { 'amountDiscounted': this.amountDiscounted, 'discount': this.discount} });
  }


}