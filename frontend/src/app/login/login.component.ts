import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Authentication } from '../model/user/authentication';
import { Role } from '../model/user/role';
import { AuthenticationService } from '../service/authentication/authentication.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  public loginForm: FormGroup;
  username;
  password;
  credentials;
  constructor(private router : Router, private authService : AuthenticationService) { }

  ngOnInit(): void {
    this.loginForm = new FormGroup({
      'username' : new FormControl(null, [Validators.required, Validators.email]),
      'password' : new FormControl(null, Validators.required)
    });
  }

  back(){
    this.router.navigate(['/home']);
  }

  login(){
    this.username = this.loginForm.controls.username.value;
    this.password = this.loginForm.controls.password.value;
    console.log(this.username);
    this.credentials = new Authentication(this.username.toLowerCase(), this.password);
    console.log(this.credentials);
    this.authService.login(this.credentials).subscribe(
      result => {
        localStorage.setItem('userId',String(result.id))

        this.router.navigate(['/'])

      },
      error=>{
        alert("Wrong username or password")
        this.router.navigate(['/login']);

      });
  }
}
