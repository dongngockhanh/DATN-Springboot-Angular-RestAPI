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
import { ReactiveFormsModule } from '@angular/forms';
import { SocialLoginModule,SocialAuthServiceConfig, GoogleLoginProvider,GoogleSigninButtonModule, FacebookLoginProvider } from '@abacritt/angularx-social-login';

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
    RecaptchaModule,
    SocialLoginModule,
    ReactiveFormsModule,
    GoogleSigninButtonModule
  ],
  providers: [
    {
    provide: 'SocialAuthServiceConfig',
    useValue: {
      autoLogin: false,
      providers: [
        {
          id: GoogleLoginProvider.PROVIDER_ID,
          provider: new GoogleLoginProvider('152090975014-7dk8r10ffnnb3d5ttqo3p66u5t2qk314.apps.googleusercontent.com'),
        },
        {
          id: FacebookLoginProvider.PROVIDER_ID,
          provider: new FacebookLoginProvider('1419501698661103')
        }
      ]
    } as SocialAuthServiceConfig,
  }
  ],
  bootstrap: [
    AppComponent
    // HomeComponent
  ]
})
export class AppModule { }
