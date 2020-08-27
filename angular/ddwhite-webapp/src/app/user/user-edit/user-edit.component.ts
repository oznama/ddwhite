import { Component, OnInit } from '@angular/core';
import {Router} from "@angular/router";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {first} from "rxjs/operators";
import { ApiUserService } from "../../service/module.service";
import { AlertService, alertOptions } from '../../_alert';

@Component({
  selector: 'app-user-edit',
  templateUrl: './user-edit.component.html',
  styleUrls: ['./user-edit.component.css']
})
export class UserEditComponent implements OnInit {

  editForm: FormGroup;

  constructor(
    private formBuilder: FormBuilder,
    private router: Router, 
    private apiService: ApiUserService, 
    public alertService:AlertService) {
  }

  ngOnInit(): void {
  	let editUserId = window.localStorage.getItem("editUserId");
    if(!editUserId) {
      alert("Invalid action.")
      this.cancelar();
      return;
    }
  	this.editForm = this.formBuilder.group({
      id: [],
      username: [''],
      password: ['', [Validators.required,Validators.pattern("[a-zA-Z0-9]{4,}")]],
      repetPassword: ['', [Validators.required,Validators.pattern("[a-zA-Z0-9]{4,}")]],
      fullName: ['', [Validators.required, Validators.pattern("[a-zA-Z ]{1,}")]],
      //userId: [],
      dateCreated: []
    });
    this.editForm.controls.username.disable();
    //this.editForm.controls.userId.disable();
    this.editForm.controls.dateCreated.disable();
    this.apiService.getById(+editUserId)
      .subscribe( data => {
        this.editForm.controls.id.setValue(data.id);
        this.editForm.controls.username.setValue(data.username);
        this.editForm.controls.password.setValue(data.password);
        this.editForm.controls.repetPassword.setValue(data.password);
        this.editForm.controls.fullName.setValue(data.fullName);
        this.editForm.controls.dateCreated.setValue(data.dateCreated);
      }
    );
  }

  isFormValid(): boolean {
  	return this.editForm.valid && this.isSamePasswords();
  }

  isSamePasswords(){
  	return this.editForm.controls.password.value === this.editForm.controls.repetPassword.value;
  }

  onSubmit() {
  	var body = this.editForm.value;
  	//body.userId = window.localStorage.getItem("userId");
    this.apiService.update(body).pipe(first()).subscribe(
        data => {
          if(data.status === 200) {
            this.alertService.success('Usuario actualizado', alertOptions);
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
    this.router.navigate(['user-list']);
  }

}
