import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {Observable} from "rxjs/index";
import { baseUrl } from '../../environments/environment';

const responseTypeArrayBuffer: any = {responseType: 'arraybuffer'};

@Injectable()
export class ApiDatabaseService {

  constructor(private http: HttpClient) { }
  context: string = baseUrl + '/database';

  restore(formData) {
    return this.http.post<any>(this.context + '/restore', formData, {
      reportProgress: true,
      observe: 'events'
    });
  }

  backup() : Observable<ArrayBuffer> {
    return this.http.get(this.context + '/backup', responseTypeArrayBuffer);
  }

}