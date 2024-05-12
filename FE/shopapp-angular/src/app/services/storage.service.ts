import { Injectable } from '@angular/core';
import { TokenService } from './token.service';

const USER_KEY = 'access_token';

@Injectable({
  providedIn: 'root'
})
export class StorageService {

  constructor(
              private tokenService: TokenService,
            ){}

  // clean():void{
  //   window.sessionStorage.clear();
  // }


  // saveUser(user: any): void {
  //   window.sessionStorage.removeItem(USER_KEY);
  //   window.sessionStorage.setItem(USER_KEY, JSON.stringify(user));
  // }
  // getUser(): any {
  //   const user = window.sessionStorage.getItem(USER_KEY);
  //   if (user) {
  //     return JSON.parse(user);
  //   }

  //   return {};
  // }
  isLoggedIn(): boolean {
    const user = localStorage.getItem(USER_KEY);
    // return user !== null && !this.jwtHelper.isTokenExpired(user);
    if (!this.tokenService.isTokenExpired()) {
      return true;
    }
    return false;
  }
}
