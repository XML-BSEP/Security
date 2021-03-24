import { UsersService } from './../service/users/users.service';
import { STEPPER_GLOBAL_OPTIONS } from '@angular/cdk/stepper';
import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import {COMMA, ENTER} from '@angular/cdk/keycodes';
import { Observable } from 'rxjs';
import { MatChipInputEvent } from '@angular/material/chips';
import { MatAutocomplete, MatAutocompleteSelectedEvent } from '@angular/material/autocomplete';
import {map, startWith} from 'rxjs/operators';
import { MatDialog } from '@angular/material/dialog';
import { ChooseIssuerDialogComponent } from '../dialogs/choose-issuer-dialog/choose-issuer-dialog.component';
import { SigningCertificate } from '../model/certificates/SigningCertificate';
import { Template } from '../model/certificates/Template';
import { TemplateService } from '../service/template/template.service';
import { SavedTemplatesDialogComponent } from '../dialogs/saved-templates-dialog/saved-templates-dialog.component';
import { User } from '../model/user/user';
import { CertificatesService } from '../certificates.service';

@Component({
  selector: 'app-create-certificate',
  templateUrl: './create-certificate.component.html',
  styleUrls: ['./create-certificate.component.css'],
  providers: [{
    provide: STEPPER_GLOBAL_OPTIONS, useValue: {showError: true}
  }]
})
export class CreateCertificateComponent implements OnInit {
  firstFormGroup: FormGroup;
  secondFormGroup: FormGroup;
  secondFormGroup1: FormGroup;

  thirdFormGroup: FormGroup;
  keyUsageChecked: boolean;
  extendedKeyUsageChecked: boolean;
  chosenKeyUsages : string;
  allSubjects : string[] =["subject1", "subject2", "subject3"];
  selectedKeyUsages;
  selectedExtendedKeyUsages;
  userLoggedIn: boolean;
  signingCertificate: SigningCertificate;
  minDate;
  chosenSubject;
  subjects : User[];
  public template;
  chosenTemplate;
  createdSertificate : SigningCertificate;
  allCA;
  allRoot;
  // visible = true;
  // selectable = true;
  // removable = true;
  // separatorKeysCodes: number[] = [ENTER, COMMA];
  // keyUsageControl = new FormControl();
  // filteredKeyUsages: Observable<string[]>;
  allKeyUsages : string[] = ["Digital Signature", "Non-repudiation", "Key encipherment", "Data encipherment", "Key agreement", "Certificate signing", "CRL signing", "Encipher only", "Decipher only" ];

  // @ViewChild('keyUsageInput') keyUsageInput: ElementRef<HTMLInputElement>;
  // @ViewChild('auto') matAutocomplete: MatAutocomplete;

  allExtendedKeyUsages: string[] = ["TLS Web server authentication", "TLS Web client authentication", "Sign (downloadable) executable code", "Email protection", "IPSEC End System", "IPSEC Tunnel", "IPSEC User", "Timestamping"];

  constructor(private certificateService :CertificatesService, private router : Router,    public signingCertDialog: MatDialog, private templateService : TemplateService, public savedTemplates : MatDialog , private userService : UsersService) { }

  ngOnInit(): void {

    // this.filteredKeyUsages = this.keyUsageControl.valueChanges.pipe(
    //   startWith(null),
    //   map((keyUsage: string | null) => keyUsage ? this._filter(keyUsage) : this.allKeyUsages.slice()));
    this.minDate = new Date(Date.now()).toISOString().split('T')[0];

    if(localStorage.getItem('userId')!==null){
      this.userLoggedIn = true;
    }else{
      this.userLoggedIn=false;
    }
    this.firstFormGroup = new FormGroup({
      "issuer": new FormControl(null, [Validators.required]),
      "validFrom" : new FormControl(null, [Validators.required]),
      "validTo" : new FormControl(null, [Validators.required]),
      "signatureAlgorithm" : new FormControl(null, [Validators.required]),
      "pubKeyAlgorithm": new FormControl("RSA", [Validators.required])

    });
    this.secondFormGroup = new FormGroup({
      "firstName": new FormControl(null),
      "lastName": new FormControl(null),
      "state": new FormControl(null),
      "city": new FormControl(null),
      "org": new FormControl(null),
      "orgunit": new FormControl(null),
      "email": new FormControl(null),
      "password": new FormControl(null),
    });
    this.secondFormGroup1 = new FormGroup({

      "chosenSubject" : new FormControl(null)
    });

    this.thirdFormGroup = new FormGroup({
      'keyUsage' : new FormControl(null, Validators.required),
      'extendedKeyUsage' : new FormControl(null, Validators.required)
    })
    this.loadSubjects();
    this.loadCACertificates();
  }


