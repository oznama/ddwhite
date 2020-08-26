import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import {
MatButtonModule,
MatToolbarModule,
MatIconModule,
MatFormFieldModule,
MatInputModule,
MatMenuModule,
MatDialogModule
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
   MatDialogModule
],
exports: [
   MatButtonModule,
   MatToolbarModule,
   MatIconModule,
   MatInputModule,
   MatFormFieldModule,
   MatMenuModule,
   MatDialogModule
],
providers: []
})

export class AngularMaterialModule { }