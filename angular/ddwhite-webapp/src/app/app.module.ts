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
  ApiReportService,
  ApiCompanyService,
  ApiSessionService,
  ApiDatabaseService,
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
import { PurchaseDialogCostComponent } from './purchase/purchase-dialog-cost/purchase-dialog-cost-component';
import { SaleComponent } from './sale/sale.component';
import { PaymentDialogComponent } from './sale/payment-dialog-component/payment-dialog.component';
import { ClientAddComponent } from './client/client-add/client-add.component';
import { ClientEditComponent } from './client/client-edit/client-edit.component';
import { ClientListComponent } from './client/client-list/client-list.component';
import { ClientDialogSearchComponent } from './client/dialog-search/client-dialog-search.component';
import { UserAddComponent } from './user/user-add/user-add.component';
import { UserEditComponent } from './user/user-edit/user-edit.component';
import { UserListComponent } from './user/user-list/user-list.component';
import { ExpenseComponent } from './expense/expense.component';
import { TicketComponent } from './sale/ticket-component/ticket.component';
import { TicketTagComponent } from './sale/ticket-tag-component/ticket-tag.component';
import { ReportFilterDialogComponent } from './report/dialog-report-filter.component';
import { InvoiceSaleComponent } from './sale/invoice-component/invoice.component';
import { MycompanyComponent } from './mycompany/mycompany.component';
import { CashoutComponent } from './report/cashout-component/cashout.component';
import { CatalogComponent } from './catalog/catalog.component';
import { PurchaseReasignComponent } from './purchase/reasign/reasign.component';
import { PurchaseEditComponent } from './purchase/purchase-edit/purchase-edit.component';
import { PurchaseListComponent } from './purchase/purchase-list/purchase-list.component';
import { SessionComponent } from './login/session/session.component';
import { DiscountDialogComponent } from './sale/discount-dialog-component/discount-dialog.component';
import { ReprintTicketDialogComponent } from './report/reprint-ticket-component/reprint-ticket.component';
import { CashoutTicketComponent } from './report/cashout-ticket-component/cashout-ticket.component';
import { DatabaseComponent } from './management/database/database.component';
import { WithdrawallDialogComponent } from './sale/withdrawall-dialog-component/withdrawall-dialog.component';
import { SessionListComponent } from './login/session/session-list/session-list.component';
import { SessionEditComponent } from './login/session/session-edit/session-edit.component';

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
    TicketComponent,
    TicketTagComponent,
    ReportFilterDialogComponent,
    InvoiceSaleComponent,
    MycompanyComponent,
    CashoutComponent,
    CatalogComponent,
    PurchaseReasignComponent,
    PurchaseEditComponent,
    PurchaseListComponent,
    SessionComponent,
    DiscountDialogComponent,
    ReprintTicketDialogComponent,
    CashoutTicketComponent,
    DatabaseComponent,
    WithdrawallDialogComponent,
    SessionListComponent,
    SessionEditComponent
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
    ApiReportService,
    ApiCompanyService,
    ApiSessionService,
    ApiDatabaseService,
    Privileges
  ],
  bootstrap: [AppComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AppModule { }
