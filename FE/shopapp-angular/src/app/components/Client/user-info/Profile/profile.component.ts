import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { UserService } from '../../../../services/user.service';
import { UserResponse } from '../../../../responses/user/user.response';
import { TokenService } from '../../../../services/token.service';
import { MessageService } from 'primeng/api';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { UpdateDTO } from '../../../../dtos/user/update.dto';
// import { MessageService } from 'primeng/api';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.scss',
  providers: [MessageService]
})
export class ProfileComponent implements OnInit{
  constructor(
              private router:Router,
              private userService:UserService,
              private messageService : MessageService,
              private tokenService:TokenService,
  ) {}
  userId!:number;
  token!:string;
  userDetails!:UserResponse;
  formUserDetails:any;
  ngOnInit() {
    this.getUserDetails();
    this.formUserDetails ={
      fullname:this.userDetails?.full_name,
      phone_number:this.userDetails?.phone_number,
      email:this.userDetails?.email,
      address:this.userDetails?.address,
      date_of_birth: this.userDetails?.date_of_birth,
      facebook_id:this.userDetails?.facebook_id,
      google_id:this.userDetails?.google_id,
      is_active:this.userDetails?.is_active,
    };
  }
  
  getUserDetails(){
    this.userId = this.tokenService.getUserId();
    this.token = this.tokenService.getToken()!;
    this.userDetails = this.userService.getUserDetailFromLocalStorage();
  }
  saveUser(){
    debugger
    // lưu thông tin user mới xác thực bằng token
    this.userService.updateUser(this.formUserDetails,this.token).subscribe({
      next:(res:any)=>{
        debugger
        this.tokenService.setToken(res.token);
        this.userService.getUserDetail(this.userId).subscribe({
          next:(res:any)=>{
            this.userDetails = res;
            this.userService.saveUserDetailToLocalStogare(this.userDetails);
          }
        })
        this.showSuccess(res.message);
      },
      error:(err:HttpErrorResponse)=>{
        debugger
        this.showError(err.error.message);
      }
    });
  }

  // // thông báo lỗi
  showSuccess(text: string) {
    this.messageService.add({severity:'success', summary: 'Success', detail: text});
  }
  showError(text: string) {
    this.messageService.add({severity:'error', summary: 'Error', detail: text});
  }

  // showWarn(text : string) {
  //   this.messageService.add({severity:'warn', summary: 'Warn', detail: text});
  // }

}
