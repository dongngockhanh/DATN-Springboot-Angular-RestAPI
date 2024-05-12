import { Component, ElementRef, HostListener, OnInit, Renderer2, ViewChild } from '@angular/core';
import { ProductResponse } from '../../../responses/products/product.response';
import { ProductService } from '../../../services/productService/product.service';
import { environment } from '../../../environments/environment';
import Aos from 'aos';
import { CategoryResponse } from '../../../responses/categories/category.response';
import { CategoryService } from '../../../services/categoryService/category.service';
import { ActivatedRoute, Router } from '@angular/router';
import { ViewEncapsulation } from '@angular/compiler';
import { CartService } from '../../../services/cart.service';
import { StorageService } from '../../../services/storage.service';
import { MessageService } from 'primeng/api';


@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  // templateUrl: './home.component.html',
  styleUrl: './home.component.scss',
  providers: [MessageService],
})
export class HomeComponent implements OnInit{
  // products = [
  //   { id: 1, name: 'Product 1', price: 100 },
  //   { id: 2, name: 'Product 2', price: 200 },
  //   { id: 3, name: 'Product 3', price: 300 },
  //   { id: 4, name: 'Product 4', price: 400 },
  //   { id: 5, name: 'Product 5', price: 500 },
  //   { id: 6, name: 'Product 6', price: 600 },
  //   { id: 7, name: 'Product 7', price: 700 },
  //   { id: 8, name: 'Product 8', price: 800 },
  //   { id: 9, name: 'Product 9', price: 900 },
  //   { id: 10, name: 'Product 10', price: 1000 },
  // ];
  products: ProductResponse[] =[];
  categories!: CategoryResponse[];

  currentPage: number = 0;
  itemsPerPage: number = 12;
  pages: number[] = [];
  totalPages: number = 0;
  visiblePages: number[] = [];

  keywordsearch: string;
  categoryID!: number;
  categoryName!: string;

  constructor(private router: Router,
              private activatedRoute:ActivatedRoute,
              private productService: ProductService,
              private categoryService: CategoryService,
              private cartService: CartService,
              private storageService: StorageService,
              private messageService: MessageService,
              private renderer: Renderer2, private el: ElementRef
              ) { 
      this.keywordsearch = '';
  }
  ngOnInit(){
    Aos.init();
    this.getCategories().then(() => {
      this.activatedRoute.queryParams.subscribe(params => {
        this.keywordsearch = '';
        this.currentPage = 0;
        this.categoryID = params['category'];
        this.categoryName = this.categories.find(cate => cate.id == this.categoryID)?.name || '';
        this.getProducts(this.keywordsearch, this.categoryID, this.currentPage, this.itemsPerPage);
        console.log(this.categoryID);
        console.log(this.categories);
      });
    });
    // localStorage.removeItem('access_token');
  }

  //lấy sản phẩm
  getProducts(keyword:string, categoryid:number, page: number, limit: number){
    this.productService.getProducts(keyword, categoryid, page, limit).subscribe({
      next: (response : any) => {
        response.products.forEach((product: ProductResponse) => {
          product.image =  `${environment.apiBaseUrl}/products/images/${product.image}`
        });
        this.products = response.products;
        this.totalPages = response.totalPages;
        this.visiblePages = this.generateVisiblePageArray(this.currentPage, this.totalPages);
      },
      complete: () => {
        // debugger
      },
      error: (error) => {
        debugger
        console.log(error);
      }
    });
  }
  //chuyển trang
  onPageChange(page: number){
    this.currentPage = page;
    this.getProducts(this.keywordsearch,this.categoryID,this.currentPage-1, this.itemsPerPage);
    console.log(this.categoryID);
    console.log(this.currentPage);
    console.log(this.itemsPerPage);
  }
  //tạo mảng số trang
  generateVisiblePageArray(currentPage: number, totalPages: number): number[]{
    const MaxVisiblePages = 5;
    const halfVisiblePages = Math.floor(MaxVisiblePages / 2);
    let startPage = Math.max(currentPage - halfVisiblePages, 0);
    let endPage = Math.min(startPage + MaxVisiblePages - 1, totalPages-1);
    if(endPage - startPage + 1 < MaxVisiblePages){
      startPage = Math.max(endPage - MaxVisiblePages + 1, 0);
    }
    return new Array(endPage - startPage + 1).fill(0).map((_, index) => startPage + index+1);
  }
  //lấy danh mục
  async getCategories() {
    await this.categoryService.getCategories().toPromise().then(res => {
      if (res) {
        this.categories = res; // Gán dữ liệu categories từ response
      } else {
        console.error('Response is undefined');
      }
    }).catch(error => {
      console.error('Error fetching categories: ', error);
    });
  }

