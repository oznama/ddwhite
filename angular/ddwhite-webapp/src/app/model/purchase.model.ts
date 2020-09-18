export class Purchase {
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