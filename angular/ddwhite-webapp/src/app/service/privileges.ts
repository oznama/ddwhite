import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { ApiLoginService } from './api.service.login';

const roles = {
	ROOT: 'root',
	ADMIN: 'editor'
}

const privileges = {
	CREATE: 'INSRT',
	MODIFY: 'UPDTE',
	DELETE: 'DELTE',
	PURCHASES: 'PURCH',
	SALES: 'SALE',
	REPORTS: 'REPRT'
}

@Injectable()
export class Privileges {

	constructor( private loginService: ApiLoginService ){}

	private menuAdmin = new BehaviorSubject<boolean>(false);
	private menuPurchase = new BehaviorSubject<boolean>(false);
	private menuReport = new BehaviorSubject<boolean>(false);
	private menuInventory = new BehaviorSubject<boolean>(false);

	private noPrivileges(): number {
		return this.loginService.Privileges.length;
	}

	public isAdmin(): boolean {
		return this.loginService.Role === roles.ROOT || this.loginService.Role === roles.ADMIN;
	}

	private isReporter$(): boolean {
		return this.loginService.Privileges.includes(privileges.REPORTS);
	}

	closeDialogs(): boolean {
		return this.isAdmin() || this.isReporter$();
	}

	get isRoleAdmin() {
		this.menuAdmin.next( this.isAdmin() );
		return this.menuAdmin.asObservable();
	}

	get isPurchaser() {
		this.menuPurchase.next(this.loginService.Privileges.includes(privileges.PURCHASES));
		return this.menuPurchase.asObservable();
	}

	get isReporter() {
		this.menuReport.next(this.isReporter$());
		return this.menuReport.asObservable();
	}

	get showCashout(){
		this.menuInventory.next( this.isAdmin() || this.canSale() )
		return this.menuInventory.asObservable();
	}

	canSale(): boolean {
		return this.loginService.Privileges.includes(privileges.SALES);
	}

	canCreate(): boolean {
		return this.loginService.Privileges.includes(privileges.CREATE);
	}

	canEdit(): boolean {
		return this.loginService.Privileges.includes(privileges.MODIFY);
	}

	canRemove(): boolean {
		return this.loginService.Privileges.includes(privileges.DELETE);
	}

}