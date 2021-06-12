import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Component, OnInit } from '@angular/core';
import { RegistrationService } from '../service/registration/registration.service';
import {ConfirmRegistration} from '../model/user/confirmRegistration'
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-registration-confirmation',
  templateUrl: './registration-confirmation.component.html',
  styleUrls: ['./registration-confirmation.component.css']
})
export class RegistrationConfirmationComponent implements OnInit {

  constructor(private router : Router, private registrationService : RegistrationService, private toastr : ToastrService) { }
  public codeForm : FormGroup;
  private userMail : string;
  private confirmRegistration : ConfirmRegistration


  ngOnInit(): void {
    this.codeForm = new FormGroup({
      //maybe add pattern for code validation on frontend
      'code' : new FormControl(null, [Validators.required])
    });
    if(history.state.data === undefined){
      this.router.navigate(['/home'])
    }else{
      this.userMail = history.state.data;

    }
  }
  confirm(){
    console.log(this.userMail)
    this.confirmRegistration = new ConfirmRegistration(this.userMail, this.codeForm.controls.code.value)

    this.registrationService.confAcc(this.confirmRegistration).subscribe(
      res=>{
        this.toastr.success("Successful confirmation")
        this.router.navigate(['/login']);
      },
      error => {
        this.toastr.error("Confirmation code is not correct")
      }


        )

  }
  resend(){
    let sendData = {
      "email" : this.userMail
    }
    this.registrationService.resend(sendData).subscribe(
      res => {
        this.toastr.success("Resend successful, check your email!")
      },
      error => {
        this.toastr.error("Something went wrong, your code probably expired, try to register again.")
      }
    )

  }
}
