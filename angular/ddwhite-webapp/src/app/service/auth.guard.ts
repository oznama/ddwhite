import { Injectable } from  '@angular/core';
import { Router, CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { Observable } from 'rxjs';
import { map, take } from 'rxjs/operators';
import { ApiLoginService } from "./../service/api.service.login";

@Injectable()
export class AuthGuard implements CanActivate {

	constructor(
		private apiService: ApiLoginService,
		private router: Router
	) {}

	canActivate(next: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> {
		return this.apiService.isLoggedIn.pipe(take(1),
			map((isLoggedIn: boolean) => {
				if (isLoggedIn) {
					return true;
				}
				//window.localStorage.removeItem('sessionStart');
				window.localStorage.removeItem('userFullName');
				window.localStorage.removeItem('userId');
				this.router.navigate(['/login']);
				return false;
			})
	    );
	}

}