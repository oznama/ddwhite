import { Component, OnInit } from '@angular/core';
import {Router} from "@angular/router";
import {Product} from "../../model/product.model";
import { ApiProductService } from "../../service/api.service";
import { AlertService, alertOptions } from '../../_alert';

@Component({
  selector: 'app-product-list',
  templateUrl: './product-list.component.html',
  styleUrls: ['./product-list.component.css']
})
export class ProductListComponent implements OnInit {

  products: Product[];

  constructor(private router:Router, private apiService:ApiProductService, public alertService:AlertService) { }

  ngOnInit(): void {
  	this.apiService.get()
  	  .subscribe( data => {
  	  	this.products = data.content;
  	  })
  }

  delete(product:Product): void{
  	this.apiService.delete(product.id)
  	  .subscribe( response => {
  	  	this.products = this.products.filter( p => p != product);
        this.alertService.success('Producto eliminado', alertOptions);
  	  }, error => {
        console.log(error);
        this.alertService.error(error.error, alertOptions);
      }
    );
  }

  edit(product:Product): void{
    window.localStorage.removeItem("editProductId");
    window.localStorage.setItem("editProductId", product.id.toString());
  	this.router.navigate(['product-edit']);
  }

  add(): void{
  	this.router.navigate(['product-add']);	
  }

}
