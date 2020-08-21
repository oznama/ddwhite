import { Component, OnInit } from '@angular/core';
import {Router} from "@angular/router";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {first} from "rxjs/operators";
import { ApiProductService } from "../../service/api.service";
import { AlertService, alertOptions } from '../../_alert';

@Component({
  selector: 'app-product-edit',
  templateUrl: './product-edit.component.html',
  styleUrls: ['./product-edit.component.css']
})
export class ProductEditComponent implements OnInit {

  editForm: FormGroup;

  constructor(private formBuilder: FormBuilder,private router: Router, private apiService: ApiProductService, public alertService:AlertService) { }

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
      nameShort: ['', Validators.required],
      sku: ['', Validators.required],
      description: ['', Validators.required],
      userId: [],
      dateCreated: []
    });
    this.apiService.getById(+editProductId)
      .subscribe( data => {
        this.editForm.setValue(data);
      }
    );
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
