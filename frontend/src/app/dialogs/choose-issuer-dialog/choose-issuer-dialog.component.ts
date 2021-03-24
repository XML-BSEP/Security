import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { CertificatesService } from 'src/app/certificates.service';
import { SigningCertificate } from 'src/app/model/certificates/SigningCertificate';

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
  allCA;
  allRoot;
  constructor(public dialogRef: MatDialogRef<ChooseIssuerDialogComponent>,@Inject(MAT_DIALOG_DATA) data, private certificateService : CertificatesService) {
      this.chosenKeyUsage = data.chosenKeyUsage;
      this.chosenExtendedKeyUsage = data.chosenExtendedKeyUsage;
  }

  ngOnInit(): void {
    this.loadCACertificates();
    this.loadRootCertificates();
  }

  loadCACertificates(){
    this.certificateService.getAllCA().subscribe(data =>
      {
        this.allCA = data;
        for(let i=0;i<data.length;i++){
          this.allCertificates.push(data[i]);
        }
        console.log(this.allCA);
      });
  }
  loadRootCertificates(){
    this.certificateService.getAllRoot().subscribe(data =>
      {
        this.allRoot = data;
        for(let i=0;i<data.length;i++){
          this.allCertificates.push(data[i]);
        }
        console.log(this.allCA);
      });
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
