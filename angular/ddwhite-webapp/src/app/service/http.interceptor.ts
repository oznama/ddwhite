import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpEvent, HttpResponse, HttpRequest, HttpHandler } from '@angular/common/http';
import { Observable } from 'rxjs';
import { LoadingScreenService } from "./loading-screen.service";
import { finalize } from "rxjs/operators";

@Injectable()
export class CustomInterceptor implements HttpInterceptor {

	/*
	 * Reference
	 * https://ultimatecourses.com/blog/intro-to-angular-http-interceptors#:~:text=Interceptors%20are%20a%20unique%20type,the%20value%20of%20the%20request.
	 */

	constructor(private loadingScreenService: LoadingScreenService) {}

	intercept(httpRequest: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
		this.loadingScreenService.startLoading();
		return next.handle(httpRequest).pipe(
			finalize( () => this.loadingScreenService.stopLoading())
		);
	}
}