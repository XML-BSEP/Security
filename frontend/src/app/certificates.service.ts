import { environment } from './../environments/environment';
import { Observable } from 'rxjs';
import { SigningCertificate } from './model/certificates/SigningCertificate';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { CreateCertificate } from './model/certificates/CreateCertificate';

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

  getAllEndEntity() : Observable<SigningCertificate[]> {
    return this.http.get<SigningCertificate[]>(`${environment.baseUrl}/${environment.certificate}/${environment.getEndEntityCertificates}`)
  }

  getAll() : Observable<SigningCertificate[]> {
    return this.http.get<SigningCertificate[]>(`${environment.baseUrl}/${environment.certificate}/${environment.all}`);
  }

  getAllForSigning() : Observable<SigningCertificate[]> {
    return this.http.get<SigningCertificate[]>(`${environment.baseUrl}/${environment.certificate}/${environment.getAllForSigning}`)
  }

  getAllForSigningByUser() : Observable<SigningCertificate[]> {
    return this.http.get<SigningCertificate[]>(`${environment.baseUrl}/${environment.certificate}/${environment.getAllForSigningByUser}`)
  }

  getCertificatesByUser() : Observable<SigningCertificate[]> {
    return this.http.get<SigningCertificate[]>(`${environment.baseUrl}/${environment.certificate}/${environment.getCertificatesByUser}`)
  }

  saveCertificate(certificate : CreateCertificate) {
    return this.http.post(`${environment.baseUrl}/${environment.certificate}/${environment.createCertificate}`, certificate);
  }
}
