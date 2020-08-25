export class Client {
	id: string;

	constructor(init?:Partial<Client>){
		Object.assign(this, init);
	}
}