import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Router} from "@angular/router";
import { ApiCompanyService, ApiCatalogService, CAT_CONST } from "../service/module.service";
import { AlertService, alertOptions } from '../_alert';

@Component({
  selector: 'app-mycompany',
  templateUrl: './mycompany.component.html',
  styleUrls: ['./mycompany.component.css']
})
export class MycompanyComponent implements OnInit {

  constructor(
  	private formBuilder: FormBuilder,
    private router: Router, 
    private apiService: ApiCompanyService,
    private catalogService: ApiCatalogService,
    public alertService:AlertService) {
  }

  addForm: FormGroup;

  ngOnInit(): void {
  	this.addForm = this.formBuilder.group({
      name: ['', Validators.required],
      address: ['', Validators.required],
      phone: ['', [Validators.required, Validators.pattern("[0-9]{10,}")]],
      website: [, [Validators.pattern("((https?|ftp|smtp):\/\/)?(www.)?[a-z0-9]+\.[a-z]+(\/[a-zA-Z0-9#]+\/?)*")]],
      email: [, [Validators.pattern("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}")]],
      rfc: [, [Validators.pattern("[a-zA-Z0-9]{12,13}")]],
      bussinesName: [],
      messageTicket: [],
    });
    this.loadCompanyData();
  }

  onSubmit() {
  	var body = this.addForm.value;
    this.apiService.update(body).subscribe( data => {
        this.alertService.success('Datos de compa&ntilde;ia guardado', alertOptions);
      }, error => {
        this.alertService.error('Los datos no han sido guardados: ' + error.error, alertOptions);
      }
    );
  }

  private loadCompanyData(): void{
    this.catalogService.getByName(CAT_CONST.DATA_COMPANY).subscribe( response => {
      if(response && response.items){
        response.items.forEach( (value, index) => {
          switch(value.name){
            case CAT_CONST.DATA_COMPANY_NAME:
              this.addForm.controls.name.setValue(value.description);
              break;
            case CAT_CONST.DATA_COMPANY_ADDRESS:
               this.addForm.controls.address.setValue(value.description);
              break;
            case CAT_CONST.DATA_COMPANY_PHONE:
              this.addForm.controls.phone.setValue(value.description);
              break;
            case CAT_CONST.DATA_COMPANY_WEBSITE:
              this.addForm.controls.website.setValue(value.description);
              break;
            case CAT_CONST.DATA_COMPANY_EMAIL:
              this.addForm.controls.email.setValue(value.description);
              break;
            case CAT_CONST.DATA_COMPANY_BUSINESS_NAME:
              this.addForm.controls.bussinesName.setValue(value.description);
              break;
            case CAT_CONST.DATA_COMPANY_RFC:
              this.addForm.controls.rfc.setValue(value.description);
              break;
            case CAT_CONST.DATA_COMPANY_MESSAGE:
              this.addForm.controls.messageTicket.setValue(value.description);
              break;
            default:
          }
        });
      }
    }, error =>{
      console.error(error);
    });
  }

}
