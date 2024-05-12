import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router } from '@angular/router';
import { StorageService } from './storage.service';
import { UserService } from './user.service';

@Injectable({
  providedIn: 'root'
})
export class RoleGuardService implements CanActivate {

  isloggedIn : boolean = false;
  role:any;

  constructor(
    private storageService:StorageService,
    private router: Router,
    private userService: UserService,
  ) {
   }

  canActivate(route: ActivatedRouteSnapshot):boolean{
    const expectedRole = route.data['expectedRole'];
    this.isloggedIn = this.storageService.isLoggedIn();
    this.role = this.userService.getRoleFromLocalStorage();
    if( this.isloggedIn == false || this.role != expectedRole){
      alert("bạn không có quyền truy cập vào trang này");
      this.router.navigate(['/']);
      return false;
    }
    return true;
  }
}
