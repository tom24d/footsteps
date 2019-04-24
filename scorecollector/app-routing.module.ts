import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Routes, RouterModule } from '@angular/router';
import { AppComponent } from './app.component';
import { HomeComponent } from './home/home.component';
import { LoginComponent } from './login/login.component';
import { SubmitComponent } from './submit/submit.component';
import {DashboardComponent} from './dashboard/dashboard.component';
import { NewComponent} from './new/new.component';
import { HelpComponent} from './help/help.component';
import { SettingComponent} from './setting/setting.component';

const routes: Routes = [
  {path: 'home', component: HomeComponent},
  {path: 'login', component: LoginComponent},
  {path: 'dashboard', component: DashboardComponent},
  {path: 'submit/:userId/:compeId', component: SubmitComponent},
  {path: 'new', component: NewComponent},
  {path: 'setting', component: SettingComponent},
  {path: 'help', component: HelpComponent},
  {path: '', redirectTo: '/home', pathMatch: 'full'}];

@NgModule({
  imports: [RouterModule.forRoot(routes, {useHash: false, enableTracing: false})],
  exports: [RouterModule]
})


export class AppRoutingModule { }
