import { NgModule } from '@angular/core';
import { Routes, RouterModule, CanActivate } from '@angular/router';
import { AuthGuard } from './service/module.service';
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
import { UserListComponent } from './user/user-list/user-list.component';
import { UserAddComponent } from './user/user-add/user-add.component';
import { UserEditComponent } from './user/user-edit/user-edit.component';
import { ExpenseComponent } from './expense/expense.component';

const routes: Routes = [
	{ path: '', pathMatch: 'full', redirectTo: 'login'},
	{ path: 'login', component: LoginComponent },
	{ path: 'home', component: HomeComponent, canActivate: [AuthGuard] },
	{ path: 'provider-list', component: ProviderListComponent, canActivate: [AuthGuard] },
	{ path: 'provider-add', component: ProviderAddComponent, canActivate: [AuthGuard] },
	{ path: 'provider-edit', component: ProviderEditComponent, canActivate: [AuthGuard] },
	{ path: 'product-list', component: ProductListComponent, canActivate: [AuthGuard] },
	{ path: 'product-add', component: ProductAddComponent, canActivate: [AuthGuard] },
	{ path: 'product-edit', component: ProductEditComponent, canActivate: [AuthGuard] },
	{ path: 'purchases', component: PurchaseComponent, canActivate: [AuthGuard] },
	{ path: 'sales', component: SaleComponent, canActivate: [AuthGuard]},
	{ path: 'client-edit', component: ClientEditComponent, canActivate: [AuthGuard]},
	{ path: 'client-list', component: ClientListComponent, canActivate: [AuthGuard]},
	{ path: 'user-list', component: UserListComponent, canActivate: [AuthGuard] },
	{ path: 'user-add', component: UserAddComponent, canActivate: [AuthGuard] },
	{ path: 'user-edit', component: UserEditComponent, canActivate: [AuthGuard] },
	{ path: 'expenses', component: ExpenseComponent, canActivate: [AuthGuard] },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
