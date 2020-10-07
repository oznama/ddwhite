import { Component, OnInit, Inject } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Product } from './../../model/product.model';

@Component({
  selector: 'discount-dialog',
  templateUrl: 'discount-dialog.component.html',
})
export class DiscountDialogComponent implements OnInit {
  discountForm: FormGroup;
  amountDiscounted: number;
  amount: number;
  
  constructor(
    public dialogRef: MatDialogRef<DiscountDialogComponent>, 
    @Inject(MAT_DIALOG_DATA) public data: Product,
    private formBuilder: FormBuilder) {
  }

  ngOnInit() {
    this.discountForm = this.formBuilder.group({
      percentage: [0, [Validators.required,Validators.pattern("[0-9]{0,3}")]]
    });
    this.amount = this.data.inventory.price;
    this.amountDiscounted = this.amount;
  }

  onChangePercentage(value: number){
    let discount = value/100*this.amount;
    let newPrice = this.amount - discount;
    this.amountDiscounted = +newPrice.toFixed();
  }

  applyDiscount(){
    this.dialogRef.close({ event: 'close', data: this.amountDiscounted });
  }


}