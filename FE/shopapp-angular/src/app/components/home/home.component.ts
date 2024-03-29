import { Component, HostListener, OnInit } from '@angular/core';
import { ProductResponse } from '../../responses/products/product.response';
import { ProductService } from '../../services/productService/product.service';
import { environment } from '../../environments/environment';
import Aos from 'aos';
import { CategoryResponse } from '../../responses/categories/category.response';
import { CategoryService } from '../../services/categoryService/category.service';


@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss',
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
  categories: CategoryResponse[] = [];

  currentPage: number = 0;
  itemsPerPage: number = 12;
  pages: number[] = [];
  totalPages: number = 0;
  visiblePages: number[] = [];

  keywordsearch: string ;
  categoryID: number = 0;

  constructor(private productService: ProductService,
              private categoryService: CategoryService) { 
    this.keywordsearch = '';
  }
  ngOnInit(){
    Aos.init();
    this.getCategories();
    this.getProducts(this.keywordsearch,this.categoryID,this.currentPage,this.itemsPerPage);
  }
  //lấy sản phẩm
  getProducts(keyword:string, categoryid:number, page: number, limit: number){
    this.productService.getProducts(keyword, categoryid, page, limit).subscribe({
      next: (response : any) => {
        debugger
        response.products.forEach((product: ProductResponse) => {
          product.image =  `${environment.apiBaseUrl}/products/images/${product.image}`
        });
        this.products = response.products;
        this.totalPages = response.totalPages;
        this.visiblePages = this.generateVisiblePageArray(this.currentPage, this.totalPages);
      },
      complete: () => {
        debugger
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
  getCategories(){
    this.categoryService.getCategories().subscribe({
      next: (response: any) => {
        debugger
        
        this.categories = response;

      },
      complete: () => {
        debugger
      },
      error: (error) => {
        debugger
        console.log(error);
      }
    })
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
}
