import { Component } from '@angular/core';

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrl: './cart.component.scss' 
})
export class CartComponent {
  selectAll: boolean = false;
  products: any[] = [
    { id: 1, name: 'Sản phẩm 1', type: 'Loại 1', price: 100, selected: false },
    { id: 2, name: 'Sản phẩm 2', type: 'Loại 2', price: 150, selected: false },
  
    // ... và tiếp tục cho các sản phẩm khác
  ];

  checkAll() {
    // Đặt trạng thái của tất cả các sản phẩm bằng với checkbox chọn tất cả
    this.products.forEach(product => product.selected = this.selectAll);
    this.updateSelectedProductCount();
  }

  checkSingle() {
    // Nếu có bất kỳ checkbox con nào không được chọn, checkbox chọn tất cả sẽ không được chọn
    this.selectAll = this.products.every(product => product.selected);
    this.updateSelectedProductCount();
  }

  selectedProductCount: number = 0;
  stringProductCount: string = '';
  updateSelectedProductCount() {
    this.selectedProductCount = this.products.filter(product => product.selected).length;
    if (this.selectedProductCount != 0) {
      this.stringProductCount = '('+`${this.selectedProductCount}`+')';
    }
    else {
      this.stringProductCount = '';
    }
  }
}
