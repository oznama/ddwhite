import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Router} from "@angular/router";
import { ApiProviderService } from "../../service/api.service";
import { AlertService, alertOptions } from '../../_alert';

@Component({
  selector: 'app-provider-add',
  templateUrl: './provider-add.component.html',
  styleUrls: ['./provider-add.component.css']
})
export class ProviderAddComponent implements OnInit {

  constructor(
    private formBuilder: FormBuilder,
    private router: Router, 
    private apiService: ApiProviderService, 
    public alertService:AlertService) {
  }

  addForm: FormGroup;

  ngOnInit(): void {
  	this.addForm = this.formBuilder.group({
      bussinesName: ['', Validators.required],
      address: ['', Validators.required],
      phone: ['', [Validators.required, Validators.pattern("[0-9]{10,}")]],
      contactName: ['', Validators.required],
      website: ['', [Validators.pattern("(https?://)?([\\da-z.-]+)\\.([a-z.]{2,6})[/\\w .-]*/?")]]
    });
  }    

  onSubmit() {
  	var body = this.addForm.value;
  	body.userId = window.localStorage.getItem("userId");
    this.apiService.create(body)
      .subscribe( data => {
        this.alertService.success('Proveedor guardado', alertOptions);
        this.cancelar();
      }, error => {
        this.alertService.error('El registro no ha sido guardado: ' + error.error, alertOptions);
      }
    );
  }

  cancelar(){
    this.router.navigate(['provider-list']);
  }

}
