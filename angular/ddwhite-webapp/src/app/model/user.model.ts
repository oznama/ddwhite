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
	roleId: number;
	roleName: string;

	constructor(init?:Partial<User>){
		Object.assign(this, init);
	}
}

export class Session {
	id: number;
	userId: number;
	userFullname: string;
	inDate: string;
	outDate: string;
	initialAmount: number;
	finalAmount: number;

	constructor(init?:Partial<Session>){
		Object.assign(this, init);
	}

}