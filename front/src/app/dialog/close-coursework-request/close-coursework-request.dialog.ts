import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material';
import { CourseworkRequest } from '../../model/coursework-request';
import { CourseworkRequestService } from '../../service/coursework-request.service';

@Component({
  selector: 'app-close-coursework-request-dialog',
  templateUrl: './close-coursework-request.dialog.html',
  styleUrls: ['./close-coursework-request.dialog.css']
})
export class CloseCourseworkRequestDialogComponent {

  constructor(
    public dialogRef: MatDialogRef<CloseCourseworkRequestDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: CourseworkRequest,
    private $courseworkRequestService: CourseworkRequestService
  ) {
  }

  public closeRequest(): void {
    this.$courseworkRequestService
      .close(this.data.id, this.data.comment)
      .subscribe(() => this.close(true));
  }

  public close(success?: boolean): void {
    this.dialogRef.close(success);
  }
}
