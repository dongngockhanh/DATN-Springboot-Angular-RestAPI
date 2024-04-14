import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { HomeComponent } from './components/Client/home/home.component';
import { HeaderComponent } from './components/Client/header/header.component';
import { FooterComponent } from './components/Client/footer/footer.component';
import { DetailProductComponent } from './components/Client/detail-product/detail-product.component';
import { AppComponent } from './app.component';
import { OrderComponent } from './components/Client/order/order.component';
import { CartComponent } from './components/Client/cart/cart.component';
import { FormsModule } from '@angular/forms';
import { HttpClientModule,HTTP_INTERCEPTORS } from '@angular/common/http';
import { LoginPageComponent } from './components/Client/login-page/login-page.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RecaptchaModule} from 'ng-recaptcha';
// import { AppComponent } from './app.component';
import { ReactiveFormsModule } from '@angular/forms';
import { GoogleLoginProvider, GoogleSigninButtonModule, FacebookLoginProvider } from '@abacritt/angularx-social-login';
import { TokenInterceptor } from './interceptors/token.interceptor';
import { LOCALE_ID } from '@angular/core';
import { registerLocaleData } from '@angular/common';
import localeVi from '@angular/common/locales/vi';
import { SocialAuthServiceConfig, SocialLoginModule } from 'angularx-social-login';

registerLocaleData(localeVi, 'vi');
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatInputModule } from '@angular/material/input';
import { DashboardComponent } from './components/Admin/dashboard/dashboard.component';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

@NgModule({
  declarations: [
    AppComponent,
  
    HomeComponent,
    HeaderComponent,
    FooterComponent,
    DetailProductComponent,
    OrderComponent,
    CartComponent,
    LoginPageComponent,
    DashboardComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    BrowserAnimationsModule,
    RecaptchaModule,
    SocialLoginModule,
    GoogleSigninButtonModule,
    MatFormFieldModule,
    MatAutocompleteModule,
    MatInputModule,
    ReactiveFormsModule,  
    BrowserAnimationsModule,
    FontAwesomeModule
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
  },
  {
    provide: HTTP_INTERCEPTORS,
    useClass: TokenInterceptor,
    multi: true
  },
  {
    provide: LOCALE_ID,
    useValue: 'vi-VN' // hoặc 'vi'
  }
  ],
  bootstrap: [
    AppComponent
    // HomeComponent

  ]
})
export class AppModule { }
