import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { BehaviorSubject } from 'rxjs';
import { CertificatesService } from '../certificates.service';
import { AuthenticatedUser } from '../model/user/authenticatedUser';
import { AuthenticationService } from '../service/authentication/authentication.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  userLoggedIn : Boolean;
  hasCA: boolean = true;
  currentUserSubject;
  constructor(private router : Router, private authenticationService : AuthenticationService, private certificateService: CertificatesService) { }

  ngOnInit(): void {
    console.log(localStorage.getItem('userId'));
    if(localStorage.getItem('userId')!==null){
      this.userLoggedIn = true;
      if(!this.isAdminLoggedIn()){
        this.getAllForSigningByUser();
      }
    }else{
      this.userLoggedIn=false;
    }

  }
  isAdminLoggedIn() : boolean {
    this.currentUserSubject = new BehaviorSubject<AuthenticatedUser>(JSON.parse(localStorage.getItem('currentUser')));
    if(this.currentUserSubject.value.role == "Admin") {
      return true;
    }
    return false;
  }
  getAllForSigningByUser() {
    this.certificateService.getAllForSigningByUser().subscribe(data => {
      if(data.length>0){
        this.hasCA=true;
      }else{
        this.hasCA = false;
      }
    })

  }
  createCertificate(){
    this.router.navigate(['/createCertificate']);
  }
  allCertificates(){
    this.router.navigate(['/allCertificates']);

  }

  login(){
    this.router.navigate(['/login']);
  }

  logout(){
    this.authenticationService.logout();
    this.router.navigate(['/login']);
  }

}
