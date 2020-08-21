import { Component, OnInit } from '@angular/core';
import {Router} from "@angular/router";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {first} from "rxjs/operators";
import { ApiProviderService } from "../../service/api.service";
import { AlertService, alertOptions } from '../../_alert';

@Component({
  selector: 'app-provider-edit',
  templateUrl: './provider-edit.component.html',
  styleUrls: ['./provider-edit.component.css']
})
export class ProviderEditComponent implements OnInit {

  editForm: FormGroup;

  constructor(private formBuilder: FormBuilder,private router: Router, private apiService: ApiProviderService, public alertService:AlertService) { }

  ngOnInit(): void {
  	let editProviderId = window.localStorage.getItem("editProviderId");
    if(!editProviderId) {
      alert("Invalid action.")
      this.cancelar();
      return;
    }
  	this.editForm = this.formBuilder.group({
      id: [],
      bussinesName: ['', Validators.required],
      address: ['', Validators.required],
      phone: ['', [Validators.required, Validators.pattern("[0-9]{10,}")]],
      contactName: ['', Validators.required],
      website: ['', [Validators.pattern("(https?://)?([\\da-z.-]+)\\.([a-z.]{2,6})[/\\w .-]*/?")]],
      userId: [],
      dateCreated: []
    });
    this.apiService.getById(+editProviderId)
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
            this.alertService.success('Proveedor actualizado', alertOptions);
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
    this.router.navigate(['provider-list']);
  }

}
