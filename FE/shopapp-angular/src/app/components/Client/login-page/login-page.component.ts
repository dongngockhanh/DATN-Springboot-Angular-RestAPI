import { Component, OnInit,Inject, ViewChild, EventEmitter } from '@angular/core';
import { SocialAuthService, GoogleLoginProvider, SocialUser, FacebookLoginProvider } from '@abacritt/angularx-social-login';
import { FormsModule, NgForm, NgModel } from '@angular/forms';
import { HttpClient,HttpHeaders } from '@angular/common/http';
import { Router } from '@angular/router';
import { UserService } from '../../../services/user.service';
import { RegisterDTO } from '../../../dtos/user/register.dto';
import { LoginDTO } from '../../../dtos/user/login.dto';
import { LoginResponse } from '../../../responses/user/login.response';
import { TokenService } from '../../../services/token.service';
import { UserResponse } from '../../../responses/user/user.response';
import { CartService } from '../../../services/cart.service';
import { HeaderComponent } from '../header/header.component';
import { MessageService } from 'primeng/api';
// import { MessageService } from 'primeng/api';


@Component({
  selector: 'app-login-page',
  templateUrl: './login-page.component.html',
  styleUrl: './login-page.component.scss',
  providers: [MessageService]
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
  userResponse?: UserResponse;

  constructor(
              private messageService:MessageService,
              private socialAuthService: SocialAuthService,
              private userService: UserService,
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
    this.userService.register(registerDTO).subscribe({
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
roleadmin:number = 2;
login(){
  const loginDTO:LoginDTO = {
    "phone_number":this.phoneLogin,
    "password":this.passwordLogin
  };
  this.userService.login(loginDTO).subscribe({
    next:(response:LoginResponse) => {
      debugger
      const {token} = response;
      this.tokenService.setToken(token);
      const userId = this.tokenService.getUserId();
      this.userService.getUserDetail(userId).subscribe({
        next:(response:any) => {
          debugger
          this.userResponse = {
            ...response,
            date_of_birth: new Date(response.date_of_birth)
          }
          this.userService.saveUserDetailToLocalStogare(this.userResponse);
          if(response.role_id.id === this.roleadmin){
            this.router.navigate(['/admin']);
          }else{
            this.router.navigate(['/']).then(() => {
              window.location.reload();
            });
          }
        },
        error:(error:any)=>{
          debugger
        }
      })
    },
    complete:()=>{
      this.showSuccess("Đăng nhập thành công");
    },
    error:(error:any)=>{
      debugger
      this.showError(error.error.message);
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

  // message
  showSuccess(message:string){
    this.messageService.add({severity:'success', summary:'Success', detail:message});
  }
  showError(message:string){
    this.messageService.add({severity:'error', summary:'Error', detail:message});
  }

}
