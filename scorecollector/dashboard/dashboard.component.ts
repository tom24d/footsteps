import { Component, OnInit } from '@angular/core';
import {BackfireService} from '../backfire.service';
import {Observable} from 'rxjs';
import {Competition} from '../new/new.component';
import {QuerySnapshot} from '@angular/fire/firestore';
import {map} from 'rxjs/operators';
import {MatSnackBar} from '@angular/material';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {

  user: Observable<firebase.User>;

  competitions: Competition[];

  constructor(private backFireService: BackfireService, private sb: MatSnackBar) { }

  ngOnInit() {
    this.user = this.backFireService.user;
    this.backFireService.registerCallBackAfterLogin(() => {
      this.backFireService.getAllCompetitions().snapshotChanges().pipe(
        map(actions => {
          return actions.map( a => {
            const data = a.payload.doc.data();
            const id = a.payload.doc.id;
            console.log('db change detected');
            return {id, ...data};
          });
        })
      ).subscribe(compe => {
        this.competitions = compe;
        console.log(this.competitions.length);
        this.competitions.forEach(value => console.log(value.competitionName));
      });
    });
  }

  deleteCompetition(c: Competition) {
    const index = this.competitions.findIndex((item) => item.id === c.id);
    this.competitions.splice(index, 1);
    const sbRef = this.sb.open('deleting...', 'cancel', {
      duration: 4000
    });

    sbRef.afterDismissed().subscribe(value => {
      this.backFireService.deleteCompetition(c);
    });

    sbRef.onAction().subscribe(value => {
      this.competitions.splice(index, 0, c);
    });
  }

}
