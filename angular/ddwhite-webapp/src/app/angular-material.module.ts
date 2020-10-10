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
MatCheckboxModule,
MatCardModule,
MatProgressBarModule
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
   MatCheckboxModule,
   MatCardModule,
   MatProgressBarModule
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
   MatCheckboxModule,
   MatCardModule,
   MatProgressBarModule
],
providers: []
})

export class AngularMaterialModule { }