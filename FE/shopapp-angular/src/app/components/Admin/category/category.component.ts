import { Component, OnInit } from '@angular/core';
import { MessageService } from 'primeng/api';
import { CategoryService } from '../../../services/categoryService/category.service';
import { PrimeNGConfig } from 'primeng/api';

@Component({
  selector: 'app-category',
  templateUrl: './category.component.html',
  styleUrls: ['./category.component.css'],
  providers: [MessageService]

})

export class CategoryComponent implements OnInit {

  listCategory : any;

  displayForm: boolean = false;

  deleteForm : boolean = false;

  onUpdate : boolean = false;

  categoryForm : any ={
    id: null,
    name : null
  }

  constructor(
              private primengConfig: PrimeNGConfig,    
              private messageService : MessageService,
              private categoryService: CategoryService)
    {}

  ngOnInit(): void {
    this.getListCategory();
    this.primengConfig.ripple = true;
  }


  getListCategory(){
    this.categoryService.getCategories().subscribe({
      next: (res:any) =>{
        this.listCategory = res;
        console.log(res);
      },error: (err:any) =>{
        console.log(err);
      }
    })
  }

  showForm(){
    this.onUpdate = false;
    this.categoryForm ={
      id : null,
      name : null
    }
    this.displayForm = true;
  }

  
 onUpdateForm(id: number,name : string){
      this.onUpdate = true;
      this.displayForm =true;
      this.categoryForm.id = id;
      this.categoryForm.name = name;
  }

  onDelete(id:number,name : string){
    this.deleteForm = true;
    this.categoryForm.id = id;
    this.categoryForm.name = name;
  }

  createCategory(){
    const {name} = this.categoryForm;
    this.categoryService.createCategory(name).subscribe({
      next: (res:any) =>{
        this.getListCategory();
        this.showSuccess("Tạo danh mục thành công!");
        this.displayForm = false;
      },error: (err:any)=>{
        this.showError(err.message);
      }
    })
  }


  updateCategory(){
    const {id,name} = this.categoryForm;
    this.categoryService.updateCategory(id,name).subscribe({
      next: (res:any) =>{
        this.getListCategory();
        this.showSuccess("Cập nhật danh mục thành công!");
        this.displayForm = false;
      },error: (err:any) =>{
        this.showError(err.message);
      }
    })
  }


  enableCategory(id : number){
//     this.categoryService.enableCategory(id).subscribe({
//       next: (res:any) =>{
//         this.getListCategory();
        // this.showSuccess("Cập nhật thành công!!");
//       },error: (err:any)=>{
        // this.showError(err.message);
//       }
//     })
  }


  deleteCategory(){
    const {id} = this.categoryForm;
    this.categoryService.deleteCategory(id).subscribe({
      next: (res:any) =>{
        this.getListCategory();
        this.showSuccess("Xóa danh mục thành công!!");
        this.deleteForm = false;
      },error: (err:any)=>{
        this.showError(err.message);
      }
    })
  }

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
