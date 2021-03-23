import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Template } from 'src/app/model/certificates/Template';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class TemplateService {

  constructor(private http: HttpClient, private router : Router) { }

  saveTemplate(template: Template) {
    return this.http.post<Template>(`${environment.baseUrl}/${environment.templates}`, template);
  }
}
