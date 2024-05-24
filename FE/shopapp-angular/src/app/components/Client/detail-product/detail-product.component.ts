import { Component } from '@angular/core';
import { ProductService } from '../../../services/productService/product.service';
import { ProductResponse } from '../../../responses/products/product.response';
import { ProductImage } from '../../../responses/products/product-image.response';
import { environment } from '../../../environments/environment';
import { CartService } from '../../../services/cart.service';
import { CurrencyPipe } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { MessageService } from 'primeng/api';
import { UserService } from '../../../services/user.service';

@Component({
  selector: 'app-detail-product',
  templateUrl: './detail-product.component.html',
  styleUrl: './detail-product.component.scss',
  providers: [MessageService]
})
export class DetailProductComponent {
  product?: ProductResponse;
  productId: number = 0;
  currentImageProduct: number = 0;
  quantity: number = 1;

  constructor(
              private router: Router,
              private userService: UserService,
              private messageService: MessageService,
              private activeRoute: ActivatedRoute,
              private productService: ProductService,
              private cartService: CartService) { }
  ngOnInit(){
    debugger
    // this.cartService.clearCart();//test xoá giỏ hàng
    // const idParam = 37
    const idParam = this.activeRoute.snapshot.paramMap.get('id');

    if(idParam!==null)
    {
      // this.productId = +idParam;
      this.productId = +idParam;
    }
    if(!isNaN(this.productId))
    {
      this.getComment();
      this.productService.getDetailProduct(this.productId).subscribe({
        next:(response: any) => {
          //lấy ra các hình ảnh của sản phẩm và thay đổi đường dẫn
          debugger
          if(response.product_images && response.product_images.length > 0)
          {
            response.product_images.forEach((product_image:ProductImage)=>{
              product_image.image_url = `${environment.apiBaseUrl}/products/images/${product_image.image_url}`;
            });
          }
          debugger
          this.product = response;
          this.showImage(0);
        },
        complete() {
            debugger
        },
        error: (error) => {
          debugger
          console.log(error);
        }
      });
    } 
  }

  showImage(index: number): void{
    if(this.product && this.product.product_images && this.product.product_images.length > 0){
      // đảm bảo index nằm trong khoảng hợp lệ
      if(index < 0){
        index = 0;
      }
      else if(index >= this.product.product_images.length){
        index = this.product.product_images.length - 1;
      }
      //gán index cho currentImageProduct   
      this.currentImageProduct = index;
    }
  }

  imageClick(index: number){
    debugger
    //gọi khi 1 hình ảnh được click
    this.currentImageProduct = index; //cập nhật currentImageProduct
  }

  nextImage(): void{
    debugger
    this.showImage(this.currentImageProduct + 1);
  }

  prevImage(): void{
    debugger
    this.showImage(this.currentImageProduct - 1);
  }

  // chuyển giá tiền thừ 20000 -> 20.000
  formatPrice(price: number|undefined): string{
    if (price != null){
    // return price.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ".");
    return price.toLocaleString('vi-VN', { style: 'currency', currency: 'VND' });
    }
    return '';
  }

  addToCart(): void{
    debugger
    if (this.product){
      this.cartService.addToCart(this.product.id,this.quantity);
      this.showSuccess('Đã thêm vào giỏ hàng');
    } else{
      console.error('Không thể thêm vào giỏ hàng vì sản phẩm không tồn tại');
    }
  }

  increaseQuantity(): void{
    this.quantity++;
  }

  decreaseQuantity(): void{
    if(this.quantity > 1){
      this.quantity--;
    }
  }

  onBuyNow(){
    this.router.navigate(['/order'], { queryParams: { ids: this.productId } });
  }

  listComment!: any[];
  getComment(){
    this.userService.getComment(this.productId).subscribe({
      next:(response: any) => {
        debugger
        console.log(response);
        response.forEach((comment: any) => {
          comment.created_at = new Date(comment.created_at[0], comment.created_at[1] - 1, comment.created_at[2], comment.created_at[3], comment.created_at[4], comment.created_at[5]);
        });
        this.listComment = response
      },
      complete() {
          debugger
      },
      error: (error) => {
        debugger
        console.log(error);
      }
    });
  }
  contentComment!: string;
  test(){
    alert(this.contentComment);
    this.contentComment = '';
  }
  createComment(){
    const user = this.userService.getUserDetailFromLocalStorage();
    this.userService.addComment(this.productId, this.contentComment,user.user_id).subscribe({
      next:(response: any) => {
        debugger
        console.log(response);
        this.showSuccess('Đã thêm bình luận');
        this.getComment();
      },
      complete() {
          debugger
      },
      error: (error) => {
        debugger
        console.log(error);
        this.showError('Không thể thêm bình luận');
      }
    });
  }

  // Hiển thị thông báo
  showSuccess(message: string) {
    this.messageService.add({severity:'success', summary: 'Success', detail: message});
  }
  showError(message: string) {
    this.messageService.add({severity:'error', summary: 'Error', detail: message});
  }
}
