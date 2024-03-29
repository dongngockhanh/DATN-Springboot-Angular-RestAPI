import { Component } from '@angular/core';
import { ProductService } from '../../services/productService/product.service';
import { ProductResponse } from '../../responses/products/product.response';
import { ProductImage } from '../../responses/products/product-image.response';
import { environment } from '../../environments/environment';

@Component({
  selector: 'app-detail-product',
  templateUrl: './detail-product.component.html',
  styleUrl: './detail-product.component.scss'
})
export class DetailProductComponent {
  product?: ProductResponse;
  productId: number = 0;
  currentImageProduct: number = 0;

  constructor(private productService: ProductService) { }
  ngOnInit(){
    debugger
    const idParam = 6
    if(idParam!==null)
    {
      this.productId = +idParam;
    }
    if(!isNaN(this.productId))
    {
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
      })
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
}
