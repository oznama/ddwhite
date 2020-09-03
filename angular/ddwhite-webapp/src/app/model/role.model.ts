import { Pageable, Sort } from './api.response';

export class RolePageable{
	content: Role[];
	pageable: Pageable;
	totalElements: number;
	totalPages: number;
	last: boolean;
	size: number;
	number: number;
	sort: Sort;
	numberOfElements: number;
	first: boolean;
	empty: boolean;

	constructor(init?:Partial<RolePageable>){
		Object.assign(this, init);
	}
}

export class Role {
	id: number;
	name: string;
	description: string;

	constructor(init?:Partial<Role>){
		Object.assign(this, init);
	}
}