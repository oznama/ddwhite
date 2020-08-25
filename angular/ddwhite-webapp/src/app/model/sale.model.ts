export class Sale {
	clientId: string;
	userId: number;
	total: number;
	subTotal: number;
	iva: number;

	detail: SaleDetail[];

	constructor(init?:Partial<Sale>){
		Object.assign(this, init);
	}
}

export class SaleDetail{
	productId: number;
	productName: string
	quantity: number;
	price: number;
	total: number;

	constructor(init?:Partial<SaleDetail>){
		Object.assign(this, init);
	}
}