import { STEPPER_GLOBAL_OPTIONS } from '@angular/cdk/stepper';
import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import {COMMA, ENTER} from '@angular/cdk/keycodes';
import { Observable } from 'rxjs';
import { MatChipInputEvent } from '@angular/material/chips';
import { MatAutocomplete, MatAutocompleteSelectedEvent } from '@angular/material/autocomplete';
import {map, startWith} from 'rxjs/operators';

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
  
  constructor(private router : Router) { }

  ngOnInit(): void {

    // this.filteredKeyUsages = this.keyUsageControl.valueChanges.pipe(
    //   startWith(null),
    //   map((keyUsage: string | null) => keyUsage ? this._filter(keyUsage) : this.allKeyUsages.slice()));

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
      "allias": new FormControl(null, [Validators.required]),
      "chosenSubject" : new FormControl(null)
    });


    this.thirdFormGroup = new FormGroup({
      'keyUsage' : new FormControl(null, Validators.required),
      'extendedKeyUsage' : new FormControl(null, Validators.required)
    })
  }
  // add(event: MatChipInputEvent): void {
  //   const input = event.input;
  //   const value = event.value;

  //   // Add our fruit
  //   if ((value || '').trim()) {
  //     this.chosenKeyUsages.push(value.trim());
  //   }

  //   // Reset the input value
  //   if (input) {
  //     input.value = '';
  //   }

  //   this.keyUsageControl.setValue(null);
  // }

  // remove(usage: string): void {
  //   const index = this.chosenKeyUsages.indexOf(usage);

  //   if (index >= 0) {
  //     this.chosenKeyUsages.splice(index, 1);
  //   }
  // }
  // selected(event: MatAutocompleteSelectedEvent): void {
  //   this.chosenKeyUsages.push(event.option.viewValue);
  //   this.keyUsageInput.nativeElement.value = '';
  //   this.keyUsageControl.setValue(null);
  // }

  // private _filter(value: string): string[] {
  //   const filterValue = value.toLowerCase();

  //   return this.allKeyUsages.filter(usage => usage.toLowerCase().indexOf(filterValue) === 0);
  // }

  back(){
    this.router.navigate(['/home']);
  }
}
