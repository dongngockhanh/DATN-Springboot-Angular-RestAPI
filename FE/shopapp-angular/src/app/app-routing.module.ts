import { Component, NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './components/Client/home/home.component';
import { LoginPageComponent } from './components/Client/login-page/login-page.component';
import { DetailProductComponent } from './components/Client/detail-product/detail-product.component';
import { OrderComponent } from './components/Client/order/order.component';
import { CartComponent } from './components/Client/cart/cart.component';
import { DashboardComponent } from './components/Admin/dashboard/dashboard.component';
import { AuthGuard } from './services/auth-guard.service';
import { combineLatest } from 'rxjs';
import { ProfileComponent } from './components/Client/user-info/Profile/profile.component';
import { SecurityComponent } from './components/Client/user-info/Security/security.component';
import { ControlUserComponent } from './components/Client/user-info/control-user/control-user.component';
import { OrderStatusComponent } from './components/Client/user-info/order-status/order-status.component';
import { OrderDetailComponent } from './components/Client/user-info/order-detail/order-detail.component';
import { CategoryComponent } from './components/Admin/category/category.component';
import { ProductComponent } from './components/Admin/product/product.component';
import { OrderAdminComponent } from './components/Admin/order-admin/order-admin.component';
import { AccountComponent } from './components/Admin/account/account.component';
import { StatisticsComponent } from './components/Admin/statistics/statistics.component';
import { BillingComponent } from './components/Client/user-info/billing/billing.component';
import { RoleGuardService } from './services/role-guard.service';
import { PaymentReturnComponent } from './components/Client/order/payment/payment-return/payment-return.component';
import { AddAccountComponent } from './components/Admin/add-account/add-account.component';
// import { CategoryComponent } from './components/Admin/category/category.component';
// import { ProductComponent } from './components/Admin/product/product.component';
// import { TagComponent } from './components/Admin/tag/tag.component';
// import { DashboardComponent } from './components/Admin/dashboard/dashboard.component';
const routes: Routes = [
  {
    path: 'admin',component:DashboardComponent, canActivate : [RoleGuardService],data: {expectedRole: "admin"},
    children:[
      {path:'account',component:AccountComponent},
      {path:"category",component: CategoryComponent},
      {path:'product',component:ProductComponent},
      {path:'order-admin',component:OrderAdminComponent},
      {path:'profile',component:ProfileComponent},
      {path:'statistics',component:StatisticsComponent},
      {path:'addacc',component:AddAccountComponent}
    ]
  },
  { path: '', component: HomeComponent },
  { path: 'login-page', component: LoginPageComponent },
  { path: 'cart', component: CartComponent,canActivate:[AuthGuard]},
  { path: 'detail-product/:id', component: DetailProductComponent},
  { path: 'order', component: OrderComponent ,canActivate: [AuthGuard]},
  { path: 'payment-return',component:PaymentReturnComponent},
  { path: 'user', component: ControlUserComponent,canActivate: [AuthGuard],
    children:[
      {path:'profile',component:ProfileComponent},
      {path:'security',component:SecurityComponent},
      {path:'billing',component:BillingComponent},
      {path:'order-status',component:OrderStatusComponent},
      {path:'order-detail/:id',component:OrderDetailComponent}
    ]
  },
  // { path: 'admin', component: DashboardComponent }
  
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
