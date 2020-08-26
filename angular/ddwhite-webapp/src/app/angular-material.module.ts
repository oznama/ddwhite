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
MatProgressSpinnerModule
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
   MatProgressSpinnerModule
],
exports: [
   MatButtonModule,
   MatToolbarModule,
   MatIconModule,
   MatInputModule,
   MatFormFieldModule,
   MatMenuModule,
   MatDialogModule,
   MatProgressSpinnerModule
],
providers: []
})

export class AngularMaterialModule { }