import { Component,OnInit } from '@angular/core';
import { ReCaptchaV3Service, RecaptchaErrorParameters } from 'ng-recaptcha';

@Component({
  selector: 'app-login-page',
  templateUrl: './login-page.component.html',
  styleUrl: './login-page.component.scss',
})
export class LoginPageComponent{
  isLoginFormVisible = true;
  // Sử dụng khóa trang web này trong mã HTML mà trang web của bạn phân phối cho người dùng.
  siteKey = '6LfAt5MpAAAAAA4Y8lV1K5-qSxlWLhEb9NLLtEWT';
  // Sử dụng khóa bí mật này trong mã server của bạn.
  secretKey='6LfAt5MpAAAAAPQ_hKnpa8PPqw1ji7haFzFctN0s';
}
