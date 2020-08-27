import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Router} from "@angular/router";
import { ApiUserService } from "../../service/module.service";
import { AlertService, alertOptions } from '../../_alert';

@Component({
  selector: 'app-user-add',
  templateUrl: './user-add.component.html',
  styleUrls: ['./user-add.component.css']
})
export class UserAddComponent implements OnInit {

  constructor(
  	private formBuilder: FormBuilder,
    private router: Router, 
    private apiService: ApiUserService, 
    public alertService:AlertService) { }

  addForm: FormGroup;

  ngOnInit(): void {
  	this.addForm = this.formBuilder.group({
      username: ['', [Validators.required,Validators.pattern("[a-zA-Z0-9]{1,}")]],
      password: ['', [Validators.required,Validators.pattern("[a-zA-Z0-9]{4,}")]],
      repetPassword: ['', [Validators.required,Validators.pattern("[a-zA-Z0-9]{4,}")]],
      fullName: ['', [Validators.required, Validators.pattern("[a-zA-Z ]{1,}")]]
    });
  }

  isFormValid(): boolean {
  	return this.addForm.valid && this.isSamePasswords();
  }

  isSamePasswords(){
  	return this.addForm.controls.password.value === this.addForm.controls.repetPassword.value;
  }

  onSubmit() {
  	var body = this.addForm.value;
  	//body.userId = window.localStorage.getItem("userId");
    this.apiService.create(body)
      .subscribe( data => {
        this.alertService.success('Usuario guardado', alertOptions);
        this.cancelar();
      }, error => {
        this.alertService.error('El registro no ha sido guardado: ' + error.error, alertOptions);
      }
    );
  }

  cancelar(){
    this.router.navigate(['user-list']);
  }

}
