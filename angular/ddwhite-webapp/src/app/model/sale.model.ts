export class Sale {
	id: number;
	clientId: number;
	clientName: string;
	clientRfc: string;
	userId: number;
	total: number;
	subTotal: number;
	tax: number;
	change: number;
	invoice: string;

	detail: SaleDetail[];
	payments: SalePayment[];

	constructor(init?:Partial<Sale>){
		Object.assign(this, init);
	}
}

export class SaleDetail{
	id: number;
	saleId: number;
	productId: number;
	productName: string;
	productShortName: string;
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
	paymentDesc: string;
	amount: number;
}