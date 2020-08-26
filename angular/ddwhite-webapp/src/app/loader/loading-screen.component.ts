import { Component, OnInit, OnDestroy } from '@angular/core';
import { LoadingScreenService } from "./../service/module.service";
import { Subscription } from "rxjs";

@Component({
  selector: 'loading-screen',
  templateUrl: './loading-screen.component.html',
  styleUrls: ['./loading-screen.component.css']
})
export class LoadingScreenComponent implements OnInit, OnDestroy {
   
   loading: boolean = false;
   loadingSubscription: Subscription;

   constructor(private loadingScreenService: LoadingScreenService) {}

   ngOnInit(): void {
   	this.loadingSubscription = this.loadingScreenService.loadingStatus.subscribe((value) => {
      this.loading = value;
    });
   }

   ngOnDestroy(): void {
   	this.loadingSubscription.unsubscribe();
   }
}