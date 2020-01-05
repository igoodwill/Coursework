import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { AppRoutingModule } from './app-routing.module';
import { CourseworksComponent } from './courseworks/courseworks.component';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { ApiInterceptor } from './interceptor/api-interceptor';
import { MsAdalAngular6Module } from 'microsoft-adal-angular6';
import { AuthInterceptor } from './interceptor/auth-interceptor.service';
import {
  MatButtonModule,
  MatCardModule,
  MatDialogModule,
  MatFormFieldModule,
  MatIconModule,
  MatInputModule,
  MatPaginatorModule,
  MatSortModule,
  MatTableModule,
  MatToolbarModule,
  MatTooltipModule
} from '@angular/material';
import { FormsModule } from '@angular/forms';
import { CourseworkDialogComponent } from './dialog/coursework/coursework.dialog';
import { FileUploadComponent } from './dialog/file-upload/file-upload.component';
import { CourseworkRequestsComponent } from './coursework-requests/coursework-requests.component';
import { CourseworkRequestDialogComponent } from './dialog/coursework-request/coursework-request.dialog';
import { RejectCourseworkRequestDialogComponent } from './dialog/reject-coursework-request/reject-coursework-request.dialog';
import { CloseCourseworkRequestDialogComponent } from './dialog/close-coursework-request/close-coursework-request.dialog';
import { CourseworkComponent } from './coursework/coursework.component';

@NgModule({
  declarations: [
    AppComponent,
    CourseworksComponent,
    CourseworkRequestsComponent,
    CourseworkDialogComponent,
    CourseworkRequestDialogComponent,
    RejectCourseworkRequestDialogComponent,
    CloseCourseworkRequestDialogComponent,
    FileUploadComponent,
    CourseworkComponent
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
    AppRoutingModule,
    MatFormFieldModule,
    MatIconModule,
    FormsModule,
    MatInputModule,
    MatButtonModule,
    MatDialogModule,
    MatPaginatorModule,
    MatTableModule,
    MatSortModule,
    MatToolbarModule,
    MatCardModule,
    MatTooltipModule
  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: ApiInterceptor, multi: true },
    { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true }
  ],
  bootstrap: [AppComponent],
  entryComponents: [
    CourseworkDialogComponent,
    CourseworkRequestDialogComponent,
    RejectCourseworkRequestDialogComponent,
    CloseCourseworkRequestDialogComponent,
    FileUploadComponent
  ]
})
export class AppModule {
}
