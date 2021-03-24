import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { FormControl } from '@angular/forms';
import { Router } from '@angular/router';
import { CertificateService } from "../service/certificate/certificate.service";
import { SigningCertificate } from '../model/certificates/SigningCertificate';

@Component({
  selector: 'app-all-certificates',
  templateUrl: './all-certificates.component.html',
  styleUrls: ['./all-certificates.component.css'],
  encapsulation: ViewEncapsulation.None,

})
export class AllCertificatesComponent implements OnInit {
  public panelColor = new FormControl('red');
  userLoggedIn: boolean;
  selected: string;
  isSelfSigned: boolean;
  signingCertificates: Array<SigningCertificate>;

  constructor(private router : Router, private certificateService: CertificateService) { 
    
    if(localStorage.getItem('userId')!==null){
      this.userLoggedIn = true;
    }else{
      this.userLoggedIn=false;
    }
  }

  ngOnInit(): void{
    console.log("usao");
    this.selected = "option1";
    this.getRootCerificates;
    this.isSelfSigned = true;
    
  }

  
  onTypeChange() {
    /*
    if (this.isSelfSigned){
      console.log("usao3");
      this.getRootCerificates;
    }*/

    if(this.selected == "option1"){
      console.log("usao3");
      this.isSelfSigned = true;
      this.getRootCerificates();
      console.log(this.signingCertificates);
    }else if(this.selected == "option2"){
      this.getCACerificates();
    }else if(this.selected == "option3"){
      this.getEECerificates();
    }
  }


    getRootCerificates(){console.log("usao2"); this.certificateService.getRootCertificates().subscribe(
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

  getCACerificates(){console.log("usao2"); this.certificateService.getCACertificates().subscribe(
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

getEECerificates(){console.log("usao2"); this.certificateService.getEECertificates().subscribe(
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
}
