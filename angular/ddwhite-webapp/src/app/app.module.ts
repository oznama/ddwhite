import { BrowserModule } from '@angular/platform-browser';
import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';

import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { AngularMaterialModule } from './angular-material.module';
import { FlexLayoutModule } from '@angular/flex-layout';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { HttpClientModule } from '@angular/common/http';
import { ApiCatalogService, ApiProviderService, ApiUserService, ApiProductService, ApiPurchaseService } from './service/api.service';
import { AlertModule } from './_alert';
import { AuthGuard } from './login/auth.guard';

import { HomeComponent } from './home/home.component';
import { HeaderComponent } from './header/header.component';
import { LoginComponent } from './login/login.component';
import { ProviderListComponent } from './provider/provider-list/provider-list.component';
import { ProviderAddComponent } from './provider/provider-add/provider-add.component';
import { ProviderEditComponent } from './provider/provider-edit/provider-edit.component';
import { ProductEditComponent } from './product/product-edit/product-edit.component';
import { ProductAddComponent } from './product/product-add/product-add.component';
import { ProductListComponent } from './product/product-list/product-list.component';
import { PurchaseComponent, DialogProviderSearch, DialogoProductSearch } from './purchase/purchase.component';

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    HeaderComponent,
    LoginComponent,
    ProviderListComponent,
    ProviderAddComponent,
    ProviderEditComponent,
    ProductEditComponent,
    ProductAddComponent,
    ProductListComponent,
    PurchaseComponent, DialogProviderSearch, DialogoProductSearch
  ],
  imports: [
    BrowserModule,
    AlertModule,
    AppRoutingModule,
    HttpClientModule,
    BrowserAnimationsModule,
    AngularMaterialModule,
    FlexLayoutModule,
    FormsModule,
    ReactiveFormsModule
  ],
  providers: [
    AuthGuard,
    ApiCatalogService,
    ApiUserService,
    ApiProviderService,
    ApiProductService,
    ApiPurchaseService
  ],
  bootstrap: [AppComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AppModule { }
