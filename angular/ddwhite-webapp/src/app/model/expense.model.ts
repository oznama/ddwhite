import { Pageable, Sort } from './api.response';

export class ExpensePageable{
	content: Expense[];
	pageable: Pageable;
	totalElements: number;
	totalPages: number;
	last: boolean;
	size: number;
	number: number;
	sort: Sort;
	numberOfElements: number;
	first: boolean;
	empty: boolean;

	constructor(init?:Partial<ExpensePageable>){
		Object.assign(this, init);
	}
}

export class Expense {
	description: string;
	amount: number;
	taxeable: boolean;
	invoice: string;
	userId: number;
	userName: string;

	constructor(init?:Partial<Expense>){
		Object.assign(this, init);
	}
}