import { Component, OnInit } from '@angular/core';
import {SpreadsheetService} from '../spreadsheet.service';

@Component({
  selector: 'app-setting',
  templateUrl: './setting.component.html',
  styleUrls: ['./setting.component.css']
})
export class SettingComponent implements OnInit {

  constructor(private ss: SpreadsheetService) { }

  ngOnInit() {
    // this.ss.makingNewSpreadsheet();
  }

}
