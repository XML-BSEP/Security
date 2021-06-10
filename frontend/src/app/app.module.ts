import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HomeComponent } from './home/home.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MaterialModule } from './material-module';
import { CreateCertificateComponent } from './create-certificate/create-certificate.component';
import { AllCertificatesComponent } from './all-certificates/all-certificates.component';
import { LoginComponent } from './login/login.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { ChooseIssuerDialogComponent } from './dialogs/choose-issuer-dialog/choose-issuer-dialog.component';
import { SavedTemplatesDialogComponent } from './dialogs/saved-templates-dialog/saved-templates-dialog.component';
import { ErrorInterceptor, JwtInterceptor } from './helpers';
import { ToastrModule } from 'ngx-toastr';
import {RegisterComponent} from '../app/register/register.component';
import { RegistrationConfirmationComponent } from './registration-confirmation/registration-confirmation.component';
import { ForgotPasswordComponent } from './forgot-password/forgot-password.component'
@NgModule({
  declarations: [
    AppComponent,

    HomeComponent,
    CreateCertificateComponent,
    AllCertificatesComponent,
    LoginComponent,
    ChooseIssuerDialogComponent,
    SavedTemplatesDialogComponent,
    RegisterComponent,
    RegistrationConfirmationComponent,
    ForgotPasswordComponent,

  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    MaterialModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    ToastrModule.forRoot()
  ],
  providers: [{provide : HTTP_INTERCEPTORS, useClass : JwtInterceptor, multi : true},
              {provide : HTTP_INTERCEPTORS, useClass : ErrorInterceptor, multi : true}],
  bootstrap: [AppComponent]
})
export class AppModule { }