  //lấy id từ category.component.ts
  onCategoryChange(selectedCategoryId: number){
    debugger
    this.categoryID = selectedCategoryId;
  }
  //tìm kiếm
  onSearch(){
    debugger
    this.currentPage = 0;
    this.getProducts(this.keywordsearch,this.categoryID,this.currentPage, this.itemsPerPage);
  }
  //nút mua ngay
  onBuyNow(productId: number){
    this.router.navigate(['/order'], { queryParams: { ids: productId } });
  }
  //nút thêm vào giỏ hàng(hiển thị thông báo là đã thêm vào giỏ hàng chứ không chuyển trang)
  onAddToCart(productId: number){
    if (this.storageService.isLoggedIn()) {
      this.cartService.addToCart(productId);
      this.showSuccess('Đã thêm vào giỏ hàng');
    }
    else {
      alert('Vui lòng đăng nhập để thêm vào giỏ hàng');
    }
  }

  // Hiển thị thông báo
  showSuccess(message: string) {
    this.messageService.add({severity:'success', summary: 'Success', detail: message});
  }
  showError(message: string) {
    this.messageService.add({severity:'error', summary: 'Error', detail: message});
  }

   // Hiển thị nút "Back to Top" khi người dùng cuộn xuống một khoảng cách cụ thể
  @HostListener('window:scroll', [])
  onWindowScroll() {
    let backToTop = document.querySelector('.back-to-top') as HTMLElement;
    if (document.body.scrollTop > 500 || document.documentElement.scrollTop > 500) {
      if (backToTop) backToTop.style.display = 'block';
    } else {
      if (backToTop) backToTop.style.display = 'none';
    }
  };
  // backToTop
  scrollToTop(){
    document.body.scrollTop = 0;
    document.documentElement.scrollTop = 0;
  };

    //phục vụ css home-cate
    ngAfterViewInit() {
      window.addEventListener('scroll', this.scrollEvent, true);
    }
  
    ngOnDestroy() {
      window.removeEventListener('scroll', this.scrollEvent, true);
    }
  
    scrollEvent = (event: any): void => {
      const number = window.pageYOffset || document.documentElement.scrollTop || document.body.scrollTop || 0;
      if (number > 85) {
        // this.renderer.setStyle(this.el.nativeElement.querySelector('.style-home-cate'), 'border-top-left-radius', '0');
        // this.renderer.setStyle(this.el.nativeElement.querySelector('.style-home-cate'), 'border-top-right-radius', '0');
      }
      else{
        // this.renderer.setStyle(this.el.nativeElement.querySelector('.style-home-cate'), 'border-radius', '5px');
      }
    };


  // currentTime:GLfloat =30;
  // @ViewChild('myVideo')myVideo!: ElementRef;
  // onHover(){
  //   // this.currentTime = 20;
  //   const videoElement = this.myVideo.nativeElement;
  //     // phát video từ thời gian currentTime
  //     videoElement.currentTime = this.currentTime;
  //     videoElement.play();
  //     // cập nhật value của videoSeekBar theo thời gian thực của video
  //     videoElement.ontimeupdate = () => {
  //       this.currentTime = videoElement.currentTime;
  //       this.videoSeekBar.nativeElement.value = this.currentTime;
  //     }
  // }
  // stopVideo(){
  //   const videoElement = document.getElementById('myVideo') as HTMLVideoElement;
  //   if (videoElement) {
  //     this.currentTime = videoElement.currentTime;
  //     videoElement.pause();
  //   }
  // }
  // @ViewChild('videoSeekBar') videoSeekBar!: ElementRef;
  // onSeek(){
  //   this.currentTime = this.videoSeekBar.nativeElement.value;
  //   this.myVideo.nativeElement.currentTime = this.currentTime;
  // }
  // ngAfterViewInit(){
  //   const videoElement = this.myVideo.nativeElement;
  //   videoElement.muted = true; // chính sách của trình duyệt không cho phép tự động phát video bắt buộc phải mute thì mới tự động phát được
  //   videoElement.onloadedmetadata = () => {
  //     this.videoSeekBar.nativeElement.max = videoElement.duration;
  //   };
  //   this.videoSeekBar.nativeElement.value = this.currentTime;
  // }
}
