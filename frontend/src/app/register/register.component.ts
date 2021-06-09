import { RegistrationService } from './../service/registration/registration.service';
import { Router } from '@angular/router';
import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { RegisteredUser } from '../model/user/registeredUser';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

  constructor(private router: Router,private registrationService : RegistrationService, private toastr : ToastrService) { }
  public registrationForm: FormGroup;
  private newUser : RegisteredUser;
  ngOnInit(): void {
    this.registrationForm = new FormGroup({
    'name' : new FormControl(null, Validators.required),
    'lastname' : new FormControl(null, Validators.required),
    'username' : new FormControl(null, Validators.required),
    'email' : new FormControl(null, [Validators.required, Validators.email]),
    'password' : new FormControl(null, [Validators.required, Validators.pattern('^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[$@$!%*?&])[A-Za-z0-9\d$@$!%*?&].{7,}$')]),
    'confirmPassword' : new FormControl(null, [Validators.required,    Validators.pattern('^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[$@$!%*?&])[A-Za-z0-9\d$@$!%*?&].{7,}$')]),
  });
  }

  register(){
    var name = this.registrationForm.controls.name.value;
    var lastname = this.registrationForm.controls.lastname.value;
    var username = this.registrationForm.controls.username.value;
    var email = this.registrationForm.controls.email.value;
    var password = this.registrationForm.controls.password.value;
    var confirmPassword = this.registrationForm.controls.confirmPassword.value;
    console.log(password);
    if(password===confirmPassword){
      this.newUser = new RegisteredUser(name, lastname, email, password, confirmPassword, username);

      // this.registrationService.register(this.newUser).subscribe(
      //   success => {
      //     this.toastr.success("Please check your mail to confirm registration")
      //     this.router.navigate(['/regconfirm'], {state: {data: email}});
      //   },
      //   error => {
      //     this.toastr.error(error)
      //   }

      // )
    }

  }

  checkPassword() {
    var password =  this.registrationForm.controls.password.value;
    var regex = new RegExp('^[A-Z][A-Za-z0-9]+[$@$!%*?&]{1}$')
    console.log(regex.test(password))
    if(regex.test(password)){
      this.toastr.warning("You are using common password type!")

    }


  }
}
