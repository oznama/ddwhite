import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from "rxjs";

@Injectable()
export class LoaderService {

  private loading = new BehaviorSubject<boolean>(false);
  
  show(){
    this.loading.next(true);
  }

  hide(){
    this.loading.next(false);
  }

  getLoading(): Observable<boolean> {
  	return this.loading.asObservable();
  }
}