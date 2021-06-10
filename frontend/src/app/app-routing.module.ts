import { ForgotPasswordComponent } from './forgot-password/forgot-password.component';
import { RegisterComponent } from './register/register.component';
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { AllCertificatesComponent } from './all-certificates/all-certificates.component';
import { CreateCertificateComponent } from './create-certificate/create-certificate.component';
import { AuthGuard } from './helpers';
import { HomeComponent } from './home/home.component';
import { LoginComponent } from './login/login.component';
import { Role } from './model/user/role';
import { RegistrationConfirmationComponent } from './registration-confirmation/registration-confirmation.component';

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
  component: CreateCertificateComponent,
  //canActivate : [AuthGuard],
  //data: {roles:[Role.Admin]}
},
{
  path: 'allCertificates',
  component: AllCertificatesComponent,
  // canActivate : [AuthGuard],
  // data: {roles:[Role.Admin]}
},
{
  path: 'register',
  component: RegisterComponent
},
{
  path:'regconfirm',
  component: RegistrationConfirmationComponent

},
{
  path: 'login',
  component: LoginComponent
},
{
  path:'forgotPassword',
  component: ForgotPasswordComponent
}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
