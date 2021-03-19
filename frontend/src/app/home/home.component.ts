import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthenticationService } from '../service/authentication/authentication.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  userLoggedIn : Boolean;
  constructor(private router : Router, private authenticationService : AuthenticationService) { }

  ngOnInit(): void {
    this.userLoggedIn=false;
  }
  createCertificate(){
    this.router.navigate(['createCertificate']);
  }
  allCertificates(){
    this.router.navigate(['allCertificates']);

  }
  
  login(){
    this.router.navigate(['login']);
  }

  logout(){
    this.authenticationService.logout();
    this.router.navigate(['/login']);
  }
}
