import { Component, ElementRef, HostListener, OnInit, Renderer2, ViewChild } from '@angular/core';
import { NavigationStart, Router } from '@angular/router';
import { DarkModeService } from 'angular-dark-mode';
import { UserService } from '../../../services/user.service';
import { UserResponse } from '../../../responses/user/user.response';
import { Subscription } from 'rxjs';
import { CategoryService } from '../../../services/categoryService/category.service';
import { CategoryResponse } from '../../../responses/categories/category.response';
import { HomeComponent } from '../home/home.component';
import { CartService } from '../../../services/cart.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrl: './header.component.scss'
})
export class HeaderComponent implements OnInit{
  showUserOptions:boolean = false;
  // dark mode
  darkMode$ = this.darkModeService.darkMode$;
  onToggle(): void {
    this.darkModeService.toggle();
  }
  //css active link router

  userResponse?: UserResponse | null;
  isPopoverOpen: boolean = false; // Declare the isPopoverOpen property
  constructor(
    private darkModeService: DarkModeService,
    private router: Router,
    private userService: UserService,
    private categoryService: CategoryService,
    private cartService: CartService,
    //for css
    private renderer: Renderer2, private el: ElementRef
  ) {}


  ngOnInit() {
      this.getCartQuantity();
      this.getCategories();
      this.userResponse = this.userService.getUserDetailFromLocalStorage();
  }
  // isActive = false;
  togglePopover(event: Event): void {
    event.preventDefault();
    // this.isActive = !this.isActive;
    this.isPopoverOpen = !this.isPopoverOpen;
  }
  logout() {
      this.userService.logout();
      this.router.navigate(['/']).then(() => {
          window.location.reload();
      });
  }

  // lấy thông báo
  listNotification:any[]=[
    "cập nhật đơn hàng",
    "cập nhật đơn hàng",
    "cập nhập đơn hàng"
  ];
  getNotification(){

  }
  // lấy danh sách danh mục
  categoryResponse: CategoryResponse[] = [];
  getCategories() {
    this.categoryService.getCategories().subscribe((res) => {
      this.categoryResponse = res;
    });
  }
  // lấy số lượng trong giỏ hàng
  getCartQuantity() {
  let cartQuantity = 0;
  const cart = this.cartService.getCart();
  cart.forEach((quantity:number) => {
    cartQuantity += quantity;
  })
  return cartQuantity;
  }

  // lấy id danh mục
  // @ViewChild(HomeComponent) HomeComponent: any;
  // onLoadPage(id:number){
  //   console.log(
  //   this.HomeComponent.getProducts("",id,0,0))
  // }
  // tải lại trang chủ với id danh mục
  onLoadPage(id: number) {
    this.router.navigate([''], { queryParams: { category: id } });
  }
  
  //phục vụ css
  showDropdown: boolean = false;
  @ViewChild('dropdownContent') dropdownContent: any;

  toggleDropdown() {
    this.showDropdown = !this.showDropdown;
  }
  clickToActive(event : any){
    let data = document.querySelectorAll('.nav-item .nav-link');
    data.forEach(i =>{
      i.classList.remove('active');
    })
    event.target.classList.toggle("active");
}
  ngAfterViewInit() {
    window.addEventListener('scroll', this.scrollEvent, true);
  }

  ngOnDestroy() {
    window.removeEventListener('scroll', this.scrollEvent, true);
  }

  scrollEvent = (event: any): void => {
    const number = window.pageYOffset || document.documentElement.scrollTop || document.body.scrollTop || 0;
    if (number > 80) {
      // this.renderer.setStyle(this.el.nativeElement.querySelector('.style-category-container'), 'border-bottom-left-radius', '0');
      // this.renderer.setStyle(this.el.nativeElement.querySelector('.style-category-container'), 'border-bottom-right-radius', '0');
    }
    else{
      // this.renderer.setStyle(this.el.nativeElement.querySelector('.style-category-container'), 'border-radius', '5px');
    }
  };
  // ngAfterViewInit(){
  //   this.renderer.listen(window,'scroll',()=>{
  //     const element = this.el.nativeElement.querySelector('.change-background-color');
  //     const element_child = this.el.nativeElement.querySelector('change-border-radius')
  //     const bounding = element.getBoundingClientRect();
  //     if(bounding.top<=100){
  //       // alert("faaaaaaaa")
  //       this.renderer.setStyle(element_child,'border-radius','5px');
  //       this.renderer.setStyle(element_child,'border-top-right-radius','5px');
  //     }
  //   })
  // }
}
