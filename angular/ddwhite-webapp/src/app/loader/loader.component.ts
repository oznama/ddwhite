import { Component, OnInit } from '@angular/core';
import { LoaderService } from "./../service/module.service";
import { Observable } from "rxjs";
import { delay } from 'rxjs/operators'

@Component({
  selector: 'loader',
  templateUrl: './loader.component.html',
  styleUrls: ['./loader.component.css']
})
export class LoaderComponent implements OnInit {

   color = 'primary';
   mode = 'indeterminate';
   value = 50;
   isLoading: Observable<boolean>;

   constructor(private loaderService: LoaderService){
   }

   ngOnInit() {
   	this.isLoading = this.loaderService.getLoading().pipe(delay(0));
   }

}