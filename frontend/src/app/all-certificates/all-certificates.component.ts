import { DownloadCertificate } from './../model/certificates/DownloadCertificate';
import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { FormControl } from '@angular/forms';
import { Router } from '@angular/router';
import { CertificateService } from "../service/certificate/certificate.service";
import { SigningCertificate } from '../model/certificates/SigningCertificate';
import { BehaviorSubject } from 'rxjs';
import { CertificatesService } from '../certificates.service';
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
  isSelfSigned: boolean;
  signingCertificates: Array<SigningCertificate>;
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

  ngOnInit(): void{
    this.getRootCerificates;
    this.isSelfSigned = true;

      //this.getAllCertificatesByUser();
      this.isAdmin = this.isAdminLoggedIn();
      /*
      if(this.isAdmin) {
        this.getAllCertificates()
      }
      else {
        this.getAllCertificatesByUser();
      }*/
      // this.isAdmin ? this.selectedOption = "option1" : this.selectedOption = "option2";

  }

  downloadCertificate(item){

    var downlaodCertificate = new DownloadCertificate(item.email, item.serialNumber, item.commonName);
    this.certificateService.downloadCertificate(downlaodCertificate).subscribe(
      res=>{
        alert('Success! You can find your certificate in your downloads folder');
      },
      error=>{
        alert("Fail!");
      }

      )

     // this.isAdmin ? this.selectedOption = "option1" : this.selectedOption = "option2";

  }

  onTypeChange() {
    if(this.isAdmin){
      if(this.selectedOption == "option1" ){

        this.isSelfSigned = true;
        this.getRootCerificates();
        console.log(this.signingCertificates);
      }else if(this.selectedOption == "option2"){
        this.getCACerificates();
      }else if(this.selectedOption == "option3"){
        this.getEECerificates();
      }
    }else{
      if(this.selectedOption == "option2"){
        console.log("usao hehe2")
        this.getCACerificatesByUser();
      }else if(this.selectedOption == "option3"){
        console.log("usao hehe3")
        this.getEECerificatesByUser();
      }
    }

  }


    getRootCerificates(){ this.certificateService.getAllRoot().subscribe(
      {
        next: (result) => {
          this.signingCertificates = result;
        },
        error: data => {
          if (data.error && typeof data.error === "string")
          console.log("pokusaj opet")
          else
          console.log("nije ucitao")
        }
      }
    );
  }

    getCACerificates(){console.log("usao2"); this.certificateService.getAllCA().subscribe(
      {
        next: (result) => {
          this.signingCertificates = result;
        },
        error: data => {
          if (data.error && typeof data.error === "string")
          console.log("pokusaj opet")
          else
          console.log("nije ucitao")
        }
      }
    );
  }

  getEECerificates(){console.log("usao2"); this.certificateService.getAllEndEntity().subscribe(
    {
      next: (result) => {
        this.signingCertificates = result;
      },
      error: data => {
        if (data.error && typeof data.error === "string")
        console.log("pokusaj opet")
        else
        console.log("nije ucitao")
      }
    }
  );
  }

  getCACerificatesByUser(){console.log("usao2"); this.certificateService.getCaCertificatesByUser().subscribe(
    {
      next: (result) => {
        this.signingCertificates = result;
      },
      error: data => {
        if (data.error && typeof data.error === "string")
        console.log("pokusaj opet")
        else
        console.log("nije ucitao")
      }
    }
  );
  }

getEECerificatesByUser(){console.log("usao2"); this.certificateService.getEeCertificatesByUser().subscribe(
{
  next: (result) => {
    this.signingCertificates = result;
  },
  error: data => {
    if (data.error && typeof data.error === "string")
    console.log("pokusaj opet")
    else
    console.log("nije ucitao")
  }
}
);
}

  back(){
    this.router.navigate(['/home']);
  }

  getAllCertificatesByUser() {
    this.certificateService.getCertificatesByUser().subscribe(data => {
      this.signingCertificates = data;

    })
  }

  //obrisi mozda
  getAllCertificates() {
    this.certificateService.getAll().subscribe(data => {
      this.certificates = data;
    })
  }

  isAdminLoggedIn() : boolean {
    this.currentUserSubject = new BehaviorSubject<AuthenticatedUser>(JSON.parse(localStorage.getItem('currentUser')));
    if(this.currentUserSubject.value.role == "Admin") {
      return true;
    }
    return false;
  }

  revokeCertificate(item : SigningCertificate) {
      if(this.isAdmin) {
        this.certificateService.revokeCertificate(item.serialNumber).subscribe(
          res=>{
            alert('Success!');
            location.reload();
          },
          error=>{
            alert("Fail!");
          }
        )
      }
    }



}
