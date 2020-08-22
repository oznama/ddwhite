export class Catalog {
	id: number;
	name: string;
	description: string;
	items: CatalogItem[];

	constructor(init?:Partial<Catalog>){
		Object.assign(this, init);
	}
}

export class CatalogItem{
	id: number;
	name: string;
	description: string;

	constructor(init?:Partial<CatalogItem>){
		Object.assign(this, init);
	}
}