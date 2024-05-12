import { Component, EventEmitter, OnInit } from '@angular/core';
import { ConfirmationService, MessageService } from 'primeng/api';
import { ProductService } from '../../../services/productService/product.service';
import { CategoryService } from '../../../services/categoryService/category.service';
import { environment } from '../../../environments/environment';
import { hide } from '@popperjs/core';
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';
import { Observable } from 'rxjs';
import { IsNumber } from 'class-validator';
import { ProductImage } from '../../../responses/products/product-image.response';
import { ProductResponse } from '../../../responses/products/product.response';


@Component({
  selector: 'app-product',
  templateUrl: './product.component.html',
  styleUrls: ['./product.component.css'],
  providers: [MessageService,ConfirmationService]

})
export class ProductComponent implements OnInit {

  listProduct: any;
  listCategory: any;

  disabled : boolean = true;//disable button xoá hình ảnh

  selectedFiles: File[] = [];//chứa hình ảnh tải lên

  imageChoosen : any;//chứa hình ảnh được chọn

  onUpdate : boolean =false;//kiểm tra cập nhật hay thêm mới
  showForm : boolean = false;//show form thêm mới hoặc cập nhật
  showImage: boolean = false;//show form chọn hình ảnh
  showDelete: boolean = false;//show dialog xoá sản phẩm

  productForm: any ={
    id:1,
    name : null,
    description : null,
    price: null,
    quantity: null,
    categoryId: null,
    imageIds: []
  };

  constructor(private messageService: MessageService,
              private productService: ProductService,
              private sanitizer:DomSanitizer,
              private categoryService:CategoryService){

  }

  ngOnInit(): void {
    this.getListCategoryEnabled();
    this.getListProduct();
  }


  openNew() {
    this.onUpdate = false;
    this.showForm = true;
    this.productForm ={
      id:null,
      name: null,
      description : null,
      price: null,
      quantity: null,
      categoryId: null,
      imageIds: []
    }
  }

