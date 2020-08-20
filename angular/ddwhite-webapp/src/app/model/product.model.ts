import { Pageable, Sort } from './api.response';

export class ProductPageable{
	content: Product[];
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

	constructor(init?:Partial<ProductPageable>){
		Object.assign(this, init);
	}
}

export class Product {
	id: number;
	nameLarge: string;
	nameShort: string;
	sku: string;
	description: string;
	dateCreated: string;
	userId: number;

	constructor(init?:Partial<Product>){
		Object.assign(this, init);
	}
}