import { Component, OnInit, Optional, Inject } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Router} from "@angular/router";
import { ApiClientService } from "../../service/module.service";
import { AlertService, alertOptions } from '../../_alert';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Client } from './../../model/client.model';

@Component({
  selector: 'app-client-add',
  templateUrl: './client-add.component.html',
  styleUrls: ['./client-add.component.css']
})
export class ClientAddComponent implements OnInit {

  constructor(
  	private formBuilder: FormBuilder,
    private router: Router, 
    private apiService: ApiClientService, 
    public alertService:AlertService,
    public dialogRef: MatDialogRef<ClientAddComponent>, @Optional() @Inject(MAT_DIALOG_DATA) public dClient: Client) {
  }

  addForm: FormGroup;
  phonePatter: string = "[0-9]{10,}";

  ngOnInit(): void {
  	this.addForm = this.formBuilder.group({
      name: ['', Validators.required],
      midleName: ['', Validators.required],
      lastName: ['', Validators.required],
      address: ['', Validators.required],
      phone: ['', [Validators.required, Validators.pattern(this.phonePatter)]],
      email: [, [Validators.pattern("[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,4}")]],
      rfc: [, [Validators.pattern("[a-zA-Z0-9]{12,13}")]],
      bussinessAddress: [],
      bussinessPhone: [, [Validators.pattern(this.phonePatter)]],
    });
  }

  save() {
  	var body = this.addForm.value;
  	body.userId = window.localStorage.getItem("userId");
    this.apiService.create(body).subscribe( data => {
        this.alertService.success('Cliente guardado', alertOptions);
        this.dialogRef.close({ event: 'close', data: data });
      }, error => {
        this.alertService.error('El registro no ha sido guardado: ' + error.error, alertOptions);
      }
    );
  }

}
