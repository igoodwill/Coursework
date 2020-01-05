import { Component, OnInit } from '@angular/core';
import { AuthService } from './service/auth.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {

  constructor(private $authService: AuthService) {
  }

  public ngOnInit(): void {
    this.$authService.init();
  }

  public logout(): void {
    this.$authService.logout();
  }
}
