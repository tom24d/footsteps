import { Injectable } from '@angular/core';
import {HttpClient, HttpClientModule, HttpHeaders} from '@angular/common/http';
import {environment} from '../environments/environment';
import {BackfireService} from './backfire.service';
import * as backgapi from '../assets/backgapi.js';
import spreadsheets = gapi.client.spreadsheets;
import {MatSnackBar} from '@angular/material';

declare function Create(props): void;

@Injectable({
  providedIn: 'root'
})
export class SpreadsheetService {

  spreadsheets = {};

  constructor(private http: HttpClient, private fire: BackfireService) {
  }

  makeNewSpreadsheet(title, callBack) {
    console.log('try to make a sheet.');
    this.gapiSheetsLoad(() => {
      console.log('gapi Sheets loaded');
      backgapi.Create({
        properties: {
          title
        }
      }, res => {
        this.spreadsheets = res;
        callBack(this.spreadsheets);
      });
    });
  }

  gapiClientLoad(callBack) {
    console.log('gapi Client Load');
    gapi.load('client', callBack);
  }

  gapiSheetsLoad(callBack) {
    this.gapiClientLoad(() => {
      gapi.client.load('sheets', 'v4' , callBack);
    });
    console.log('gapi Sheets Load func');
  }
}
