import { Component } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';

@Component({
  selector: 'app-billing',
  templateUrl: './billing.component.html',
  styleUrl: './billing.component.scss'
})
export class BillingComponent {
  showDialog = false;
  method= 'visa';
  stateOptions = [
    {
      label: 'visa',
      value: 'visa',
      icon: 'fab fa-cc-visa fa-2x',
    },
    {
      label: 'napas',
      value: 'napas',
      icon: 'fa-regular fa-credit-card fa-2x',
    },
    {
      label: 'e-wallet',
      value: 'e-wallet',
      icon: 'fa-solid fa-wallet fa-2x',
    }
  ];
  constructor() {}
  ngOnInit() {
  }

  openDialog() {
    this.showDialog = true;
  }
  onValueChange(envet:any) {
    this.method = envet.value;
    // console.log(this.valuea);
  }
  onTest(){
    alert(this.method);
  }
}
