import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { SingupComponent } from './singup/singup.component';

const routes: Routes = [
	{
		path: 'home',
		component: HomeComponent
	},
	{
		path: 'signup',
		component: SingupComponent
	}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
