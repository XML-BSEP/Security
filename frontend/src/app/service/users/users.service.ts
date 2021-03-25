import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { User } from 'src/app/model/user/user';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class UsersService {

  constructor(private http : HttpClient) { }


  getAll() : Observable<User[]>{
    return this.http.get<User[]>(`${environment.baseUrl}/${environment.users}/${environment.findAll}`);
  }

}
