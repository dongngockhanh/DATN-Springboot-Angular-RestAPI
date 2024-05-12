import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { MessageService } from 'primeng/api';
import { TokenService } from '../../../../services/token.service';
import { UserService } from '../../../../services/user.service';

@Component({
  selector: 'app-security',
  templateUrl: './security.component.html',
  styleUrl: './security.component.scss',
  providers: [MessageService]
})
export class SecurityComponent implements OnInit {
  constructor(
    private router:Router,
    private messageService : MessageService,
    private tokenService:TokenService,
    private userService:UserService
  ) {}
  ngOnInit(){
    this.token = this.tokenService.getToken()!;
    this.userId = this.tokenService.getUserId();
    console.log(this.userId);
  }
  userId!:number;
  token!:string;
  oldPassword!: string;
  newPassword!: string;
  retypePassword!: string;
  test(){
    alert(this.oldPassword+" "+this.newPassword+" "+this.retypePassword);
  }
  savePassword(){
    if(this.newPassword !== this.retypePassword){
      this.showError("Mật khẩu mới không trùng khớp");
    }else{
      if(this.oldPassword == undefined || this.newPassword == undefined || this.retypePassword == undefined){
        this.showError("Vui lòng nhập đầy đủ thông tin");
        return;
      }
      this.userService.updatePassword(this.oldPassword,this.newPassword,this.token).subscribe({
        next:(res:any)=>{
          this.showSuccess(res.message);
        },
        error:(err:any)=>{
          console.log(err);
          this.showError(err.error.message);
        }
      })
    }
  }
  deleteAccount(){
    if (this.userInput === this.randomCode) {
      this.userService.setActiveUser(this.userId).subscribe({
        next:(res:any)=>{
            this.userService.logout();
            window.location.reload();
        },
        error:(err:any)=>{
          this.showError(err.error.message);
        }
      });
    } else {
      this.showError("Mã xác nhận không chính xác")
      this.randomCode = Math.random().toString(36).substring(7);
    }
  }

  // show dialog
  displayDialog: boolean = false;
  randomCode:string = '';
  userInput: string = '';
  showDialog() {
    this.randomCode = Math.random().toString(36).substring(7);
    this.displayDialog = true;
  }
  // message
  showSuccess(message: string) {
    this.messageService.add({severity:'success', summary: 'Success', detail: message});
  }
  showError(message: string){
    this.messageService.add({severity:'error', summary: 'Error', detail: message});
  }
}
