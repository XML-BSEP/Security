import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { SigningCertificate } from 'src/app/model/certificates/SigningCertificate';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class CertificateService {

  constructor(
    private http: HttpClient
  ) { }


}
