import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
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
  constructor(    public dialogRef: MatDialogRef<ChooseIssuerDialogComponent>,    @Inject(MAT_DIALOG_DATA) data    ) {
      this.chosenKeyUsage = data.chosenKeyUsage;
      this.chosenExtendedKeyUsage = data.chosenExtendedKeyUsage;
  }

  ngOnInit(): void {
    let date1 = new Date();
    let date2 = new  Date("2029-01-16");
    let signedCert = new SigningCertificate("IssuerCommonName", "issuerIssuerEmail", "issuerEmail", 1, "serialNum", date1.toLocaleDateString(),
      date2.toLocaleDateString(), ["aaa", "aaaaa", "aaaaaaaaaa"], ["aaa1", "aaaaa2", "aaaaaaaaaa3"]);
    let signedCert2 = new SigningCertificate("IssuerCommonName1", "issuerIssuerEmail1", "issuerEmail1", 2, "serialNum1", date1.toLocaleDateString(),
      date2.toLocaleDateString(), ["aaa", "aaaaa", "aaaaaaaaaa"], ["aaa1", "aaaaa2", "aaaaaaaaaa3"]);
      let signedCert3 = new SigningCertificate("IssuerCommonName1", "issuerIssuerEmail1", "issuerEmail1", 2, "serialNum1", date1.toLocaleDateString(),
      date2.toLocaleDateString(), ["aaa", "aaaaa", "aaaaaaaaaa"], ["aaa1", "aaaaa2", "aaaaaaaaaa3"]);
      let signedCert4 = new SigningCertificate("IssuerCommonName1", "issuerIssuerEmail1", "issuerEmail1", 2, "serialNum1", date1.toLocaleDateString(),
      date2.toLocaleDateString(), ["aaa", "aaaaa", "aaaaaaaaaa"], ["aaa1", "aaaaa2", "aaaaaaaaaa3"]);
      let signedCert5 = new SigningCertificate("IssuerCommonName1", "issuerIssuerEmail1", "issuerEmail1", 2, "serialNum1", date1.toLocaleDateString(),
      date2.toLocaleDateString(), ["aaa", "aaaaa", "aaaaaaaaaa"], ["aaa1", "aaaaa2", "aaaaaaaaaa3"]);
      let signedCert6 = new SigningCertificate("IssuerCommonName1", "issuerIssuerEmail1", "issuerEmail1", 2, "serialNum1", date1.toLocaleDateString(),
      date2.toLocaleDateString(), ["aaa", "aaaaa", "aaaaaaaaaa"], ["aaa1", "aaaaa2", "aaaaaaaaaa3"]);
      let signedCert7 = new SigningCertificate("IssuerCommonName1", "issuerIssuerEmail1", "issuerEmail1", 2, "serialNum1", date1.toLocaleDateString(),
      date2.toLocaleDateString(), ["aaa", "aaaaa", "aaaaaaaaaa"], ["aaa1", "aaaaa2", "aaaaaaaaaa3"]);

    this.signingCertificates.push(signedCert2);
    this.signingCertificates.push(signedCert);
    this.signingCertificates.push(signedCert3);
    this.signingCertificates.push(signedCert4);
    this.signingCertificates.push(signedCert5);
    this.signingCertificates.push(signedCert6);
    this.signingCertificates.push(signedCert7);


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
