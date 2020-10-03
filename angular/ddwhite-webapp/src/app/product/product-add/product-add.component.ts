import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Router} from "@angular/router";
import { ApiProductService, ApiCatalogService } from "../../service/module.service";
import { AlertService, alertOptions } from '../../_alert';
import { Product } from '../../model/product.model';
import {CatalogItem} from './../../model/catalog.model';

@Component({
  selector: 'app-product-add',
  templateUrl: './product-add.component.html',
  styleUrls: ['./product-add.component.css']
})
export class ProductAddComponent implements OnInit {

  constructor(
    private formBuilder: FormBuilder,
    private router: Router, 
    private apiService: ApiProductService,
    private catalogService: ApiCatalogService,
    public alertService:AlertService) { }

  addForm: FormGroup;
  products: Product[] = [];
  catalogGroup: CatalogItem[];

  ngOnInit(): void {
  	this.addForm = this.formBuilder.group({
      nameLarge: ['', Validators.required],
      nameShort: ['', Validators.required], /* [Validators.required,Validators.pattern("[a-zA-Z0-9]{3,10}")]],*/
      sku: ['',  Validators.required], /*[Validators.required,Validators.pattern("[a-zA-Z0-9]{5,}")]],*/
      description: [],
      percentage: ['', [Validators.required,Validators.pattern("[0-9]{0,6}")]],
      group: ['', Validators.required]
    });
    this.loadCatalogGroup();
  }

  private loadCatalogGroup(): void{
    this.catalogService.getByName('GRUPOPROD').subscribe( response => {
      this.catalogGroup = response.items;
    }, error =>{
      console.error(error);
    });
  }

  onSubmit() {
  	var body = this.addForm.value;
  	body.userId = window.localStorage.getItem("userId");
    this.apiService.create(body)
      .subscribe( data => {
        this.alertService.success('Producto guardado', alertOptions);
        this.cancelar();
      }, error => {
        this.alertService.error('El registro no ha sido guardado: ' + error.error, alertOptions);
      }
    );
  }

  cancelar(){
    this.router.navigate(['product-list']);
  }

  agregar(){
    const sku = this.addForm.controls.sku.value;
    let itemFounded = this.products.find(item => item.sku === sku);
    if(!itemFounded)
      this.products.push(this.addForm.value);
    else
      this.alertService.error('Ya existe un producto registrado con SKU: ' + sku, alertOptions);
  }

  remove(sku: string){
    this.products = this.products.filter(item => item.sku !== sku);
  }

  tableValid(){
    return Array.isArray(this.products) && this.products.length;
  }

  builkSave(){
    if( this.tableValid() ){
      const userId = +window.localStorage.getItem("userId");
      this.products.filter(item => item.userId = userId);
      this.apiService.createBulk(this.products)
        .subscribe( data => {
          this.alertService.success('Productos guardado', alertOptions);
          this.products = [];
        }, error => {
          const errMsg = 'Ha ocurrido un error en la transaccion: ';
          console.error(error);
          if(error.status === 500)
            this.alertService.error(errMsg + error.message, alertOptions);
          else
            this.alertService.error(errMsg + error.error, alertOptions);

        }
      );
    }
  }

}
