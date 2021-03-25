import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { BehaviorSubject } from 'rxjs';
import { CertificatesService } from 'src/app/certificates.service';
import { SigningCertificate } from 'src/app/model/certificates/SigningCertificate';
import { AuthenticatedUser } from 'src/app/model/user/authenticatedUser';

@Component({
  selector: 'app-choose-issuer-dialog',
  templateUrl: './choose-issuer-dialog.component.html',
  styleUrls: ['./choose-issuer-dialog.component.css']
})
export class ChooseIssuerDialogComponent implements OnInit {
  signingCertificates: Array<SigningCertificate> = new Array();
  chosenKeyUsage;
  chosenExtendedKeyUsage;
  keyUsages;
  allCertificates : SigningCertificate[] = [];
  certificates : SigningCertificate[];
  currentUserSubject: BehaviorSubject<AuthenticatedUser>
  constructor(public dialogRef: MatDialogRef<ChooseIssuerDialogComponent>,@Inject(MAT_DIALOG_DATA) data, private certificateService : CertificatesService) {
      this.chosenKeyUsage = data.chosenKeyUsage;
      this.chosenExtendedKeyUsage = data.chosenExtendedKeyUsage;
  }

  ngOnInit(): void {
    if(this.isAdminLoggedIn()) {
      this.loadAllCertificatesForSigning();
    }
    else {
      this.loadAllCertificatesForSigningByUser();
    }
  }

  loadAllCertificatesForSigning() {
    this.certificateService.getAllForSigning().subscribe(data => {
      this.certificates = data;
    })
  }

  loadAllCertificatesForSigningByUser() {
    this.certificateService.getAllForSigningByUser().subscribe(data => {
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

  loadCertificatesByUser() {
    this.certificateService.getCertificatesByUser().subscribe(data => {
      this.certificates = data;
    })
  }
  updateKeyUsage(item) {
    this.keyUsages = "";
    var i = 0;
    for(; i < item.keyUsage.length; i++) {
      this.keyUsages += item.keyUsage[i] + ", ";
    }
    this.keyUsages = this.keyUsages.substr(0, this.keyUsages.length - 2);
  }
  onSubmit(certificate) {
    console.log(certificate);
    this.dialogRef.close({ certificate });
  }

  onNoClick(): void {
    this.dialogRef.close();
  }
}
