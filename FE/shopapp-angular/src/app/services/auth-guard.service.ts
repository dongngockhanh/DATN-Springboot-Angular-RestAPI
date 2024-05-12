import { ActivatedRouteSnapshot, CanActivateFn, Router, RouterStateSnapshot } from "@angular/router";
import { TokenService } from "./token.service";
import { Injectable } from "@angular/core";
import { inject } from "@angular/core";

@Injectable({
    providedIn: 'root'
})
export class AuthGuard{
    constructor(private router: Router, private tokenService:TokenService) {}
    canActivate(next: ActivatedRouteSnapshot, state:RouterStateSnapshot){
        const isTokenExpired = this.tokenService.isTokenExpired();
        const isUserValid = this.tokenService.getUserId() > 0;
        // debugger
        if(!isTokenExpired && isUserValid){
            return true;
        }
        else{
            // tạo thông báo cần đăng nhập
            alert('Bạn cần đăng nhập để thực hiện chức năng này');
            this.router.navigate(['/login-page']);
            return false;
        }
    }
}
export const AuthGuardFn: CanActivateFn = (next: ActivatedRouteSnapshot, state: RouterStateSnapshot) => {
    return inject(AuthGuard).canActivate(next, state);
}