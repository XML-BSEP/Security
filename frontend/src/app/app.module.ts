import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HomeComponent } from './home/home.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MaterialModule } from './material-module';
import { CreateCertificateComponent } from './create-certificate/create-certificate.component';
import { AllCertificatesComponent } from './all-certificates/all-certificates.component';

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    CreateCertificateComponent,
    AllCertificatesComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    MaterialModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
