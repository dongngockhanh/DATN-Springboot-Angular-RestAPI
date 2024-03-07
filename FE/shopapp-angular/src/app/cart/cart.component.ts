import { Component } from '@angular/core';

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrl: './cart.component.scss' 
})
export class CartComponent {
  headerCheckbox = false;
  itemCheckboxes: boolean[] = [true];

  onHeaderCheckboxChange() {
    for(let i = 0; i < this.itemCheckboxes.length; i++) {
      this.itemCheckboxes[i] = this.headerCheckbox;
    }
  }
}
