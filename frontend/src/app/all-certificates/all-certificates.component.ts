import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { FormControl } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-all-certificates',
  templateUrl: './all-certificates.component.html',
  styleUrls: ['./all-certificates.component.css'],
  encapsulation: ViewEncapsulation.None,

})
export class AllCertificatesComponent implements OnInit {
  public panelColor = new FormControl('red');
  userLoggedIn: boolean;

  constructor(private router : Router) { 
    
    if(localStorage.getItem('userId')!==null){
      this.userLoggedIn = true;
    }else{
      this.userLoggedIn=false;
    }
  }

  ngOnInit(): void {
  }
  back(){
    this.router.navigate(['/home']);
  }
}
