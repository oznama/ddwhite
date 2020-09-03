import { BrowserModule } from '@angular/platform-browser';
import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';

import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { AngularMaterialModule } from './angular-material.module';
import { FlexLayoutModule } from '@angular/flex-layout';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { 
  AuthGuard,
  LoaderService,
  LoaderInterceptor,
  ApiCatalogService,
  ApiLoginService, 
  ApiProviderService, 
  ApiUserService, 
  ApiProductService, 
  ApiPurchaseService,
  ApiClientService,
  ApiSaleService,
  ApiExpenseService,
  ApiRoleService,
  Privileges
} from './service/module.service';
import { AlertModule } from './_alert';

import { HomeComponent } from './home/home.component';
import { LoaderComponent } from './loader/loader.component';
import { HeaderComponent } from './header/header.component';
import { LoginComponent } from './login/login.component';
import { ProviderListComponent } from './provider/provider-list/provider-list.component';
import { ProviderAddComponent } from './provider/provider-add/provider-add.component';
import { ProviderEditComponent } from './provider/provider-edit/provider-edit.component';
import { ProviderDialogSearchComponent } from './provider/dialog-search/provider-dialog-search.component';
import { ProductEditComponent } from './product/product-edit/product-edit.component';
import { ProductAddComponent } from './product/product-add/product-add.component';
import { ProductListComponent } from './product/product-list/product-list.component';
import { ProductDialogSearchComponent } from './product/dialog-search/product-dialog-search.component';
import { PurchaseComponent } from './purchase/purchase.component';
import { PurchaseDialogCostComponent } from './purchase/purchase-dialog-cost-component';
import { SaleComponent } from './sale/sale.component';
import { PaymentDialogComponent } from './sale/payment-dialog.component';
import { ClientAddComponent } from './client/client-add/client-add.component';
import { ClientEditComponent } from './client/client-edit/client-edit.component';
import { ClientListComponent } from './client/client-list/client-list.component';
import { ClientDialogSearchComponent } from './client/dialog-search/client-dialog-search.component';
import { UserAddComponent } from './user/user-add/user-add.component';
import { UserEditComponent } from './user/user-edit/user-edit.component';
import { UserListComponent } from './user/user-list/user-list.component';
import { ExpenseComponent } from './expense/expense.component';
import { TicketComponent } from './sale/ticket-component/ticket.component';

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    LoaderComponent,
    HeaderComponent,
    LoginComponent,
    ProviderListComponent,
    ProviderAddComponent,
    ProviderEditComponent,
    ProductEditComponent,
    ProductAddComponent,
    ProductListComponent,
    PurchaseComponent,
    ProviderDialogSearchComponent, 
    ProductDialogSearchComponent,
    PurchaseDialogCostComponent,
    SaleComponent,
    PaymentDialogComponent,
    ClientAddComponent,
    ClientEditComponent,
    ClientListComponent,
    ClientDialogSearchComponent,
    UserAddComponent,
    UserEditComponent,
    UserListComponent,
    ExpenseComponent,
    TicketComponent
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
    LoaderService,
    { provide: HTTP_INTERCEPTORS, useClass: LoaderInterceptor, multi: true},
    ApiCatalogService,
    ApiLoginService,
    ApiUserService,
    ApiProviderService,
    ApiProductService,
    ApiPurchaseService,
    ApiClientService,
    ApiSaleService,
    ApiExpenseService,
    ApiRoleService,
    Privileges
  ],
  bootstrap: [AppComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AppModule { }
