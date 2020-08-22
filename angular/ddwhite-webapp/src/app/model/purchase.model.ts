import { Product } from './product.model';

export class Purchase {
	product: PurchaseProduct;
	providerId: number;
	quantity: number;
	unitPrice: number;
	unity: number;
	userId: number;

	constructor(init?:Partial<Purchase>){
		Object.assign(this, init);
	}
}

export class PurchaseProduct{
	id: number;

	constructor(init?:Partial<PurchaseProduct>){
		Object.assign(this, init);
	}
}