import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { HttpEventType, HttpErrorResponse } from '@angular/common/http';
import { of } from 'rxjs';  
import { catchError, map } from 'rxjs/operators';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import { ApiDatabaseService, exportFile, SQL_EXTENSION } from "../../service/module.service";
import { AlertService, alertOptions } from '../../_alert';

@Component({
  selector: 'app-database',
  templateUrl: './database.component.html',
  styleUrls: ['./database.component.css']
})
export class DatabaseComponent implements OnInit {

  @ViewChild("fileUpload", {static: false}) fileUpload: ElementRef;
  files  = [];

  constructor(
  	private formBuilder: FormBuilder,
    private apiService: ApiDatabaseService,
    public alertService:AlertService) { }

  databaseForm: FormGroup;
  optionSelected: number = 1;

  options : DataBaseOptions[] = [
  	{value: 1, viewValue : 'Generar archivo de respaldo'},
  	{value: 2, viewValue : 'Restaurar archivo de respaldo'}
  ];

  ngOnInit(): void {
  	this.databaseForm = this.formBuilder.group({
      mode: [1, Validators.required],
    });
  }

  onChange(option: number){
  	this.optionSelected = option;
  }

  backup(){
	this.apiService.backup().subscribe(
		data => exportFile(data, 'db-backup_', SQL_EXTENSION),
		error => this.alertService.error(error.error, alertOptions)
	);
  }

  onClick() {
  	this.files = [];
    const fileUpload = this.fileUpload.nativeElement;
    fileUpload.onchange = () => {
    	for (let index = 0; index < fileUpload.files.length; index++){
    		const file = fileUpload.files[index];
    		this.files.push({ name: file.name, data: file, inProgress: false, progress: 0});
    	}
      	this.uploadFiles();
    };  
    fileUpload.click();  
  }

  private uploadFiles() {  
    this.fileUpload.nativeElement.value = '';  
    this.files.forEach(file => this.uploadFile(file));
  }

  private uploadFile(file) {  
    const formData = new FormData();  
    formData.append('file', file.data);
    file.inProgress = true;
    this.apiService.restore(formData).pipe(	map(event => {
        switch (event.type) {
          case HttpEventType.UploadProgress:
            file.progress = Math.round(event.loaded * 100 / event.total);
            break;
          case HttpEventType.Response:
          	this.alertService.info('Base de datos restaurada', alertOptions);
            return event;
        }
      }),
      catchError((error: HttpErrorResponse) => {
        file.inProgress = false;
        this.alertService.error(error.error, alertOptions);
        return of(`${file.data.name} upload failed.`);
      })).subscribe((event: any) => {
        if (typeof (event) === 'object') {
          console.log(event.body);
        }
      });
  }


}

interface DataBaseOptions {
  value: number;
  viewValue: string;
}