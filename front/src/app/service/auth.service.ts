import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { MsAdalAngular6Service } from 'microsoft-adal-angular6';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private currentUserAdmin = false;

  constructor(
    private httpClient: HttpClient,
    private $adalService: MsAdalAngular6Service
  ) {
  }

  public init(): void {
    if (this.$adalService.isAuthenticated) {
      this.httpClient
        .get<boolean>('user/isAdmin')
        .subscribe(value => this.currentUserAdmin = value);
    } else {
      this.$adalService.login();
    }
  }

  public logout(): void {
    this.$adalService.logout();
  }

  public isCurrentUserAdmin(): boolean {
    return this.currentUserAdmin;
  }
}
