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
  public template;
  chosenTemplate;
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

  constructor(private router : Router,    public signingCertDialog: MatDialog, private templateService : TemplateService, public savedTemplates : MatDialog ) { }

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
      "firstName": new FormControl(null, [Validators.required]),
      "lastName": new FormControl(null, [Validators.required]),
      "state": new FormControl(null, [Validators.required]),
      "city": new FormControl(null, [Validators.required]),
      "org": new FormControl(null, [Validators.required]),
      "orgunit": new FormControl(null, [Validators.required]),
      "email": new FormControl(null, [Validators.required]),
      "password": new FormControl(null, [Validators.required]),
      "chosenSubject" : new FormControl(null)
    });


    this.thirdFormGroup = new FormGroup({
      'keyUsage' : new FormControl(null, Validators.required),
      'extendedKeyUsage' : new FormControl(null, Validators.required)
    })
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

  download(){

  }
}
