import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { Template } from 'src/app/model/certificates/Template';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class TemplateService {

  constructor(private http: HttpClient) { }

  saveTemplate(template: Template) {
    return this.http.post(`${environment.baseUrl}/${environment.template}/${environment.add}`,template, {responseType : 'text'});
    // return this.http.post<Template>(`${environment.baseUrl}/${environment.api}/${environment.template}/${environment.add}`, template, {responseType : 'text'});
  }

  getAll() : Observable<Template[]>{
    return this.http.get<Template[]>(`${environment.baseUrl}/${environment.template}/${environment.findAll}`);
  }

}
