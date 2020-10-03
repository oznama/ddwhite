import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Router} from "@angular/router";
import { ApiCompanyService, ApiCatalogService } from "../service/module.service";
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
    console.log(body);
    this.apiService.update(body).subscribe( data => {
        this.alertService.success('Datos de compa&ntilde;ia guardado', alertOptions);
      }, error => {
        this.alertService.error('Los datos no han sido guardados: ' + error.error, alertOptions);
      }
    );
  }

  private loadCompanyData(): void{
    this.catalogService.getByName('COMPANY').subscribe( response => {
      if(response && response.items){
        response.items.forEach( (value, index) => {
          switch(value.name){
            case 'NOMBRE':
              this.addForm.controls.name.setValue(value.description);
              break;
            case 'DIRECCION':
               this.addForm.controls.address.setValue(value.description);
              break;
            case 'TELEFONO':
              this.addForm.controls.phone.setValue(value.description);
              break;
            case 'PAGINA':
              this.addForm.controls.website.setValue(value.description);
              break;
            case 'EMAIL':
              this.addForm.controls.email.setValue(value.description);
              break;
            case 'NOMBRE_FISCAL':
              this.addForm.controls.bussinesName.setValue(value.description);
              break;
            case 'RFC':
              this.addForm.controls.rfc.setValue(value.description);
              break;
            case 'MESSAGE_TICKET':
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
