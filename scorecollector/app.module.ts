import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import { BrowserAnimationsModule} from '@angular/platform-browser/animations';
import { environment } from '../environments/environment';
import { HttpClientModule} from '@angular/common/http';

import {
  MatAutocompleteModule,
  MatCardModule,
  MatDialogModule,
  MatInputModule,
  MatProgressSpinnerModule,
  MatSlideToggleModule
} from '@angular/material';
import {MatButtonModule, MatCheckboxModule} from '@angular/material';
import {MatToolbarModule} from '@angular/material/toolbar';
import {MatSnackBarModule} from '@angular/material/snack-bar';
import {MatIconModule} from '@angular/material';
import {MatSidenavModule} from '@angular/material/sidenav';
import {MatListModule} from '@angular/material';

import { AngularFireModule } from '@angular/fire';
import { AngularFireAuthModule } from '@angular/fire/auth';
import { AngularFirestoreModule } from '@angular/fire/firestore';
import { FirebaseUIModule, firebase, firebaseui } from 'firebaseui-angular';

import { AppComponent } from './app.component';
import { HomeComponent } from './home/home.component';
import { LoginComponent } from './login/login.component';
import { AppRoutingModule } from './app-routing.module';
import { SubmitComponent } from './submit/submit.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { NewComponent} from './new/new.component';
import { HelpComponent } from './help/help.component';
import { SettingComponent } from './setting/setting.component';
import { MyDialogComponent } from './my-dialog/my-dialog.component';


const firebaseUIAuthConfig: firebaseui.auth.Config = {
  autoUpgradeAnonymousUsers: false,
  signInFlow: 'redirect',
  signInOptions: [{
    provider: firebase.auth.GoogleAuthProvider.PROVIDER_ID,
    scopes: environment.firebase.scopes,
    clientId: environment.firebase.clientId,
  }],
  tosUrl: 'google.com',
  privacyPolicyUrl: 'google.com',
  signInSuccessUrl: '/dashboard',
  credentialHelper: firebaseui.auth.CredentialHelper.NONE,
  siteName: 'Score Collector',

};

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    LoginComponent,
    SubmitComponent,
    DashboardComponent,
    NewComponent,
    HelpComponent,
    SettingComponent,
    MyDialogComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    HttpClientModule,
    AngularFireModule.initializeApp(environment.firebase),
    AngularFireAuthModule,
    AngularFirestoreModule,
    FirebaseUIModule.forRoot(firebaseUIAuthConfig),
    MatInputModule,
    MatButtonModule,
    MatCheckboxModule,
    MatToolbarModule,
    MatSnackBarModule,
    MatIconModule,
    MatSidenavModule,
    MatListModule,
    MatDialogModule,
    MatProgressSpinnerModule,
    MatCardModule,
    MatSlideToggleModule,
    ReactiveFormsModule,
    MatAutocompleteModule
  ],
  providers: [],
  bootstrap: [AppComponent],
  entryComponents: [MyDialogComponent]
})
export class AppModule { }
