import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { HomeComponent } from './home/home.component';
import { HeaderComponent } from './header/header.component';
import { FooterComponent } from './footer/footer.component';
import { DetailProductComponent } from './detail-product/detail-product.component';
import { AppComponent } from './app.component';
import { OrderComponent } from './order/order.component';
// import { AppComponent } from './app.component';

@NgModule({
  declarations: [
    // AppComponent
  
    HomeComponent,
    HeaderComponent,
    FooterComponent,
    DetailProductComponent,
    AppComponent,
    OrderComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule
  ],
  providers: [],
   bootstrap: [
    AppComponent
    // HomeComponent
  ]
})
export class AppModule { }
