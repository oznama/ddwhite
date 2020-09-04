import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpEvent, HttpRequest, HttpHandler } from '@angular/common/http';
import { Observable } from 'rxjs';
import { LoaderService } from "./loader.service";
import { finalize } from "rxjs/operators";

@Injectable()
export class LoaderInterceptor implements HttpInterceptor {

	/*
	 * Reference
	 * https://ultimatecourses.com/blog/intro-to-angular-http-interceptors#:~:text=Interceptors%20are%20a%20unique%20type,the%20value%20of%20the%20request.
	 */

	constructor(private loadingService: LoaderService) {}

	intercept(httpRequest: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {

		this.loadingService.show();

		return next.handle(httpRequest).pipe(
			finalize( () => {
				this.loadingService.hide();
			})
		);
	}
}