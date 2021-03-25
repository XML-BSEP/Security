import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { FormControl } from '@angular/forms';
import { Router } from '@angular/router';
import { BehaviorSubject } from 'rxjs';
import { CertificatesService } from '../certificates.service';
import { SigningCertificate } from '../model/certificates/SigningCertificate';
import { AuthenticatedUser } from '../model/user/authenticatedUser';

@Component({
  selector: 'app-all-certificates',
  templateUrl: './all-certificates.component.html',
  styleUrls: ['./all-certificates.component.css'],
  encapsulation: ViewEncapsulation.None,

})
export class AllCertificatesComponent implements OnInit {
  public panelColor = new FormControl('red');
  userLoggedIn: boolean;
  selectedOption : string;
  certificates : SigningCertificate[]
  currentUserSubject: BehaviorSubject<AuthenticatedUser>;
  isAdmin : boolean;

  constructor(private router : Router, private certificateService : CertificatesService) { 
    
    if(localStorage.getItem('userId')!==null){
      this.userLoggedIn = true;
    }else{
      this.userLoggedIn=false;
    }
  }

  ngOnInit(): void {
    //this.getAllCertificatesByUser();
    this.isAdmin = this.isAdminLoggedIn();
    this.isAdmin ? this.selectedOption = "option1" : this.selectedOption = "option2";
  }
  back(){
    this.router.navigate(['/home']);
  }

  getAllCertificatesByUser() {
    this.certificateService.getCertificatesByUser().subscribe(data => {
      this.certificates = data;
      console.log(this.certificates);

    })
  }

  isAdminLoggedIn() : boolean {
    this.currentUserSubject = new BehaviorSubject<AuthenticatedUser>(JSON.parse(localStorage.getItem('currentUser')));
    if(this.currentUserSubject.value.role == "Admin") {
      return true;
    }
    return false;
  }
}
