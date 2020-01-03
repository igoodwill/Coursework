import { Component, OnInit } from '@angular/core';
import { Coursework } from '../model/coursework';
import { CourseworkService } from '../service/coursework.service';
import { MatDialog } from '@angular/material';
import { ActivatedRoute } from '@angular/router';
import { CourseworkDialogComponent } from '../dialog/coursework/coursework.dialog';
import { FileUploadComponent } from '../dialog/file-upload/file-upload.component';

@Component({
  selector: 'app-coursework',
  templateUrl: './coursework.component.html',
  styleUrls: ['./coursework.component.css']
})
export class CourseworkComponent implements OnInit {

  public coursework: Coursework;

  constructor(
    private $courseworkService: CourseworkService,
    private dialog: MatDialog,
    private route: ActivatedRoute
  ) {
  }

  public ngOnInit() {
    this.$courseworkService
      .get(this.route.snapshot.params['id'])
      .subscribe((coursework: Coursework) => this.coursework = coursework);
  }

  public edit(): void {
    this.dialog.open(CourseworkDialogComponent, {
      data: { ...this.coursework }
    }).afterClosed()
      .subscribe(result => {
        if (result) {
          this.ngOnInit();
        }
      });
  }

  public uploadFile(): void {
    this.dialog.open(FileUploadComponent).afterClosed().subscribe(file => {
      if (file) {
        this.$courseworkService.uploadFile(this.coursework.id, file).subscribe(this.ngOnInit.bind(this));
      }
    });
  }
}
