import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { AllCertificatesComponent } from './all-certificates/all-certificates.component';
import { CreateCertificateComponent } from './create-certificate/create-certificate.component';
import { HomeComponent } from './home/home.component';

const routes: Routes = [
  { 
    path: '',
    redirectTo: 'home',
    pathMatch: 'full'
},
{
  path: 'home',
  component: HomeComponent
},
{
  path: 'createCertificate',
  component: CreateCertificateComponent
},
{
  path: 'allCertificates',
  component: AllCertificatesComponent
}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
