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
MatSelectModule,
MatRadioModule,
MatDatepickerModule,
MatNativeDateModule,
MatTooltipModule,
MatMenuModule,
MatAutocompleteModule
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
   MatSelectModule,
   MatRadioModule,
   MatDatepickerModule,
   MatNativeDateModule,
   MatTooltipModule,
   MatMenuModule,
   MatAutocompleteModule
],
exports: [
   MatButtonModule,
   MatToolbarModule,
   MatIconModule,
   MatListModule,
   MatGridListModule,
   MatInputModule,
   MatFormFieldModule,
   MatSelectModule,
   MatRadioModule,
   MatDatepickerModule,
   MatTooltipModule,
   MatMenuModule,
   MatAutocompleteModule
],
providers: [
   MatDatepickerModule,
]
})

export class AngularMaterialModule { }