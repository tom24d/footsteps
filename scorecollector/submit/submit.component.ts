import { Component, OnInit } from '@angular/core';
import {ActivatedRoute} from '@angular/router';

@Component({
  selector: 'app-submit',
  templateUrl: './submit.component.html',
  styleUrls: ['./submit.component.css']
})
export class SubmitComponent implements OnInit {

  userId: string;
  compeId: string;
  myScore: Score;

  numOfHole = 18;

  myName;

  constructor(private route: ActivatedRoute) { }

  ngOnInit() {
    this.userId = this.route.snapshot.paramMap.get('userId');
    this.compeId = this.route.snapshot.paramMap.get('compeId');
  }

}

export interface Hole {
  id?: string;
  index: number;


  score: number;
  pad?: number;
}

export interface Score {

  id?: string;
  playerName?: string;
  isDefault: boolean;
  holes: Hole[];
}
