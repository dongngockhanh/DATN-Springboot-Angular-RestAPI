import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './components/Client/home/home.component';
import { LoginPageComponent } from './components/Client/login-page/login-page.component';
import { DetailProductComponent } from './components/Client/detail-product/detail-product.component';
import { OrderComponent } from './components/Client/order/order.component';
import { CartComponent } from './components/Client/cart/cart.component';
import { DashboardComponent } from './components/Admin/dashboard/dashboard.component';
// import { CategoryComponent } from './components/Admin/category/category.component';
// import { ProductComponent } from './components/Admin/product/product.component';
// import { TagComponent } from './components/Admin/tag/tag.component';
// import { DashboardComponent } from './components/Admin/dashboard/dashboard.component';
const routes: Routes = [
  {
    path: 'admin',component:DashboardComponent,
    // canActivate: [RoleGuardService],data: {expectedRole: "ROLE_ADMIN"},
    children:[
      // {path:"category",component: CategoryComponent},
      // {path:'product',component:ProductComponent},
      // {path:'order',component:OrderComponent},
      // {path:'tag',component:TagComponent}
    ]
  },
  { path: '', component: HomeComponent },
  { path: 'login-page', component: LoginPageComponent },
  { path: 'cart', component: CartComponent },
  { path: 'detail-product/:id', component: DetailProductComponent},
  { path: 'order', component: OrderComponent },
  // { path: 'admin', component: DashboardComponent }
  
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
