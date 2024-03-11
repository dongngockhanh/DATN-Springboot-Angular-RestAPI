import { Component, OnInit } from '@angular/core';
import { SocialAuthService, GoogleLoginProvider, SocialUser, FacebookLoginProvider } from '@abacritt/angularx-social-login';


@Component({
  selector: 'app-login-page',
  templateUrl: './login-page.component.html',
  styleUrl: './login-page.component.scss',
})
export class LoginPageComponent implements OnInit {
  isLoginFormVisible = true;
  // Sử dụng khóa trang web này trong mã HTML mà trang web của bạn phân phối cho người dùng.
  siteKey = '6LfAt5MpAAAAAA4Y8lV1K5-qSxlWLhEb9NLLtEWT';
  // Sử dụng khóa bí mật này trong mã server của bạn.
  secretKey = '6LfAt5MpAAAAAPQ_hKnpa8PPqw1ji7haFzFctN0s';

  //đăng nhập gg xong thì lấy thông tin user
  ngOnInit(): void {
    this.socialAuthService.authState.subscribe((user) => {
      console.log(user);
    });
  }

  constructor(
    private socialAuthService: SocialAuthService) { }


  //đăng nhập gggole sử dụng thư viện angularx-social-login
  // signInWithGoogle(): void{
  //   this.socialAuthService.signIn(GoogleLoginProvider.PROVIDER_ID);
  // }


  //đăng nhập fb
  loginWithFacebook(): void {
    this.socialAuthService.signIn(FacebookLoginProvider.PROVIDER_ID);
  }

  //đang xuất
  signOut(): void {
    this.socialAuthService.signOut();
  }

}
