import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material';
import { CourseworkRequest } from '../../model/coursework-request';
import { CourseworkRequestService } from '../../service/coursework-request.service';

@Component({
  selector: 'app-reject-coursework-request-dialog',
  templateUrl: './reject-coursework-request.dialog.html',
  styleUrls: ['./reject-coursework-request.dialog.css']
})
export class RejectCourseworkRequestDialogComponent {

  constructor(
    public dialogRef: MatDialogRef<RejectCourseworkRequestDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: CourseworkRequest,
    private $courseworkRequestService: CourseworkRequestService
  ) {
  }

  public reject(): void {
    this.$courseworkRequestService
      .reject(this.data.id, this.data.comment)
      .subscribe(() => this.close(true));
  }

  public close(success?: boolean): void {
    this.dialogRef.close(success);
  }
}
