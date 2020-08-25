import { Product } from './product.model';

export class Purchase {
	product: PurchaseProduct;
	providerId: number;
	quantity: number;
	cost: number;
	unity: number;
	userId: number;

	constructor(init?:Partial<Purchase>){
		Object.assign(this, init);
	}
}

export class PurchaseProduct{
	id: number;
	cost: number;

	constructor(init?:Partial<PurchaseProduct>){
		Object.assign(this, init);
	}
}