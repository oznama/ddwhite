import { NgModule } from '@angular/core';
import { Routes, RouterModule, CanActivate } from '@angular/router';
import { AuthGuard } from './login/auth.guard';
import { LoginComponent } from './login/login.component';
import { HomeComponent } from './home/home.component';
import { ProviderListComponent } from './provider/provider-list/provider-list.component';
import { ProviderAddComponent } from './provider/provider-add/provider-add.component';
import { ProviderEditComponent } from './provider/provider-edit/provider-edit.component';
import { ProductListComponent } from './product/product-list/product-list.component';
import { ProductAddComponent } from './product/product-add/product-add.component';
import { ProductEditComponent } from './product/product-edit/product-edit.component';
import { PurchaseComponent } from './purchase/purchase.component';
import { SaleComponent } from './sale/sale.component';
import { ClientEditComponent } from './client/client-edit/client-edit.component';
import { ClientListComponent } from './client/client-list/client-list.component';

const routes: Routes = [
	{ path: '', pathMatch: 'full', redirectTo: 'login'},
	//{ path: '', pathMatch: 'full', redirectTo: 'sales'},
	{ path: 'login', component: LoginComponent },
	{ path: 'home', component: HomeComponent, canActivate: [AuthGuard] },
	{ path: 'provider-list', component: ProviderListComponent },
	{ path: 'provider-add', component: ProviderAddComponent },
	{ path: 'provider-edit', component: ProviderEditComponent },
	{ path: 'product-list', component: ProductListComponent },
	{ path: 'product-add', component: ProductAddComponent },
	{ path: 'product-edit', component: ProductEditComponent },
	{ path: 'purchases', component: PurchaseComponent },
	{ path: 'sales', component: SaleComponent},
	{ path: 'client-edit', component: ClientEditComponent},
	{ path: 'client-list', component: ClientListComponent},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
