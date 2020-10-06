export class Purchase {
	id: number;
	productId: number;
	productName: string;
	providerId: number;
	providerName: string;
	quantity: number;
	cost: number;
	unity: number;
	unityDesc: string;
	numPiece: number;
	userId: number;

	constructor(init?:Partial<Purchase>){
		Object.assign(this, init);
	}
}

export class PurchaseReasign{
	purchasesOrigin: number;
	purchaseDestity: number;
	quantity: number;

	constructor(init?:Partial<PurchaseReasign>){
		Object.assign(this, init);
	}
}

export class PurchaseList {
	id: number;
	user: string;
	provider: string;
	product: string;
	quantity: number;
	cost: number;
	unity: number;
	unityDesc: string;
	numPiece: number;
	dateCreated: string;
	usedInSale: boolean;

	constructor(init?:Partial<PurchaseList>){
		Object.assign(this, init);
	}

}