import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Coursework } from '../model/coursework';

@Injectable({
  providedIn: 'root'
})
export class ApiService {

  constructor(private httpClient: HttpClient) {
  }

  public save(coursework: Coursework): Observable<any> {
    return this.httpClient.post('coursework', coursework);
  }
}
