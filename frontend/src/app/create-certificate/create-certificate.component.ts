import { PossibleKeyUsages } from '../model/certificates/PossibleKeyUsages';
import { UsersService } from './../service/users/users.service';
import { STEPPER_GLOBAL_OPTIONS } from '@angular/cdk/stepper';
import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import {COMMA, ENTER, L} from '@angular/cdk/keycodes';
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
import { CreateCertificate } from '../model/certificates/CreateCertificate';
import { DatePipe } from '@angular/common';
import { BehaviorSubject } from 'rxjs';
import { AuthenticatedUser } from '../model/user/authenticatedUser';
import { newUser } from '../model/user/newUser';


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
  certificates : SigningCertificate[];
  subjectId : number;
  pku : PossibleKeyUsages;

  selectedSubject : User;
  isSelectedSubject:boolean;
  createSubjectDisable : boolean = false;
  // allKeyUsages : string[] = ["digitalSignature", "nonRepudiation", "keyEncipherment", "dataEncipherment", "keyAgreement", "certificateSigning", "crlSigning", "encipherOnly", "decipherOnly" ];
  allKeyUsages : string[];

  // allExtendedKeyUsages: string[] = ["serverAuth", "clientAuth", "signExecCode", "emailProtection", "ipsecEndSystem", "ipsecTunnel", "ipsecUser", "timeStamping", "ocspSigning"];
  allExtendedKeyUsages: string[];
  constructor(private certificateService :CertificatesService, private router : Router,    public signingCertDialog: MatDialog, private templateService : TemplateService, public savedTemplates : MatDialog , private userService : UsersService) { }

  ngOnInit(): void {

      this.minDate = new Date(Date.now()).toISOString().split('T')[0];
    this.isSelectedSubject = false;
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
      "firstName": new FormControl(null,[Validators.required]),
      "lastName": new FormControl(null,[Validators.required]),
      "state": new FormControl(null,[Validators.required]),
      "city": new FormControl(null,[Validators.required]),
      "org": new FormControl(null,[Validators.required]),
      "orgunit": new FormControl(null,[Validators.required]),
      "email": new FormControl(null,[Validators.required]),
      "password": new FormControl(null,[Validators.required]),
      "chosenSubject" : new FormControl(null,[Validators.required])

    });


    this.thirdFormGroup = new FormGroup({
      'keyUsage' : new FormControl(null),
      'extendedKeyUsage' : new FormControl(null)
    })
    this.loadSubjects();

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
          "issuer": this.signingCertificate.commonName
        });
        var alias = result.certificate.email+this.signingCertificate.serialNumber;

        // console.log(result.certificate.email);
        this.loadPossibleKeyUsages(alias)
      }
    });
  }
  loadPossibleKeyUsages(alias){

    this.certificateService.getPossibleKeyUsages(alias).subscribe(
      res=>{
        this.pku = res;
        this.allKeyUsages = this.pku.possibleKeyUsages;
        this.allExtendedKeyUsages = this.pku.possibleExtendedKeyUsages;
        console.log(this.pku.possibleExtendedKeyUsages);
      },
      error=>{
        alert("Fail!");
      }

      )

  }



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

    var certificate = this.createCertificateObj();
    this.certificateService.saveCertificate(certificate).subscribe(
      success => {
        alert("Success");
        this.router.navigate(["/"]);
      },
      error => {
        alert("Error");
      }
    )


  }

  createCertificateObj() : CreateCertificate {
    var issuerSerialNumber = this.signingCertificate.serialNumber;
    var validFrom = this.firstFormGroup.value.validFrom;
    var validTo = this.firstFormGroup.value.validTo;
    var signatureAlgorithm = this.firstFormGroup.value.signatureAlgorithm;
    var pubKeyAlgorithm = this.firstFormGroup.value.pubKeyAlgorithm;
    var startDate = new Date(validFrom);
    var startDateString = this.parseDate(startDate);
    var endDate = new Date(validTo);
    var endDateString = this.parseDate(endDate);
    var issuerId = this.signingCertificate.issuerId;
    var createCertificate = new CreateCertificate(this.subjectId, issuerId, startDateString, endDateString, signatureAlgorithm, this.selectedKeyUsages, this.selectedExtendedKeyUsages, issuerSerialNumber);
    return createCertificate;
  }


  selectionChange(user : User) {
    this.subjectId = user.id;
    console.log(this.subjectId);
    this.isSelectedSubject=true;
    this.secondFormGroup = new FormGroup({
      "firstName": new FormControl({value:null, disabled: true}),
      "lastName": new FormControl({value:null, disabled: true}),
      "state": new FormControl({value:null, disabled: true}),
      "city": new FormControl({value:null, disabled: true}),
      "org": new FormControl({value:null, disabled: true}),
      "orgunit": new FormControl({value:null, disabled: true}),
      "email": new FormControl({value:null, disabled: true}),
      "password": new FormControl({value:null, disabled: true}),
      "chosenSubject": new FormControl(user)
    });
    this.createSubjectDisable = true;
  }

  resetSubject(){
    this.secondFormGroup = new FormGroup({
      "firstName": new FormControl({value:null, disabled: false}, Validators.required),
      "lastName": new FormControl({value:null, disabled: false}, Validators.required),
      "state": new FormControl({value:null, disabled: false}, Validators.required),
      "city": new FormControl({value:null, disabled: false}, Validators.required),
      "org": new FormControl({value:null, disabled: false},Validators.required),
      "orgunit": new FormControl({value:null, disabled: false},Validators.required),
      "email": new FormControl({value:null, disabled: false},Validators.required),
      "password": new FormControl({value:null, disabled: false},Validators.required),
      "chosenSubject": new FormControl(null)
    });
  }

  getLoggedInUserId() : number {
      return parseInt(localStorage.getItem('userId'));

  }

  createUser(){
    let user = new newUser(this.secondFormGroup.controls.firstName.value,
                    this.secondFormGroup.controls.lastName.value,
                    null,
                    this.secondFormGroup.controls.org.value,
                    this.secondFormGroup.controls.orgunit.value,
                    this.secondFormGroup.controls.state.value,
                    this.secondFormGroup.controls.city.value,
                    this.secondFormGroup.controls.email.value,
                    null,
                    this.secondFormGroup.controls.password.value);

                    console.log(user)

    this.userService.createSubject(user).subscribe(
      res=>{
        alert('Success!');
        this.loadSubjects();
        this.secondFormGroup = new FormGroup({
          "firstName": new FormControl({value:user.givenName, disabled: true}),
          "lastName": new FormControl({value:user.surname, disabled: true}),
          "state": new FormControl({value:user.state, disabled: true}),
          "city": new FormControl({value:user.city, disabled: true}),
          "org": new FormControl({value:user.organization, disabled: true}),
          "orgunit": new FormControl({value:user.organizationUnit, disabled: true}),
          "email": new FormControl({value:user.email, disabled: true}),
          "password": new FormControl({value:user.password, disabled: true}),
          "chosenSubject": new FormControl(null)
        });
        this.createSubjectDisable = true;

      },
      error=>{
        alert("Fail!");
      }

      )
  }
  parseDate(date : Date) : string {
    var month = '' + (date.getMonth() + 1)
    var day = '' + date.getDate();
    var year = '' + date.getFullYear();

    if(month.length < 2) {
      month = '0' + month;
    }
    if(day.length < 2) {
      day = '0' + day;
    }

    return [year, month, day].join('-');
  }

  createRoot() {
    var validFrom = this.firstFormGroup.value.validFrom;
    var validTo = this.firstFormGroup.value.validTo;
    var startDate = new Date(validFrom);
    var startDateString = this.parseDate(startDate);
    var endDate = new Date(validTo);
    var endDateString = this.parseDate(endDate);
    var issuerId = 1;
    var subjectId = issuerId;
    var signatureAlgorithm = this.firstFormGroup.value.signatureAlgorithm;

    var certificate = new CreateCertificate(subjectId, issuerId, startDateString, endDateString, signatureAlgorithm, String[""], String[""], "1");

    this.certificateService.saveRootCertificate(certificate).subscribe(
      success => {
        alert("Success");
      },
      error => {
        alert("Error");
      }
    
    );
  }

  isAdminLoggedIn() : boolean {
    var currentUserSubject = new BehaviorSubject<AuthenticatedUser>(JSON.parse(localStorage.getItem('currentUser')));
    if(currentUserSubject.value.role == "Admin") {
      return true;
    }
    return false;
  }
}
