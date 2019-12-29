import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { AppRoutingModule } from './app-routing/app-routing.module';
import { HomeComponent } from './home/home.component';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { ApiInterceptor } from './interceptor/api-interceptor';
import { AuthenticationGuard, MsAdalAngular6Module } from 'microsoft-adal-angular6';
import { AuthInterceptor } from './interceptor/auth-interceptor.service';

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    HttpClientModule,
    MsAdalAngular6Module.forRoot({
      tenant: '4eb588ae-cf23-4c01-94e4-4c0f17d5effd',
      clientId: '9fa052a5-63f4-4daa-bf42-4f01ddec5f33',
      endpoints: {
        'api': 'api://c7a9cebb-acac-400a-9159-a78dc7d104f0'
      },
      redirectUri: window.location.origin,
      navigateToLoginRequestUrl: true,
      cacheLocation: 'localStorage'
    }),
    AppRoutingModule
  ],
  providers: [
    AuthenticationGuard,
    { provide: HTTP_INTERCEPTORS, useClass: ApiInterceptor, multi: true },
    { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true }
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
