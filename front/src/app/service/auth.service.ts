import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private currentUserAdmin = false;

  constructor(private httpClient: HttpClient) {
  }

  public init(): void {
    this.httpClient
      .get<boolean>('user/isAdmin')
      .subscribe(value => this.currentUserAdmin = value);
  }

  public isCurrentUserAdmin(): boolean {
    return this.currentUserAdmin;
  }
}
