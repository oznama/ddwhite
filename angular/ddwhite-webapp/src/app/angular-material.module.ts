import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import {
MatButtonModule,
MatToolbarModule,
MatIconModule,
MatFormFieldModule,
MatInputModule,
MatMenuModule,
MatDialogModule,
MatProgressSpinnerModule,
MatDatepickerModule,
MatNativeDateModule,
MatCheckboxModule
} from '@angular/material';

@NgModule({
imports: [
   CommonModule,
   MatButtonModule,
   MatToolbarModule,
   MatIconModule,
   MatFormFieldModule,
   MatInputModule,
   MatMenuModule,
   MatDialogModule,
   MatProgressSpinnerModule,
   MatDatepickerModule,
   MatNativeDateModule,
   MatCheckboxModule
],
exports: [
   MatButtonModule,
   MatToolbarModule,
   MatIconModule,
   MatInputModule,
   MatFormFieldModule,
   MatMenuModule,
   MatDialogModule,
   MatProgressSpinnerModule,
   MatDatepickerModule,
   MatNativeDateModule,
   MatCheckboxModule
],
providers: []
})

export class AngularMaterialModule { }