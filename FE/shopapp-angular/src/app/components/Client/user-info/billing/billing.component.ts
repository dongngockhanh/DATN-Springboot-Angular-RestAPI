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
  // validate form
  // check time card
  formatInput(event: Event) {
    const input = event.target as HTMLInputElement;
    let value = input.value.replace(/\D/g, '').substring(0, 4); // Remove non-digits and limit length
    if (value.length > 2) {
      const month = parseInt(value.substring(0, 2), 10);
      const year = value.substring(2);
      if (month > 12) {
        value = '12/' + year;
      } else {
        value = value.substring(0, 2) + '/' + year;
      }
    }
    input.value = value;
  }
}
