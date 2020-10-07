import { Component, OnInit } from '@angular/core';
import {Router} from "@angular/router";
import {first} from "rxjs/operators";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import { ApiPurchaseService, ApiCatalogService, CAT_CONST } from "../../service/module.service";
import { AlertService, alertOptions } from '../../_alert';
import {CatalogItem} from './../../model/catalog.model';
import {Purchase} from '../../model/purchase.model';

@Component({
  selector: 'app-purchase-edit',
  templateUrl: './purchase-edit.component.html',
  styleUrls: ['./purchase-edit.component.css']
})
export class PurchaseEditComponent implements OnInit {

  editForm: FormGroup;
  catalogUnity: CatalogItem[];
  boxId: number;

  constructor(private formBuilder: FormBuilder,
  	private router: Router,
    private apiService: ApiPurchaseService, 
    private catalogService: ApiCatalogService,
    public alertService:AlertService) { }

  ngOnInit(): void {

  	let editPurchaseId = window.localStorage.getItem("editPurchaseId");
    if(!editPurchaseId) {
      alert("Invalid action.")
      this.cancelar();
      return;
    }
  	this.editForm = this.formBuilder.group({
  	  id: [],
      provider: [],
      product: [],
      quantity: ['', [Validators.required,Validators.pattern("[0-9]{0,6}(\.[0-9]{1,3})?")]],
      cost: ['', [Validators.required,Validators.pattern("[0-9]{0,6}(\.[0-9]{1,4})?")]],
      unity: [],
      unityDesc: [],
      numPiece: [],
      user: [],
      dateCreated: [],
      usedInSale: [],
    });
    this.editForm.controls.provider.disable();
    this.editForm.controls.product.disable();
    this.editForm.controls.user.disable();
    this.editForm.controls.dateCreated.disable();
    this.editForm.controls.usedInSale.disable();
    this.loadCatalogUnity();
    this.loadPurchase(+editPurchaseId);
  }

  private loadPurchase(id: number){
    this.apiService.getOne(id)
      .subscribe( data => {
        this.editForm.setValue(data);
      }
    );
  }

  private loadCatalogUnity(): void{
  	this.catalogService.getByName(CAT_CONST.UNITIES).subscribe( response => {
    	this.catalogUnity = response.items;
      this.boxId = this.catalogUnity.find( ci => ci.name.toUpperCase() === 'CAJA' ).id;
    }, error =>{
    	console.error(error);
    });
  }

  onSubmit() {
  	const body = this.setPurchase();
    this.apiService.update(body).pipe(first()).subscribe(
        data => {
          if(data.status === 200) {
            this.alertService.success('Compra actualizada', alertOptions);
            this.cancelar();
          }else {
            this.alertService.error(data.message, alertOptions);
          }
        },
        error => {
          this.alertService.error('El registro no ha sido actualizado: ' + error.error, alertOptions);
        }
    );
  }

  private setPurchase(): Purchase {
    const np = +this.editForm.controls.numPiece.value;
    const currentUnity = +this.editForm.controls.unity.value;
    return <Purchase> {
      id: +this.editForm.controls.id.value,
      quantity: +this.editForm.controls.quantity.value,
      cost: +this.editForm.controls.cost.value,
      unity: currentUnity,
      numPiece:  (np === 0 || currentUnity !== this.boxId) ? null : np,
      userId: +window.localStorage.getItem("userId")
    };
  }


  formInvalid(){
    return !(this.editForm.controls.quantity.value &&
            this.editForm.controls.cost.value &&
            this.editForm.controls.unity.value);
  }

  showNumPiece(){
    const unity = +this.editForm.controls.unity.value;
    return unity !== 0 && unity === this.boxId;
  }

  cancelar(){
    this.router.navigate(['purchase-list']);
  }

}
