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
	percentage: number;
	group: number;
	groupDesc: string;
	dateCreated: string;
	userId: number;

	inventory: Inventory;

	constructor(init?:Partial<Product>){
		Object.assign(this, init);
		this.inventory = new Inventory();
	}
}

export class Inventory{
	productId: number;
	quantity: number;
	averageCost: number;
	currentCost: number;
	price: number;
	unityDesc: string;
	unity: number;
	numPiece: number;

	constructor(init?:Partial<Inventory>){
		Object.assign(this, init);
	}
}

export interface ModeProductDialog {
  mode: 'inventory' | 'sale' | 'all';
  productsSelected: Product[];
}