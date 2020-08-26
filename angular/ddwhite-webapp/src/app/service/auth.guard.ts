import { Injectable } from  '@angular/core';
import { Router, CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { Observable } from 'rxjs';
import { map, take } from 'rxjs/operators';
import { ApiUserService } from "./../service/api.service.user";

@Injectable()
export class AuthGuard implements CanActivate {

	constructor(
		private userService: ApiUserService,
		private router: Router
	) {}

	canActivate(next: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> {
		return this.userService.isLoggedIn.pipe(take(1),
			map((isLoggedIn: boolean) => {
				if (isLoggedIn) {
					return true
				}
				window.localStorage.removeItem('userId');
				this.router.navigate(['/login']);
				return false;
			})
	    );
	}

}