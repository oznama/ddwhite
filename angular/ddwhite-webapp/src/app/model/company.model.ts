export class Company {
	name: string;
	address: string;
	phone: string;
	website: string;
	email: string;
	bussinessName: string;
	rfc: string;
	messageTicket: string

	constructor(init?:Partial<Company>){
		Object.assign(this, init);
	}
}