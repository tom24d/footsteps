import {Component, Inject, OnInit} from '@angular/core';
import {GolfsearchService} from '../golfsearch.service';
import {SpreadsheetService} from '../spreadsheet.service';
import {environment} from '../../environments/environment';
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef, MatSnackBar} from '@angular/material';
import {MyDialogComponent} from '../my-dialog/my-dialog.component';
import {Score} from '../submit/submit.component';
import {AngularFirestore} from '@angular/fire/firestore';
import {BackfireService} from '../backfire.service';
import {FormControl} from '@angular/forms';
import {fromEvent} from 'rxjs';
import {fromObservable} from 'rxjs/internal/observable/fromObservable';
import {debounceTime, distinctUntilChanged, filter, map} from 'rxjs/operators';

@Component({
  selector: 'app-new',
  templateUrl: './new.component.html',
  styleUrls: ['./new.component.css']
})
export class NewComponent implements OnInit {

  keyword: string;

  compe;

  cname = '';

  suggestions: GolfCourse[];

  selectedCourse;

  myFormCtrl = new FormControl();

  constructor(private golfsearch: GolfsearchService, private ss: SpreadsheetService, private dialog: MatDialog,
              private snackBar: MatSnackBar, private back: BackfireService) {
  }

  ngOnInit() {
    this.myFormCtrl.valueChanges.pipe(
      filter(value => value.length > 1),
      debounceTime(550),
      distinctUntilChanged()
    ).subscribe(value => {
      this.keyword = value;
      console.log(value);
      this.searchignit();
    });
  }

  searchignit() {
    if (!this.keyword) {
      this.keyword = '　';
    }
    console.log('search ignition keyword: '.concat(this.keyword));

    this.golfsearch.search(this.keyword).subscribe( (value: Root) => {
      // this.cands = value; // スプレッド演算子
      this.suggestions = [];
      value.Items.forEach(value1 => this.suggestions.push(value1.Item));
      // console.log(value.Items[0].Item.golfCourseName); // json parse check(correct)
    });

  }

  makingCompe() {
    if (!this.selectedCourse) {
      console.log('select course');
      return;
    }
    this.snackBar.open('Just a moment..', null, {
      duration: 1500,
      horizontalPosition: 'right',
      verticalPosition: 'bottom'
    });

    // TODO should be modified. make a sheets if user request to move db to the sheets, not now.
    this.back.addCompe(this.compe, compeId => {
      this.compe.id = compeId;
      this.compe.competitionName = this.cname;
      this.back.getSubmissionLinkSub(this.back.currentSCUser.id, compeId).subscribe( (value: ShortenResponse) => {
        this.back.updateSubmissionLinkToCompe(value.shortLink, this.compe);
        const dialogRefs = this.dialog.open(MyDialogComponent, {
          data: {showUrl: value.shortLink}
        });
        dialogRefs.afterClosed().subscribe(res => {
          console.log('dialog closed');
        });
      });
    });
    // the compe and the scores saved in Firestore until all attendants submission finished.
    /*
    this.ss.makeNewSpreadsheet(this.compeNames, result => {
      console.log('spreadsheets api called');
      this.ss.shortenUrl('https://scorecollector.herokuapp.com/submit/'.concat(result.result.spreadsheetId))
        .subscribe((value: ShortenResponse) => {
          const dialogRefs = this.dialog.open(MyDialogComponent, {
            data: {showUrl: value.shortLink}
          });
          dialogRefs.afterClosed().subscribe(res => {
            console.log('dialog closed');
          });
      });
      console.log(result.result.spreadsheetId);
    });
    */
  }

  selectSug(courseId, courseName) {
    console.log('selected courseId: '.concat(courseId));
    this.selectedCourse = {
      id: courseId,
      golfCourseName: courseName
    };
    this.compe = {
      course: this.selectedCourse,
      score: []
    };
    this.keyword = courseName;
    this.myFormCtrl.setValue(courseName);
  }

}

export interface GolfCourse {
  id?: string;
  golfCourseId: number;
  golfCourseName: string;
  parScore?: Score;
}

export interface Items {
  Item: GolfCourse;
}
export interface Root extends Object {
  Items: Items[];
}
export interface DialogData {
  spreadsheetId?: string;
  showUrl?: string;
}
export interface ShortenResponse {
  shortLink?: string;
  previewLink?: string;
}
export interface Competition {

  id?: string;
  course: GolfCourse;
  submissionLink?: string;

  competitionName: string;

  score: Score[];
}
