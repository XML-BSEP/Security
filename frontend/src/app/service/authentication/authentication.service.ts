import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { BehaviorSubject, Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { AuthenticatedUser } from 'src/app/model/user/authenticatedUser';
import { Authentication } from '../../model/user/authentication';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {
  private currentUserSubject: BehaviorSubject<AuthenticatedUser>;
  public currentUser: Observable<AuthenticatedUser>;

  constructor(private http: HttpClient, private router : Router) {
      this.currentUserSubject = new BehaviorSubject<AuthenticatedUser>(JSON.parse(localStorage.getItem('currentUser')));
      this.currentUser = this.currentUserSubject.asObservable();
  }

  public get currentUserValue(): AuthenticatedUser {
      return this.currentUserSubject.value;
  }

public getUserValue() : AuthenticatedUser {
    console.log("Token" + this.currentUserSubject.value.token.accessToken);
    return this.currentUserSubject.value;
}

login(credentials: Authentication){

  
    return this.http.post<AuthenticatedUser>(`${environment.baseUrl}/${environment.auth}/${environment.login}`, credentials)
    .pipe(map(response => {
      localStorage.setItem('currentUser', JSON.stringify(response));
      console.log(response.token);
      this.currentUserSubject.next(response);
      return response;
    }));
}

logout() {
      // remove user from local storage to log user out
      localStorage.removeItem('currentUser');
      localStorage.removeItem('userId');
      this.currentUserSubject.next(null);
      this.router.navigate(['/']);
}
}