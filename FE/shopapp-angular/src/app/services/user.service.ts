import { EventEmitter, Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, Subject } from 'rxjs';
import { RegisterDTO } from '../dtos/user/register.dto';
import { LoginDTO } from '../dtos/user/login.dto';
import { environment } from '../environments/environment';
import { UserResponse } from '../responses/user/user.response';
import { UpdateDTO } from '../dtos/user/update.dto';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private apiUrlUser = `${environment.apiBaseUrl}/users`;
  private apiUrlRegister = `${environment.apiBaseUrl}/users/register`;
  private apiUrlLogin    = `${environment.apiBaseUrl}/users/login`;
  private apiConfig = {
    headers: this.createHeders(),
  }
  constructor(private http: HttpClient) { }
  private createHeders(): HttpHeaders{
    return new HttpHeaders({
      'Content-Type': 'application/json',
      'Accept-Language': 'vi',
    });
  }

  register(registerDTO: RegisterDTO):Observable<any>{
    return this.http.post(this.apiUrlRegister,registerDTO,this.apiConfig);
  }

  login(LoginDTO: LoginDTO):Observable<any> { 
    return this.http.post(this.apiUrlLogin,LoginDTO,this.apiConfig);
  }
  logout(){
    localStorage.removeItem('access_token');
    localStorage.removeItem('cart');
    localStorage.removeItem('userDetail');
  }
  getListUser(){
    return this.http.get(`${environment.apiBaseUrl}/users`);
  }
  getUserDetail(userId:number){
    return this.http.get(`${this.apiUrlUser}/${userId}`);
  }
  // lưu userDetail vào localStorage
  saveUserDetailToLocalStogare(userResponse?: UserResponse){
    try{
      if(!userResponse||userResponse===null){
        return;
      }
      const userResponseJSON = JSON.stringify(userResponse);
      localStorage.setItem('userDetail', userResponseJSON); 
      console.log('save userDetail to localStorage success');
    } catch(e){
      console.error(e);
    }
  }
  // lấy userDetail từ localStorage
  getUserDetailFromLocalStorage(){
    try{
      const userDetail = localStorage.getItem('userDetail');
      if(userDetail){
        return JSON.parse(userDetail);
      }
      return null;
    } catch(e){
      console.error(e);
      return null;
    }
  }
  // cập nhật user
  updateUser(updateDTO:UpdateDTO,token:string): Observable<any>{
    return this.http.put(`${environment.apiBaseUrl}/users`,updateDTO,{
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
        Authorization: `Bearer ${token}`
      })
    });
  }
  // cập nhật mật khẩu
  updatePassword(oldPassword:string,newPassword:string,token:string): Observable<any>{
    return this.http.patch(`${environment.apiBaseUrl}/users`,{"old_password":oldPassword,"new_password":newPassword},{
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
        Authorization: `Bearer ${token}`
      })
    });
  }
  // xoá mềm tài khoản set active bằng false
  setActiveUser(userId:number): Observable<any>{
    return this.http.delete(`${this.apiUrlUser}/${userId}`);
  }
  // lấy role của user từ localStorage
  getRoleFromLocalStorage(){
    try{
      const userDetail = localStorage.getItem('userDetail');
      if(userDetail){
        const userResponse = JSON.parse(userDetail);
        return userResponse.role_id.name;
      }
      return null;
    } catch(e){
      console.error(e);
      return null;
    }
  }

}
