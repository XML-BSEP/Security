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
import { HttpClientModule } from '@angular/common/http';
import { ChooseIssuerDialogComponent } from './dialogs/choose-issuer-dialog/choose-issuer-dialog.component';

@NgModule({
  declarations: [
    AppComponent,

    HomeComponent,
    CreateCertificateComponent,
    AllCertificatesComponent,
    LoginComponent,
    ChooseIssuerDialogComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    MaterialModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
