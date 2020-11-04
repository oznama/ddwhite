import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {Observable} from "rxjs/index";
import { baseUrl, observeResponse } from './../../environments/environment';
import {ApiResponse} from "../model/api.response";
import {Catalog, CatalogItem} from "../model/catalog.model";

@Injectable()
export class ApiCatalogService {

  constructor(private http: HttpClient) { }
  context: string = baseUrl + '/catalog';

  get() : Observable<Catalog[]> {
    return this.http.get<Catalog[]>(this.context + '/find');
  }

  getById(id: number): Observable<Catalog> {
    return this.http.get<Catalog>(this.context + '/find/' + id);
  }

  getByName(name: string): Observable<Catalog> {
    return this.http.get<Catalog>(this.context + '/findByName/' + name);
  }

  updateItems(catalogId: number, items: CatalogItem[]): Observable<any> {
    return this.http.put<any>(this.context + '/updateItems/' + catalogId, items, observeResponse);
  }

}

export const CAT_CONST = {
  CONST: 'CONST',
  DATA_COMPANY: 'COMPANY',
  DATA_COMPANY_NAME: 'NOMBRE',
  DATA_COMPANY_ADDRESS: 'DIRECCION',
  DATA_COMPANY_PHONE: 'TELEFONO',
  DATA_COMPANY_WEBSITE: 'PAGINA',
  DATA_COMPANY_EMAIL: 'EMAIL',
  DATA_COMPANY_BUSINESS_NAME: 'NOMBRE_FISCAL',
  DATA_COMPANY_RFC: 'RFC',
  DATA_COMPANY_MESSAGE: 'MESSAGE_TICKET',
  PRODUCT_GROUP: 'GRUPOPROD',
  UNITIES: 'UNIDADES',
  PAYMENT_METHOD: 'METODPAG',
  PIN_PAD: 'PINPAD',
  AMOUNT_MIN_BOX: 'MONTO_MIN_CAJA',
  TAX:'IVA',
  DISCOUNT_ENABLED: 'DESCUENTO_ACTIVO',
  DISCOUNT: 'DESCUENTO',
  DENOMINATION: 'DENOMINACION',
  UNITY_BOX: 'CAJA',
  UNITY_PZA: 'PZA',
  PAYMENT_METHOD_CASH: 'EFECTIVO',
  PAYMENT_METHOD_COM: 'COMISION'
}