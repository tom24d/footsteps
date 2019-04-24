import { Component, OnInit, DoCheck } from '@angular/core';
import { AngularFireAuth} from '@angular/fire/auth';
import { FirebaseUISignInSuccessWithAuthResult, FirebaseUISignInFailure} from 'firebaseui-angular';
import {Observable} from 'rxjs';
import {Location} from '@angular/common';
import {BackfireService} from '../backfire.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  user: Observable<firebase.User>;

  constructor(private ngFireAuth: AngularFireAuth, private location: Location, private backendFire: BackfireService,
              private router: Router) { }

  ngOnInit() {
    this.backendFire.login();
    console.log('LoginComponent ngInit.');
    this.user = this.ngFireAuth.user;
    this.user.subscribe(user => {
      if (user) {
        this.gotoDashboard();
      }
    });
  }

  async logout() {
    this.backendFire.logout();
  }

  successSignIn(signInData: FirebaseUISignInSuccessWithAuthResult) {
    console.log('singnin successful');
    this.backendFire.currentSCUser = {
      uid: signInData.authResult.user.uid
    };
    this.backendFire.login();
    this.gotoDashboard();
  }

  onSignIn() {


  }

  gotoDashboard() {
    this.router.navigateByUrl('/dashboard');
  }

  async errorSignIn(error: FirebaseUISignInFailure) {
    console.log(error);
  }
}
