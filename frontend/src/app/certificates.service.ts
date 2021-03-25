import { environment } from './../environments/environment';
import { Observable } from 'rxjs';
import { SigningCertificate } from './model/certificates/SigningCertificate';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class CertificatesService {

  constructor(private http : HttpClient) { }

  getAllCA() : Observable<SigningCertificate[]>{
    return this.http.get<SigningCertificate[]>(`${environment.baseUrl}/${environment.certificate}/${environment.getCaCertificates}`);
  }

  getAllRoot() : Observable<SigningCertificate[]>{
    return this.http.get<SigningCertificate[]>(`${environment.baseUrl}/${environment.certificate}/${environment.getRootCertificates}`);
  }

  getCertificatesByUser() : Observable<SigningCertificate[]> {
    return this.http.get<SigningCertificate[]>(`${environment.baseUrl}/${environment.api}/${environment.getCertificatesByUser}`)
  }
}
