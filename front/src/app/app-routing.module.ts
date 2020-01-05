import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CourseworksComponent } from './courseworks/courseworks.component';
import { CourseworkRequestsComponent } from './coursework-requests/coursework-requests.component';
import { CourseworkComponent } from './coursework/coursework.component';

const routes: Routes = [
  {
    path: 'courseworks',
    component: CourseworksComponent
  }, {
    path: 'coursework-requests',
    component: CourseworkRequestsComponent
  }, {
    path: 'coursework/:id',
    component: CourseworkComponent
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
