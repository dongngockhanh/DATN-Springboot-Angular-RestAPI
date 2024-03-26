import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './components/home/home.component';
import { LoginPageComponent } from './components/login-page/login-page.component';
import { DetailProductComponent } from './components/detail-product/detail-product.component';
import { OrderComponent } from './components/order/order.component';
import { CartComponent } from './components/cart/cart.component';

const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'login-page', component: LoginPageComponent },
  { path: 'cart', component: CartComponent },
  { path: 'detail-product', component: DetailProductComponent},
  { path: 'order', component: OrderComponent },
  
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
