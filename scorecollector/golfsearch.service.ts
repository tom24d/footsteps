import { Injectable } from '@angular/core';
import {HttpClient, HttpClientModule, HttpHeaders} from '@angular/common/http';
import { environment } from '../environments/environment';
import {Items, Root} from './new/new.component';

const httpOptions = {
  headers: new HttpHeaders()
};

const configUrl = 'https://app.rakuten.co.jp/services/api/Gora/GoraGolfCourseSearch/20170623?'
+ 'format=' + environment.gora.format +
  '&applicationId=' + environment.gora.applicationId +
  '&application_secret=' + environment.gora.application_secret +
  '&affiliateId=' + environment.gora.affiliateId +
  '&elements=' + environment.gora.elements;

@Injectable({
  providedIn: 'root'
})

export class GolfsearchService {

  constructor(private http: HttpClient) {
    console.log('constructor');
  }

  search(keyword: string) {
      const str = configUrl.concat('&keyword=' + encodeURIComponent(keyword));
      console.log(str);
      return this.http.get<Root>(str, httpOptions);
  }

}
