export class Cashout {
	total: number;
	totalChange: number;
	detail: CashoutDetail[];
	payment: CashoutPayment[];

	constructor(init?:Partial<Cashout>){
		Object.assign(this, init);
	}
}

export class CashoutDetail {
	group: string;
	total: number;

	constructor(init?:Partial<CashoutDetail>){
		Object.assign(this, init);
	}
}

export class CashoutPayment {
	payment: string;
	amount: number;

	constructor(init?:Partial<CashoutPayment>){
		Object.assign(this, init);
	}

}

export class Withdrawall {
	denominationId: number;
	denomination: string;
	denominationValue: number;
	quantity: number;
	
	constructor(init?:Partial<Withdrawall>){
		Object.assign(this, init);
	}
}

export class Withdrawal {
	ammount: number;
	dateCreated: string;

	constructor(init?:Partial<Withdrawal>){
		Object.assign(this, init);
	}
}