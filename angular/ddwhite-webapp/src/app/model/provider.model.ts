import { Pageable, Sort } from './api.response';

export class ProviderPageable{
	content: Provider[];
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

	constructor(init?:Partial<ProviderPageable>){
		Object.assign(this, init);
	}
}

export class Provider {
	id: number;
	bussinesName: string;
	address: string;
	phone: string;
	website: string;
	contactName: string;
	userId: number;
	dateCreated: string;

	constructor(init?:Partial<Provider>){
		Object.assign(this, init);
	}
}