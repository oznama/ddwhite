import { Component, OnInit } from '@angular/core';
import {Router} from "@angular/router";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {first} from "rxjs/operators";
import { ApiClientService } from "../../service/module.service";
import { AlertService, alertOptions } from '../../_alert';

@Component({
  selector: 'app-client-edit',
  templateUrl: './client-edit.component.html',
  styleUrls: ['./client-edit.component.css']
})
export class ClientEditComponent implements OnInit {

  constructor(
  	private formBuilder: FormBuilder,
    private router: Router, 
    private apiService: ApiClientService, 
    public alertService:AlertService) {
  }

  editForm: FormGroup;
  phonePatter: string = "[0-9]{10,}";

  ngOnInit(): void {
  	let editClientId = window.localStorage.getItem("editClientId");
    if(!editClientId) {
      alert("Invalid action.")
      this.cancelar();
      return;
    }
  	this.editForm = this.formBuilder.group({
      id: [],
      name: ['', Validators.required],
      midleName: ['', Validators.required],
      lastName: ['', Validators.required],
      address: ['', Validators.required],
      phone: ['', [Validators.required, Validators.pattern(this.phonePatter)]],
      email: [, [Validators.pattern("^[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,4}$")]],
      rfc: [, [Validators.pattern("[a-zA-Z0-9]{12,13}")]],
      bussinessAddress: [],
      bussinessPhone: [, [Validators.pattern(this.phonePatter)]],
      userId: [],
      dateCreated: []
    });
    this.editForm.controls.userId.disable();
    this.editForm.controls.dateCreated.disable();
    this.apiService.getById(+editClientId)
      .subscribe( data => {
        this.editForm.setValue(data);
      }
    );
  }

  onSubmit() {
  	var body = this.editForm.value;
  	body.userId = +window.localStorage.getItem("userId");
    this.apiService.update(body).pipe(first()).subscribe(
        data => {
          if(data.status === 200) {
            this.alertService.success('Cliente actualizado', alertOptions);
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
    this.router.navigate(['client-list']);
  }

}
