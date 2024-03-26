import { Component, OnInit } from '@angular/core';
import { ProductResponse } from '../../responses/prodducts/product.response';
import { ProductService } from '../../services/productService/product.service';
import { environment } from '../../environments/environment';

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
  currentPage: number = 0;
  itemsPerPage: number = 12;
  pages: number[] = [];
  totalPages: number = 0;
  visiblePages: number[] = [];

  constructor(private productService: ProductService) { }
  ngOnInit(){
    this.getProducts(this.currentPage,this.itemsPerPage);
  }
  getProducts(page: number, limit: number){
    this.productService.getProducts(page, limit).subscribe({
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

  onPageChange(page: number){
    this.currentPage = page;
    this.getProducts(this.currentPage, this.itemsPerPage);
  }

  generateVisiblePageArray(currentPage: number, totalPages: number): number[]{
    const MaxVisiblePages = 5;
    const halfVisiblePages = Math.floor(MaxVisiblePages / 2);
    let startPage = Math.max(currentPage - halfVisiblePages, 1);
    let endPage = Math.min(startPage + MaxVisiblePages - 1, totalPages);
    if(endPage - startPage + 1 < MaxVisiblePages){
      startPage = Math.max(endPage - MaxVisiblePages + 1, 1);
    }
    return new Array(endPage - startPage + 1).fill(0).map((_, index) => startPage + index);
  }
}
