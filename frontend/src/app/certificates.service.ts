import { DownloadCertificate } from './model/certificates/DownloadCertificate';
import { environment } from './../environments/environment';
import { Observable } from 'rxjs';
import { SigningCertificate } from './model/certificates/SigningCertificate';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { CreateCertificate } from './model/certificates/CreateCertificate';
import { PossibleKeyUsages } from './model/certificates/PossibleKeyUsages';

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

  getCaCertificatesByUser(): Observable<SigningCertificate[]> {
    return this.http.get<SigningCertificate[]>(`${environment.baseUrl}/${environment.certificate}/${environment.getCaCertificatesByUser}`)
  }


  getEeCertificatesByUser(): Observable<SigningCertificate[]> {
    return this.http.get<SigningCertificate[]>(`${environment.baseUrl}/${environment.certificate}/${environment.getEndEntityCertificatesByUser}`)
  }

  getPossibleKeyUsages(alias: String) : Observable<PossibleKeyUsages>{
    return this.http.get<PossibleKeyUsages>(`${environment.baseUrl}/${environment.certificate}/${environment.getPossibleKeyUsages}?alias=${alias}`)
  }
  revokeCertificate(serialNumber : String) {
    return this.http.post(`${environment.baseUrl}/${environment.certificate}/${environment.revokeCertificate}`, serialNumber);
  }

  downloadCertificate(download : DownloadCertificate){
    return this.http.post(`${environment.baseUrl}/${environment.certificate}/${environment.downloadCertificate}`, download);
  }

  saveRootCertificate(certificate : CreateCertificate) {
    return this.http.post(`${environment.baseUrl}/${environment.certificate}/${environment.createRootCertificate}`, certificate);
  }

}
