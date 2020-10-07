import {Component, Inject} from '@angular/core';
import {MatDialog, MatDialogRef, MAT_DIALOG_DATA} from '@angular/material/dialog';

export interface DialogData {
  currentCost: number;
  newCost: number;
}

@Component({
  selector: 'purchase-dialog-cost-component',
  templateUrl: 'purchase-dialog-cost-component.html',
})
export class PurchaseDialogCostComponent {

  constructor(
    public dialogRef: MatDialogRef<PurchaseDialogCostComponent>,
    @Inject(MAT_DIALOG_DATA) public data: DialogData) {}

  onNoClick(): void {
    this.dialogRef.close();
  }

}
