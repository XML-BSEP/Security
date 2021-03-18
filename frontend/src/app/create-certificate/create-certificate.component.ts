import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-create-certificate',
  templateUrl: './create-certificate.component.html',
  styleUrls: ['./create-certificate.component.css']
})
export class CreateCertificateComponent implements OnInit {

  constructor(private router : Router) { }

  ngOnInit(): void {
  }
  back(){
    this.router.navigate(['/home']);
  }
}
