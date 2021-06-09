import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, Validators, FormControl} from '@angular/forms';
import { ResetPasswordService} from '../service/reset-password/reset-password.service'
import { ResetMail } from '../model/user/resetMail';
import { ResetPass } from '../model/user/resetPass';
import { Router } from '@angular/router';
import { ToastrModule, ToastrService } from 'ngx-toastr';
import { Message } from '@angular/compiler/src/i18n/i18n_ast';
import { error } from 'selenium-webdriver';

@Component({
  selector: 'app-forgot-password',
  templateUrl: './forgot-password.component.html',
  styleUrls: ['./forgot-password.component.css']
})
export class ForgotPasswordComponent implements OnInit {

  isLinear = true;
  firstFormGroup: FormGroup;
  secondFormGroup: FormGroup;
  thirdFormGroup : FormGroup;
  resetMail : ResetMail;
  code : string;
  resetPass : ResetPass;
  email : string;

  constructor(private _formBuilder: FormBuilder, private resetPasswordSerivce : ResetPasswordService, private router : Router, private toastr : ToastrService) { }

  ngOnInit() {
    this.firstFormGroup = new FormGroup({
      email: new FormControl('', [Validators.required, Validators.email])
    });
    this.secondFormGroup = new FormGroup({
      code: new FormControl('', Validators.required)
    });
    this.thirdFormGroup = new FormGroup({
      password: new FormControl(null, [Validators.required, Validators.pattern('(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[$@$!%*?&])[A-Za-z\d$@$!%*?&].{7,}')]),
      confirmPassword: new FormControl(null, [Validators.required, Validators.pattern('(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[$@$!%*?&])[A-Za-z\d$@$!%*?&].{7,}')])
    });
  }

  sendMail(){
    this.email = this.firstFormGroup.controls.email.value;
    this.resetMail = new ResetMail(this.email)

    this.resetPasswordSerivce.resetPasswordMail(this.resetMail).subscribe(
      response => {
        this.toastr.success(response.toString())
      },
      error => {
        this.toastr.error(error.toString())
      }
    )

  }

  verifyCode() {
    this.code = this.secondFormGroup.controls.code.value;
  }


  resetPassword() {
    var password = this.thirdFormGroup.controls.password.value;
    var confirmPassword = this.thirdFormGroup.controls.confirmPassword.value;
    this.resetPass = new ResetPass(this.email, password, confirmPassword, this.code)

    if(password===confirmPassword){
      this.resetPasswordSerivce.resetPassword(this.resetPass).subscribe(
           res=>{
            this.toastr.success("Successfully changed password");
            this.router.navigate(['/login']);
          },
          error=>{
            this.toastr.error(error.toString());
          }
        )
    }else {
      this.toastr.error("Enter same passwords!")
    }


  }

  checkPassword() {
    var password =  this.thirdFormGroup.controls.password.value;
    var regex = new RegExp('^[A-Z][A-Za-z0-9]+[$@$!%*?&]{1}$')
    console.log(regex.test(password))
    if(regex.test(password)){
      this.toastr.warning("You are using common password type!")

    }
  }

}
