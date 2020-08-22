import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import {
MatButtonModule,
MatToolbarModule,
MatIconModule,
MatListModule,
MatGridListModule,
MatFormFieldModule,
MatInputModule,
MatRadioModule,
MatDatepickerModule,
MatNativeDateModule,
MatTooltipModule,
MatMenuModule,
MatAutocompleteModule,
MatDialogModule
} from '@angular/material';

@NgModule({
imports: [
   CommonModule,
   MatButtonModule,
   MatToolbarModule,
   MatIconModule,
   MatListModule,
   MatGridListModule,
   MatFormFieldModule,
   MatInputModule,
   MatRadioModule,
   MatDatepickerModule,
   MatNativeDateModule,
   MatTooltipModule,
   MatMenuModule,
   MatAutocompleteModule,
   MatDialogModule
],
exports: [
   MatButtonModule,
   MatToolbarModule,
   MatIconModule,
   MatListModule,
   MatGridListModule,
   MatInputModule,
   MatFormFieldModule,
   MatRadioModule,
   MatDatepickerModule,
   MatTooltipModule,
   MatMenuModule,
   MatAutocompleteModule,
   MatDialogModule
],
providers: [
   MatDatepickerModule,
]
})

export class AngularMaterialModule { }