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
  
  constructor(
    public dialogRef: MatDialogRef<DiscountDialogComponent>, 
    @Inject(MAT_DIALOG_DATA) public data: Product,
    private formBuilder: FormBuilder) {
  }

  ngOnInit() {
    this.discountForm = this.formBuilder.group({
      percentage: [, [Validators.required,Validators.pattern("[0-9]{0,6}")]]
    });
  }

  applyDiscount(){
    let percentage = +this.discountForm.controls.percentage.value;
    this.dialogRef.close({ event: 'close', data: percentage });
  }


}