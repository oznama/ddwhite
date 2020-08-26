export class Sale {
	id: number;
	clientId: number;
	userId: number;
	total: number;
	subTotal: number;
	tax: number;

	detail: SaleDetail[];

	constructor(init?:Partial<Sale>){
		Object.assign(this, init);
	}
}

export class SaleDetail{
	id: number;
	saleId: number;
	productId: number;
	productName: string
	quantity: number;
	price: number;
	total: number;

	constructor(init?:Partial<SaleDetail>){
		Object.assign(this, init);
	}
}

export class SalePayment{
	id: number;
	saleId: number;
	payment: number;
	amount: number;
}