import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { HomeComponent } from './home/home.component';
import { HeaderComponent } from './header/header.component';
import { FooterComponent } from './footer/footer.component';
import { DetailProductComponent } from './detail-product/detail-product.component';
import { AppComponent } from './app.component';
import { OrderComponent } from './order/order.component';
import { CartComponent } from './cart/cart.component';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { LoginPageComponent } from './login-page/login-page.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RecaptchaModule} from 'ng-recaptcha';
// import { AppComponent } from './app.component';

@NgModule({
  declarations: [
    // AppComponent
  
    HomeComponent,
    HeaderComponent,
    FooterComponent,
    DetailProductComponent,
    AppComponent,
    OrderComponent,
    CartComponent,
    LoginPageComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    BrowserAnimationsModule,
    RecaptchaModule
  ],
  providers: [],
  bootstrap: [
    AppComponent
    // HomeComponent
  ]
})
export class AppModule { }
