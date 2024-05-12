import { Injectable } from "@angular/core";
import { JwtHelperService } from "@auth0/angular-jwt";
@Injectable({
    providedIn: 'root'
})
export class TokenService {
    private readonly TOKEN_KEY = 'access_token';
    private jwtHelperService = new JwtHelperService();
    constructor() { }

    getToken(): string | null {
        return localStorage.getItem(this.TOKEN_KEY);
    }
    setToken(token: string): void {
        localStorage.setItem(this.TOKEN_KEY, token);
    }
    isTokenExpired(): boolean { // kiểm tra token đã hết hạn chưa false là chưa hết hạn true là hết hạn(và không có token)
        const token = this.getToken();
        if (token === null) {
            return true;
        }
        return this.jwtHelperService.isTokenExpired(token!);
    }
    removeToken(): void {
        localStorage.removeItem(this.TOKEN_KEY);
    }
    getUserId():number{
        let userObject = this.jwtHelperService.decodeToken(this.getToken()??'');
        return  userObject && 'id' in userObject ? parseInt(userObject['id']) : 0;
    }
}