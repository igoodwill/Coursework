import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CourseworksComponent } from './courseworks/courseworks.component';
import { AuthenticationGuard } from 'microsoft-adal-angular6';
import { CourseworkRequestsComponent } from './coursework-requests/coursework-requests.component';

const routes: Routes = [
  {
    path: 'courseworks',
    component: CourseworksComponent,
    canActivate: [AuthenticationGuard]
  }, {
    path: 'coursework-requests',
    component: CourseworkRequestsComponent,
    canActivate: [AuthenticationGuard]
  }, {
    path: '**',
    redirectTo: 'courseworks'
  }
];

@NgModule({
  declarations: [],
  imports: [
    RouterModule.forRoot(routes)
  ],
  exports: [
    RouterModule
  ]
})
export class AppRoutingModule {
}
