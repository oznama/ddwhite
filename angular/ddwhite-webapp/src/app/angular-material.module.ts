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
MatNativeDateModule
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
   MatNativeDateModule
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
   MatNativeDateModule
],
providers: []
})

export class AngularMaterialModule { }