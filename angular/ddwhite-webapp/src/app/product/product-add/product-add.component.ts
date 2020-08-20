import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Router} from "@angular/router";
import { ApiProductService } from "../../service/api.service";
import { AlertService, alertOptions } from '../../_alert';

@Component({
  selector: 'app-product-add',
  templateUrl: './product-add.component.html',
  styleUrls: ['./product-add.component.css']
})
export class ProductAddComponent implements OnInit {

  constructor(private formBuilder: FormBuilder,private router: Router, private apiService: ApiProductService, public alertService:AlertService) { }

  addForm: FormGroup;

  ngOnInit(): void {
  	this.addForm = this.formBuilder.group({
      id: [],
      nameLarge: ['', Validators.required],
      nameShort: ['', Validators.required],
      sku: ['', Validators.required],
      description: ['', Validators.required]
    });
  }

  onSubmit() {
  	var body = this.addForm.value;
  	body.userId = 1;
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

}
