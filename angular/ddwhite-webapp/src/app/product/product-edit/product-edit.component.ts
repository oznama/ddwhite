import { Component, OnInit } from '@angular/core';
import {Router} from "@angular/router";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {first} from "rxjs/operators";
import { ApiProductService, ApiCatalogService } from "../../service/module.service";
import { AlertService, alertOptions } from '../../_alert';
import {CatalogItem} from './../../model/catalog.model';

@Component({
  selector: 'app-product-edit',
  templateUrl: './product-edit.component.html',
  styleUrls: ['./product-edit.component.css']
})
export class ProductEditComponent implements OnInit {

  editForm: FormGroup;
  catalogGroup: CatalogItem[];

  constructor(
    private formBuilder: FormBuilder,
    private router: Router, 
    private apiService: ApiProductService, 
    private catalogService: ApiCatalogService,
    public alertService:AlertService) { }

  ngOnInit(): void {
  	let editProductId = window.localStorage.getItem("editProductId");
    if(!editProductId) {
      alert("Invalid action.")
      this.cancelar();
      return;
    }
  	this.editForm = this.formBuilder.group({
  	  id: [],
      nameLarge: ['', Validators.required],
      nameShort: ['', [Validators.required,Validators.pattern("[a-zA-Z0-9]{1,10}")]],
      sku: ['', Validators.required],
      description: ['', Validators.required],
      percentage: ['', [Validators.required,Validators.pattern("[0-9]{0,6}(\.[0-9]{1,2})?")]],
      cost: [],
      group: ['', Validators.required],
      price: [],
      userId: [],
      dateCreated: []
    });
    this.editForm.controls.userId.disable();
    this.editForm.controls.dateCreated.disable();
    this.editForm.controls.price.disable();
    this.loadCatalogGroup();
    this.loadProduct(+editProductId);
  }

  private loadProduct(id: number){
    this.apiService.getById(id)
      .subscribe( data => {
        this.editForm.setValue(data);
      }
    );
  }

  private loadCatalogGroup(): void{
    this.catalogService.getByName('GRUPOPROD').subscribe( response => {
      this.catalogGroup = response.items;
    }, error =>{
      console.error(error);
    });
  }

  onSubmit() {
  	var body = this.editForm.value;
  	body.userId = window.localStorage.getItem("userId");
    this.apiService.update(body).pipe(first()).subscribe(
        data => {
          if(data.status === 200) {
            this.alertService.success('Producto actualizado', alertOptions);
            this.cancelar();
          }else {
            this.alertService.error(data.message, alertOptions);
          }
        },
        error => {
          this.alertService.error('El registro no ha sido actualizado: ' + error.error, alertOptions);
        }
    );
  }

   cancelar(){
    this.router.navigate(['product-list']);
  }

}
