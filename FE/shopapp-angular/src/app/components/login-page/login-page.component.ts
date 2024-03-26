import { Component, OnInit,Inject, ViewChild } from '@angular/core';
import { SocialAuthService, GoogleLoginProvider, SocialUser, FacebookLoginProvider } from '@abacritt/angularx-social-login';
import { FormsModule, NgForm } from '@angular/forms';
import { HttpClient,HttpHeaders } from '@angular/common/http';
import { Router } from '@angular/router';
import { UserService } from '../../services/user.service';
import { RegisterDTO } from '../../dtos/user/register.dto';
import { LoginDTO } from '../../dtos/user/login.dto';
import { LoginResponse } from '../../responses/user/login.response';
import { TokenService } from '../../services/token.service';


@Component({
  selector: 'app-login-page',
  templateUrl: './login-page.component.html',
  styleUrl: './login-page.component.scss',
})
export class LoginPageComponent implements OnInit {
  // siteKey = '6LfAt5MpAAAAAA4Y8lV1K5-qSxlWLhEb9NLLtEWT'; //khóa trang web này trong mã HTML mà trang web của bạn phân phối cho người dùng.
  // secretKey = '6LfAt5MpAAAAAPQ_hKnpa8PPqw1ji7haFzFctN0s';   // Sử dụng khóa bí mật này trong mã server của bạn.
  //đăng nhập gg xong thì lấy thông tin user
  ngOnInit(): void {
    this.socialAuthService.authState.subscribe((user) => {
      console.log(user);
    });
  }

  isLoginFormVisible = true; // hiển thị form đăng nhập
  @ViewChild('validateForm') validateForm!:NgForm;// validate form 
  checkRetypepasswordMatch(){
    if(this.passwordRegister !== this.retypePasswordRegister){
      this.validateForm.form.controls['retypePasswordRegister'].setErrors({'passwordNotMatch':true});
    }else{
      this.validateForm.form.controls['retypePasswordRegister'].setErrors(null);
    }
  }

  fullname: string ;
  phoneRegister: string ;
  passwordRegister: string ;
  retypePasswordRegister: string ;

  phoneLogin: string ;
  passwordLogin: string ;

  constructor(private socialAuthService: SocialAuthService,
              private UserService: UserService,
              private router: Router,
              private tokenService: TokenService){
    this.fullname = '';
    this.phoneRegister = '';
    this.passwordRegister = '';
    this.retypePasswordRegister = '';

    this.phoneLogin = '';
    this.passwordLogin = '';
  }


  // đăng ký sử dụng tài khoản và mật khẩu
  register(){
    const registerDTO:RegisterDTO = {
      "fullname":this.fullname,
      "phone_number":this.phoneRegister,
      "email":"",
      "password":this.passwordRegister,
      "retype_password":this.retypePasswordRegister,
      "address":"",
      "date_of_birth":null,
      "facebook_id":0,
      "google_id":0,
      "role_id":1    
    };
    this.UserService.register(registerDTO).subscribe({
      next:(response:any) => {
        debugger
          // this.router.navigate(['/login']);
      },
      complete: ()=> {
        debugger
      },
      error:(error:any)=>{
        debugger
        if(error.status === 200)
          alert(error.error.text);
        else
        alert(`đăng ký không thành công : ${error.error}`);
      }
    });
  }

// đăng nhập sử dụng tài khoản và mật khẩu
login(){
  const loginDTO:LoginDTO = {
    "phone_number":this.phoneLogin,
    "password":this.passwordLogin
  };
  this.UserService.login(loginDTO).subscribe({
    next:(response:LoginResponse) => {
      debugger
      const {token} = response;
      this.tokenService.setToken(token);
        // this.router.navigate(['/']);
    },
    complete: ()=> {
      debugger
    },
    error:(error:any)=>{
      debugger
      if(error.status === 200)
        alert(error.error.text);
      else
      alert(`đăng nhập không thành công : ${error.error}`);
    }
  });
}






//đăng nhập sử dụng social account

  //đăng nhập gggole sử dụng thư viện angularx-social-login
  // signInWithGoogle(): void{
  //   this.socialAuthService.signIn(GoogleLoginProvider.PROVIDER_ID);
  // }

  //đăng nhập fb
  signInWithFB(): void {
    this.socialAuthService.signIn(FacebookLoginProvider.PROVIDER_ID);
  }
  //đang xuất
  signOut(): void {
    this.socialAuthService.signOut();
  }

}
