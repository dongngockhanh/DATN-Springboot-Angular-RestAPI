import { Component } from '@angular/core';
import { CartService } from '../../services/cart.service';
import { ProductService } from '../../services/productService/product.service';
import { ProductResponse } from '../../responses/products/product.response';
import { environment } from '../../environments/environment';

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrl: './cart.component.scss' 
})
export class CartComponent {
  constructor(private cartService: CartService,private productService: ProductService) {}

  selectAll: boolean = false;
  // products: any[] = [
  //   { id: 1, name: 'Sản phẩm 1', type: 'Loại 1', price: 100, quantity:1, selected: false },
  //   { id: 2, name: 'Sản phẩm 3', type: 'Loại 1', price: 200, quantity:1, selected: false },
  //   { id: 3, name: 'Sản phẩm 4', type: 'Loại 2', price: 250, quantity:1, selected: false}
  //   // ... và tiếp tục cho các sản phẩm khác
  // ];

  products: ProductResponse[] = [];
  listId: number[] = [];
  ngOnInit() {
    // lấy danh sách id và số lượng sản phẩm trong giỏ hàng
    this.listId = Array.from(this.cartService.getCart().keys());
    // tính tông tiền sản phẩm trong giỏ hàng
    this.getCarts();
  }

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

  totalMoney: number = 0;
  selectedProductCount: number = 0;
  stringProductCount: string = '';
  updateSelectedProductCount() {
    debugger
    this.selectedProductCount = this.products.filter(product => product.selected).length;
    if (this.selectedProductCount != 0) {
      // this.stringProductCount = '('+`${this.selectedProductCount}`+')';
      // tổng quantity sản phẩm được chọn
      this.stringProductCount = `(${this.products.filter(product => product.selected).reduce((sum, product) => sum + product.quantity, 0)})`;
      // tổng tiền sản phẩm được chọn
      this.totalMoney = this.products.filter(product => product.selected).reduce((sum, product) => sum + product.price * product.quantity, 0);
    }
    else {
      this.totalMoney = 0;
      this.stringProductCount = '';
    }
  }

  //lấy danh sách sản phẩm trong giỏ hàng
  getCarts() {
    this.productService.getProductByIds(this.listId).subscribe({
      next: (response:any) => {
        debugger
        //lấy số lượng sản phẩm trong giỏ hàng
        this.products = response.map((product: ProductResponse) => {
          product.image = `${environment.apiBaseUrl}/products/images/${product.image}`;
          product.quantity = this.cartService.getCart().get(product.id) || 1;
          return product;
        });
        this.products = response;
      },
      complete: () => {
        console.log('complete');
      },
      error: (error) => {
        console.log(error);
      }
    });
  }

  //tăng giảm số lượng sản phẩm
  increaseQuantity(productId:number) {
    debugger
    let product = this.products.find(product => product.id == productId);
    if (product) {
      product.quantity++;
      // cập nhật lại số lượng sản phẩm trong giỏ hàng 
      this.cartService.updateCart(productId, product.quantity);
      // cập nhật số tổng tiền sản phẩm được chọn
      this.updateSelectedProductCount();
    }
  }
  decreaseQuantity(productId:number) {
    debugger
    let product = this.products.find(product => product.id == productId);
    if (product && product.quantity > 1) {
      product.quantity--;
      // cập nhật lại số lượng sản phẩm trong giỏ hàng 
      // this.cartService.getCart().get(productId)! - 1
      this.cartService.updateCart(productId, product.quantity);
      // cập nhật số tổng tiền sản phẩm được chọn
      this.updateSelectedProductCount();
    }
  }

  //xóa sản phẩm trong giỏ hàng
  removeProduct(productId:number) {
    debugger
    this.cartService.removeProduct(productId);
    this.products = this.products.filter(product => product.id != productId);
    this.updateSelectedProductCount();
  }
  // xoá các sản phẩm được chọn
  removeSelectedProduct() {
    debugger
    // Xoá các sản phẩm đã chọn khỏi local storage trước
    this.products.filter(product => product.selected).forEach(product => this.cartService.removeProduct(product.id));
  
    // Sau đó mới lọc mảng sản phẩm trên màn hình
    this.products = this.products.filter(product => !product.selected);
  
    // Cập nhật thông tin sản phẩm được chọn
    this.updateSelectedProductCount();
  }
}
