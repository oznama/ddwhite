import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Router} from "@angular/router";
import { Observable, of } from 'rxjs/index';
import {Expense} from './../model/expense.model';
import {User} from './../model/user.model';
import { AlertService, alertOptions } from '../_alert';
import { ApiExpenseService, ApiUserService } from './../service/module.service';
import { map, withLatestFrom, startWith, tap } from 'rxjs/operators';

@Component({
  selector: 'app-expense',
  templateUrl: './expense.component.html',
  styleUrls: ['./expense.component.css']
})
export class ExpenseComponent implements OnInit {

  expenseForm: FormGroup;
  searchForm: FormGroup;
  expenses: Observable<Expense[]>;
  expensesFiltred$: Observable<Expense[]>;

  constructor(
  	private formBuilder: FormBuilder,
  	private router: Router, 
  	private apiService: ApiExpenseService,
  	private userService: ApiUserService,
  	public alertService:AlertService) { 
  }

  ngOnInit(): void {
  	this.expenseForm = this.formBuilder.group({
  		description: ['', Validators.required],
  		amount: ['', [Validators.required,Validators.pattern("[0-9]{0,6}(\.[0-9]{1,2})?")]]
  	});
  	this.searchForm = this.formBuilder.group({
      userFullName: [],
      description: [],
    });
  	this.loadExpenses();
  }

  private loadExpenses(): void{
  	this.apiService.get().subscribe( data => {
  		if(data && data.content){
	    	this.expenses = of(this.loadUserFullname(data.content));
	    	this.expensesFiltred$ = of(this.loadUserFullname(data.content));
  		}
    });
  }

  private loadUserFullname(expenses: Expense[]): Expense[]{
  	expenses.forEach( exp => {
		this.userService.getById(exp.userId).subscribe( data => {
			if(data) exp.userName = data.fullName;
		});
	});
	return expenses;
  }

  save() {
  	const body = this.expenseForm.value;
  	body.userId = +window.localStorage.getItem("userId");
    this.apiService.create(body)
      .subscribe( data => {
        this.alertService.success('Gasto registrado', alertOptions);
        this.expenseForm.reset();
        this.loadExpenses();
      }, error => {
        this.alertService.error('El gasto no ha sido guardado: ' + error.error, alertOptions);
      }
    );
  }

  doFilter(): void{
    var userFullName = this.searchForm.get('userFullName').value;
    var description = this.searchForm.get('description').value;
    if(userFullName || description){
      if( userFullName && description ){
        this.expensesFiltred$ = this.expenses.pipe(map( 
          items => items.filter( 
            provider => (provider.userName.toLowerCase().includes(userFullName) 
                      && provider.description.toLowerCase().includes(description))
          )
        ));
      } else if( userFullName ){
        this.expensesFiltred$ = this.expenses.pipe(map( 
          items => items.filter( 
            provider => provider.userName.toLowerCase().includes(userFullName)
          )
        ));
      } else if( description ){
        this.expensesFiltred$ = this.expenses.pipe(map( 
          items => items.filter( 
            provider => provider.description.toLowerCase().includes(description)
          )
        ));
      }
    }
  }

  clearFilter(): void{
    this.searchForm.reset();
    this.expensesFiltred$ = this.expenses;
  }

}
