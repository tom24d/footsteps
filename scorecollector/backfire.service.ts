import { Injectable } from '@angular/core';
import {Observable} from 'rxjs';
import {AngularFireAuth} from '@angular/fire/auth';
import * as firebase from 'firebase';
import {environment} from '../environments/environment';
import {Competition} from './new/new.component';
import {AngularFirestore, AngularFirestoreCollection} from '@angular/fire/firestore';
import { QuerySnapshot } from '@angular/fire/firestore';
import {HttpClient} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class BackfireService {

  user: Observable<firebase.User>;

  currentlyLogin = false;

  usersCollection: AngularFirestoreCollection;

  currentSCUser;

  dbCompePath = 'competitions';

  refreshDone = false;

  callBackAfterLogin = [];

  constructor(private ngFireAuth: AngularFireAuth, private fireSer: BackfireService, private db: AngularFirestore,
              private http: HttpClient) {
    this.login();
    this.subscribeAuthChange();
  }

  login() {
    this.user = this.ngFireAuth.authState;
  }

  async logout() {
    this.ngFireAuth.auth.signOut();
  }

  gapiClientLoad(callBack) {
    gapi.load('client', callBack);
  }
  gapiAuth2Load(callBack) {
    gapi.load('auth2', callBack);
  }

  subscribeAuthChange() {
    firebase.auth().onAuthStateChanged(user => {
      console.log('auth changed');
      if (user) {
        this.usersCollection = this.db.collection<SCUser>('users', ref => ref.where('uid', '==',
          firebase.auth().currentUser.uid));
        this.getUserUidWhicheverExistsInStore();

        gapi.load('client', () => {
            gapi.client.init({
              apiKey: environment.firebase.apiKey,
              discoveryDocs: environment.firebase.discoveryDocs,
              clientId: environment.firebase.clientId,
              scope: environment.firebase.scopes.join(' '),
            }).then(() => {
              this.currentlyLogin = gapi.auth2.getAuthInstance().isSignedIn.get();
              console.log(gapi.auth2.getAuthInstance().currentUser.get().getGrantedScopes());
              console.log('gapi Login status: '.concat(String(this.currentlyLogin)));
              if (this.currentlyLogin) {
                this.callBackAfterLogin.forEach(value => value());
              }
              if (!this.currentlyLogin || !gapi.auth2.getAuthInstance().currentUser.get().getGrantedScopes().match(/spreadsheets/)) {
                gapi.auth2.getAuthInstance().signIn({
                  scope: environment.firebase.scopes.join(' '),
                  ux_mode: 'redirect',
                  prompt: 'consent'
                });
              }
            }, (reason) => {
              console.error(reason);
            });
        });
      }
    });
  }

  reAuth() {
    this.gapiAuth2Load(() => {
      gapi.auth2.init({
        client_id: environment.firebase.clientId,
        scope: environment.firebase.scopes.join(' ')
      }).then((googleAuth) => {
        console.log(googleAuth.isSignedIn.get());
        console.log(googleAuth.currentUser.get().getGrantedScopes());
      });
    });
  }

  isValidUserCurrentlyOnBrowser() {
    return (firebase.auth().currentUser);
  }

  addCompe(compe: Competition, callBack) {
    console.log('addcompe()');
    this.usersCollection.doc<SCUser>(this.currentSCUser.id).collection<Competition>(this.dbCompePath).add(compe).then(docRef => {
      callBack(docRef.id); // return compe id
    });
  }

  updateSubmissionLinkToCompe(link: string, compe) {
    compe.submissionLink = link;
    this.usersCollection.doc<SCUser>(this.currentSCUser.id).collection<Competition>(this.dbCompePath).doc<Competition>(compe.id)
      .set(compe);
  }

  getSubmissionLinkSub(userId, compeId) {
    const url = environment.rootUrl.concat('/submit/'.concat(userId, '/', compeId));
    return this.http.post(environment.dynamicLinkUrl,
      {dynamicLinkInfo: {domainUriPrefix: 'https://scoreform.page.link', link: url}, suffix: {option: 'SHORT'}});
  }

  getUserUidWhicheverExistsInStore() {
    this.usersCollection.get().subscribe((result: QuerySnapshot<SCUser>) => {
      if (result.empty) {
        console.log('add current user to firestore');
        this.currentSCUser = {uid: firebase.auth().currentUser.uid};
        this.usersCollection.add(this.currentSCUser).then(value => this.currentSCUser.id = value.id);
      } else {
        console.log('found in firestore size: ' + result.size);
        this.currentSCUser = result.docs[0].data();
        this.currentSCUser.id = result.docs[0].id;
      }
      console.log('user auth successful with firestore');
      console.log(this.currentSCUser.uid);
      console.log(this.currentSCUser.id);
      this.refreshDone = true;
    });
  }

  getAllCompetitions(): AngularFirestoreCollection<Competition> {
    if (this.isValidUserCurrentlyOnBrowser()) {
      if (!this.refreshDone) {
        return this.getAllCompetitions();
      }
      console.log('getAllCompetitions return');
      return this.usersCollection.doc<SCUser>(this.currentSCUser.id).collection<Competition>(this.dbCompePath);
    }
    console.log('no valid user');
  }

  deleteCompetition(c: Competition) {
    this.usersCollection.doc<SCUser>(this.currentSCUser.id).collection<Competition>(this.dbCompePath).doc(c.id).delete();
  }

  registerCallBackAfterLogin(callback) {
    this.callBackAfterLogin.push(callback);
  }
}

export interface SCUser {
  id?: string;
  uid: string;
}