  back(){
    this.router.navigate(['/home']);
  }
  chooseCertificate() {
    let tempKeyUsage = undefined;
    let tempExtKeyUsage = undefined;


    const dialogRef = this.signingCertDialog.open(ChooseIssuerDialogComponent, {
      width: '80vw',
      height: '90vh',
      data: {
        chosenKeyUsage: tempKeyUsage,
        chosenExtendedKeyUsage: tempExtKeyUsage
      }

    });

    dialogRef.afterClosed().subscribe(result => {

      if (result) {
        this.signingCertificate = result.certificate;
        this.firstFormGroup.patchValue({
          "issuer": this.signingCertificate.issuerCommonName
        });

      }
    });
  }
  loadCACertificates(){
    this.certificateService.getAllCA().subscribe(data =>
      {
        this.allCA = data;
        console.log(this.allCA);
      });
  }
  loadRootCertificates(){
    this.certificateService.getAllRoot().subscribe(data =>
      {
        this.allRoot = data;
        console.log(this.allCA);
      });
  }
  /*
      "firstName": new FormControl(null),
      "lastName": new FormControl(null),
      "state": new FormControl(null),
      "city": new FormControl(null),
      "org": new FormControl(null),
      "orgunit": new FormControl(null),
      "email": new FormControl(null),
      "password": new FormControl(null),
  */

  // validateSecondForm(){
  //   if(this.secondFormGroup.controls.chosenSubject.value !==null){
  //     return true;
  //   }else{
  //     return this.validateName() && this.validateEmail() && this.validateLastName() && this.validateCity() && this.validateState() && this.validateOrg() && this.validateOrgUnit() && this.validatePassword();
  //   }
  // }
  // validateName(){
  //   return this.secondFormGroup.controls.firstName.value.trim().length>0;
  // }

  // validateLastName(){
  //   return this.secondFormGroup.controls.lastName.value.trim().length>0;
  // }

  // validateState(){
  //   return this.secondFormGroup.controls.state.value.trim().length>0;
  // }

  // validateCity(){
  //   return this.secondFormGroup.controls.city.value.trim().length>0;
  // }

  // validateOrg(){
  //   return this.secondFormGroup.controls.org.value.trim().length>0;
  // }

  // validateOrgUnit(){
  //   return this.secondFormGroup.controls.orgunit.value.trim().length>0;
  // }

  // validateEmail(){
  //   return this.secondFormGroup.controls.email.value.trim().length>0;
  // }

  // validatePassword(){
  //   return this.secondFormGroup.controls.password.value.trim().length>0;
  // }


  viewSavedTemplates() {
    let tempKeyUsage = undefined;
    let tempExtKeyUsage = undefined;


    const dialogRef = this.savedTemplates.open(SavedTemplatesDialogComponent, {
      width: '80vw',
      height: '90vh'
    });

    dialogRef.afterClosed().subscribe(result => {

      if (result) {
        this.chosenTemplate = result.template;

        this.firstFormGroup.patchValue({
          "issuer": this.chosenTemplate.name,
          "signatureAlgorithm" : this.chosenTemplate.signatureAlgorithm,
          "pubKeyAlgorithm": this.chosenTemplate.keyAlgorithm
        });

        this.thirdFormGroup.patchValue({
          "keyUsage" : this.chosenTemplate.keyUsage,
          "extendedKeyUsage" : this.chosenTemplate.extendedKeyUsage
        })
        this.selectedKeyUsages = this.chosenTemplate.keyUsage;
        this.selectedExtendedKeyUsages = this.chosenTemplate.extendedKeyUsage;
      }
    });
  }
  // public id :  Number;
  // public signatureAlgorithm : String;
  // public keyAlgorithm : String;
  // public name : String;
  // public timestamp : Date;
  // public keyUsage : String[];
  // public extendedKeyUsage : String[];

  saveTemplate(){
    let timestamp = new Date();

    this.template = new Template(null, this.firstFormGroup.controls.signatureAlgorithm.value, this.firstFormGroup.controls.pubKeyAlgorithm.value, this.firstFormGroup.controls.issuer.value, timestamp, this.selectedKeyUsages, this.selectedExtendedKeyUsages);

    console.log(this.template);

    this.templateService.saveTemplate(this.template).subscribe(
      res=>{
        alert('Success!');
      },
      error=>{
        alert("Fail!");
      }

      )

  }
  loadSubjects(){
    this.userService.getAll().subscribe(data =>
      {
        this.subjects = data;
        console.log(this.subjects);
      });
  }
  saveCertificate(){
    if(this.secondFormGroup.controls.email.value.length > 0){
      var subjectEmail = this.secondFormGroup.controls.email.value;
    }else{
      var subjectEmail = this.chosenSubject.email;
    }
    this.createdSertificate = new SigningCertificate(this.firstFormGroup.controls.issuer.value, this.signingCertificate.issuerEmail, subjectEmail,null, this.signingCertificate.serialNum, this.firstFormGroup.controls.validFrom.value,
      this.firstFormGroup.controls.validTo.value, this.selectedKeyUsages, this.selectedExtendedKeyUsages, this.firstFormGroup.controls.signatureAlgorithm.value)

    console.log(this.createdSertificate);
  }
}
