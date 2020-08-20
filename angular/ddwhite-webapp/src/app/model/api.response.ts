export class ApiResponse {

}

export class Pageable{
	offset: number;
	pageNumber: number;
	pageSize: number;
	paged: number;
	sort: Sort;
	unpaged: boolean;

	constructor(init?:Partial<Pageable>){
		Object.assign(this, init);
	}
}

export class Sort{
	empty: boolean;
	sorted: boolean;
	unsorted: boolean;

	constructor(init?:Partial<Sort>){
		Object.assign(this, init);
	}
}