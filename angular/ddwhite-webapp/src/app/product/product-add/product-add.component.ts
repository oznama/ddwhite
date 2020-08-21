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
      nameLarge: ['', Validators.required],
      nameShort: ['', Validators.required],
      sku: ['', [Validators.required,Validators.pattern("[a-zA-Z0-9]{1,}")]],
      description: []
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

}
