import { Injectable } from '@angular/core';
import { environment } from './../../../environments/environment';
import { HttpClient, XhrFactory } from '@angular/common/http';
import { ResetMail } from 'src/app/model/user/resetMail';
import { ResetPass } from 'src/app/model/user/resetPass';

@Injectable({
  providedIn: 'root'
})
export class ResetPasswordService {

  constructor(private https : HttpClient) { }

  resetPasswordMail(data : ResetMail){
    return this.https.post(`${environment.baseUrl}/${environment.resetPasswordMail}`,data, {responseType : 'json'});
  }


  resetPassword(data : ResetPass){
    return this.https.post(`${environment.baseUrl}/${environment.resetPassword}`,data, {responseType : 'json'});
  }
}
