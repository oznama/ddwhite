import { Pageable, Sort } from './api.response';

export class UserPageable{
	content: User[];
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

	constructor(init?:Partial<UserPageable>){
		Object.assign(this, init);
	}
}

export class User {
	id: number;
	username: string;
	password: string;
	fullName: string;
	dateCreated: string;

	constructor(init?:Partial<User>){
		Object.assign(this, init);
	}
}