import {ChangeDetectorRef, Component, ViewChild, OnInit} from '@angular/core';
import {MatButton, MatSidenav, MatSnackBar} from '@angular/material';
import {MediaMatcher} from '@angular/cdk/layout';
import {Location} from '@angular/common';
import {BackfireService} from './backfire.service';
import {Router} from '@angular/router';
import {environment} from '../environments/environment';
import * as firebase from 'firebase';


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})

export class AppComponent implements OnInit {

  title = 'Attestify';

  @ViewChild('snav') snav: MatSidenav;

  mobileQuery: MediaQueryList;

  private mymobileQueryListener: () => void;

  sideContents: SideItem[] = [{id: 0, name: ' Dashboard '}, {id: 1, name: ' Setting '}, {id: 2, name: ' Help '}];

  @ViewChild('loginButton') loginButton: MatButton;

  loggedin: boolean;

  loginButtonString = 'Login';

  constructor(private snackBar: MatSnackBar, changeDetectorRef: ChangeDetectorRef,
              media: MediaMatcher, private location: Location, private backService: BackfireService,
              private router: Router) {
    this.mobileQuery = media.matchMedia('(max-width: 600px)');
    this.mymobileQueryListener = () => changeDetectorRef.detectChanges();
    this.mobileQuery.addListener(this.mymobileQueryListener);
/*
    window.onLoadCallback = () => {
      gapi.load('client:auth2', () => {
        gapi.auth2.init({
          client_id: environment.firebase.clientId,
          fetch_basic_profile: true,
          scope: 'profile'
        });
      });
    };
*/
  }

  ngOnInit() {
  this.observeIfLogin();
  }

  openSnackBar() {
    this.snackBar.open('Hello snackBar', null, {
      duration: 1000,
      horizontalPosition: 'right',
      verticalPosition: 'bottom'
    });
  }

  sidechoose(nav: SideItem) {
    if (nav.id === 0) {
      this.router.navigateByUrl('/dashboard');
    }
    if (nav.id === 1) {
      this.router.navigateByUrl('/setting');

    }
    if (nav.id === 2) {
      this.router.navigateByUrl('/help');

    }

    this.snav.close();

  }

  observeIfLogin() {
    this.backService.user.subscribe(user => {
      if (user) {
        this.loggedin = true;
        console.log('Logged in. UID: '.concat(String(user.uid)));
        this.loginButton.color = 'warn';
        this.loginButtonString = 'Sign Out';
      } else {
        this.loggedin = false;
        console.log('Not Logged in');
        this.loginButton.color = 'accent';
        this.loginButtonString = 'Login';

      }
    });
  }

  onclickLogin() {

    if (!this.loggedin) {
      this.router.navigateByUrl('/login');
      console.log('onclicklogin(no user)');
    } else {
      this.router.navigateByUrl('');
      console.log('onclicklogin(user exists)');
      this.backService.logout();
    }
  }
}

export interface SideItem {
  id?: number;
  name: string;

}
