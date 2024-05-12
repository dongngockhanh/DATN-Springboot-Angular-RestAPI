import { Component, OnInit } from '@angular/core';
import { UserService } from '../../../services/user.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-account',
  templateUrl: './account.component.html',
  styleUrls: ['./account.component.css']
})
export class AccountComponent implements OnInit{
  listUser: any;
  constructor(private userService: UserService) {}
  ngOnInit(){
    this.getListUser();
  }
  test()
  {
    console.log(this.listUser)
  }
  getListUser() {
    this.userService.getListUser().subscribe({
      next: res => {
        debugger;
        this.listUser = res;
        // for(let i = 0; i < this.listUser.length; i++){
        //   this.listUser[i].is_active = false;
        // }
      },
      error: err => {
        console.log(err);
      }
    });
  }
  toggleActive(user:any){
    this.userService.setActiveUser(user.user_id).subscribe({
      next:(res:any)=>{
        this.getListUser();
      },
      error:(err:any)=>{
        console.log(err);
      }
    })
  }
}
