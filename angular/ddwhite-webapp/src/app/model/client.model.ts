import { Pageable, Sort } from './api.response';

export class ClientPageable{
	content: Client[];
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

	constructor(init?:Partial<ClientPageable>){
		Object.assign(this, init);
	}
}

export class Client {
	id: number;
	name: string;
	midleName: string;
	lastName: string;
	address: string;
	phone: string;
	email: string;
	rfc: string;
	bussinessAddress: string;
	bussinessPhone: string;
	userId: string;

	constructor(init?:Partial<Client>){
		Object.assign(this, init);
	}
}