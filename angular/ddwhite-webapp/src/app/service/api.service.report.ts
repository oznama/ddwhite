import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {Observable} from "rxjs/index";
import { baseUrl, observeResponse } from './../../environments/environment';

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

}