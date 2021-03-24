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

  getRootCertificates() {
    return this.http.get<SigningCertificate[]>(`${environment.baseUrl}/${environment.certificates}/getRootCertificates`);
  }

  getCACertificates() {
    return this.http.get<SigningCertificate[]>(`${environment.baseUrl}/${environment.certificates}/getCaCertificates`);
  }

  getEECertificates() {
    return this.http.get<SigningCertificate[]>(`${environment.baseUrl}/${environment.certificates}/getEndEntityCertificates`);
  }
}