  openUpdate(data : any){
      this.onUpdate = true;
      this.showForm =true;
      this.productForm = {
        id:data.id,
        name: data.name,
        description : data.description,
        price: data.price,
        quantity: data.quantity,
        categoryId: data.category.id,
      }
      this.getImageProduct();
  }
  onChooseImage(){
    this.showImage =true;
    this.disabled = true;
    let data = document.querySelectorAll('.list-image img');
      data.forEach(i =>{
        i.classList.remove('choosen');
    })  
  }
  getListProduct(){
    debugger
    this.productService.getProducts("",0,0,10000).subscribe({
      next: (res: any) =>{
        debugger
        res.products.forEach((product: any) =>{
          product.imageUrl = `${environment.apiBaseUrl}/products/images/${product.image}`;
        })      
        this.listProduct =res.products;
        //tìm category bằng id
        for(let i = 0; i < this.listProduct.length; i++){
          for(let j = 0; j < this.listCategory.length; j++){
            if(this.listProduct[i].category_id == this.listCategory[j].id){
              this.listProduct[i].category = this.listCategory[j];
            }
          }
        }
        console.log(this.listProduct)
      },error: (err: any)=>{
        console.log(err);
      }
    })
  }
  getListCategoryEnabled(){
    this.categoryService.getCategories().subscribe({
      next: (res: any) =>{
        this.listCategory = res;
      },error : (err: any)=>{
        console.log(err);
      }
    })
  }
  createProduct(){
  let image:string='';
    const {name,price,description,categoryId} = this.productForm;
    console.log(this.productForm);
    this.productService.createProduct(name,price,image,description,categoryId).subscribe({
      next: (res: any) =>{
        this.productService.uploadImages(res.id,this.selectedFiles).subscribe({
          next: (res: any) =>{
            this.getListProduct();
            this.showForm = false;
            this.showSuccess("Thêm mới thành công");
          },error: (err: { message: string; }) =>{
            this.showError(err.message);
          }
        });
      },error: (err: { message: string; }) =>{
        this.showError(err.message);
      }
    })
  }
  updateProduct(){
    const product ={
      name: this.productForm.name,
      price: this.productForm.price,
      description: this.productForm.description,
      categoryId: this.productForm.categoryId,
    }
    this.productService.updateProduct(this.productForm.id, product.name,product.price,product.description,product.categoryId).subscribe({
      next: (res: any) =>{
        debugger
        this.getListProduct();
        this.showForm = false;
        this.showSuccess("Cập nhật thành công");
      },error: (err: { message: string; }) =>{
        debugger
        this.showError(err.message);
      }
    })
  }
  url:any[]=[];//chứa hình ảnh của sản phẩm trên server
  getImageProduct(){
    this.productService.getDetailProduct(this.productForm.id).subscribe({
      next:(response: any) => {
        //lấy ra các hình ảnh của sản phẩm và thay đổi đường dẫn
        if(response.product_images && response.product_images.length > 0)
        {
          response.product_images.forEach((product_image:ProductImage)=>{
            product_image.image_url = `${environment.apiBaseUrl}/products/images/${product_image.image_url}`;
          });
          this.url = response.product_images;
        }
      },
      error: (error) => {
        console.log(error);
      }
    });
  }
  onDelete(id: number,name: string){
    this.productForm.id = null;
    this.showDelete = true;
    this.productForm.id = id;
    this.productForm.name = name;
  }
  deleteProduct(){
    this.productService.deleteProduct(this.productForm.id).subscribe({
      next: (res: any) =>{
        this.getListProduct();
        this.showSuccess("Xóa thành công");
        this.showDelete = false;
      },error: (err: { message: string; }) =>{
        this.showError(err.message);
      }
    })
  }


// ==========================================================================================
  // hàm tải hình ảnh lên client
  showListImage:{file:File,url: SafeUrl}[]=[];//chứa dạng hình ảnh để hiển thị
  uploadFile(event:any) {
    if(event.target.files){
      const files = event.target.files;
      for(let i = 0;i < files.length;i++) {
        this.selectedFiles.push(files[i]);
        // nếu là tạo mới sản phẩm
        if(!this.onUpdate){
          const fileHandle = {
            file:files[i],
            url:this.sanitizer.bypassSecurityTrustUrl(
            window.URL.createObjectURL(files[i])
          )};
          this.showListImage.push(fileHandle);
        }
      }
      // nếu là cập nhật sản phẩm
      if(this.onUpdate){
        this.productService.uploadImages(this.productForm.id,this.selectedFiles).subscribe({
          next: (res: any) =>{
            this.showSuccess("Thêm hình ảnh thành công");
            this.getImageProduct();
          },error: (err: { message: string; }) =>{
            this.showError(err.message);
          }
        });
      }
    }
    this.showImage = true;
  }

  // xoá hình ảnh thêm mới và cập nhật sản phẩm
  remoteImage(){
    // nếu là thêm mới sản phẩm
    if(!this.onUpdate){
      this.selectedFiles = this.selectedFiles.filter(a=>a!==this.imageChoosen.file);
      this.showListImage = this.showListImage.filter(a=>a.file!==this.imageChoosen.file);
    }else{//nếu là cập nhật sản phẩm
      this.url = this.url.filter(a=>a!==this.imageChoosen);
      this.productService.deleteImage(this.imageChoosen.id).subscribe({
        next:(rest:any)=>{
          this.showSuccess("xoá hình ảnh thành công");
        }
      });
    }
  }

  // hàm chọn hình ảnh
  selectImage(event : any,res: any){
    let data = document.querySelectorAll('.list-image img');
    data.forEach(i =>{
      i.classList.remove('choosen');
    })
    event.target.classList.toggle("choosen");
    this.imageChoosen = res;
    this.disabled = false;
}













// ===================================================================================================
showSuccess(text: string) {
  this.messageService.add({severity:'success', summary: 'Success', detail: text});
}
showError(text: string) {
  this.messageService.add({severity:'error', summary: 'Error', detail: text});
}

showWarn(text : string) {
  this.messageService.add({severity:'warn', summary: 'Warn', detail: text});
}

}
