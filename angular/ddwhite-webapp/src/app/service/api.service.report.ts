import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {Observable} from "rxjs/index";
import { baseUrl, observeResponse } from '../../environments/environment';
import { Cashout } from '../model/cashout.model';

const responseTypeArrayBuffer: any = {responseType: 'arraybuffer'};

@Injectable()
export class ApiReportService {

  constructor(private http: HttpClient) { }
  context: string = baseUrl + '/report';

  getGeneralCSV(startDate: string, endDate: string) : Observable<ArrayBuffer> {
    return this.http.get(this.context + '/general/csv?startDate='+ startDate +'&endDate='+ endDate, responseTypeArrayBuffer);
  }

  getWarehouseCSV(sort: string) : Observable<ArrayBuffer> {
    return this.http.get(this.context + '/warehouse/csv?sort='+sort, responseTypeArrayBuffer);
  }

  getCashout(startDate: Date, endDate: Date) : Observable<Cashout> {
  	return this.http.get<Cashout>(this.context + '/cashout?startDate='+ startDate +'&endDate='+ endDate);
  }

  printCashout(userId: number) : Observable<Cashout> {
    return this.http.get<Cashout>(this.context + '/print/cashout?userId='+ userId);
  }

  getPurchasesCSV(startDate: string, endDate: string) : Observable<ArrayBuffer> {
    return this.http.get(this.context + '/purchases/csv?startDate='+ startDate +'&endDate='+ endDate, responseTypeArrayBuffer);
  }

  getSalesCSV(startDate: string, endDate: string) : Observable<ArrayBuffer> {
    return this.http.get(this.context + '/sales/csv?startDate='+ startDate +'&endDate='+ endDate, responseTypeArrayBuffer);
  }

  reprintTicket(saleId: number) : Observable<ArrayBuffer> {
    return this.http.get(this.context + '/reprint/ticket?saleId='+ saleId, responseTypeArrayBuffer);
  }

  payments(paymentId: number, startDate: string, endDate: string) : Observable<ArrayBuffer> {
    return this.http.get(this.context + '/payments?paymentId='+ paymentId 
      + (startDate ? '&startDate='+ startDate : '' ) + (endDate ? '&endDate='+ endDate : ''), responseTypeArrayBuffer);
  }

}