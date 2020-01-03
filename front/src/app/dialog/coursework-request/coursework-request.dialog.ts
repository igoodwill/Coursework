import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material';
import { CourseworkRequest } from '../../model/coursework-request';
import { CourseworkRequestService } from '../../service/coursework-request.service';

@Component({
  selector: 'app-coursework-request-dialog',
  templateUrl: './coursework-request.dialog.html',
  styleUrls: ['./coursework-request.dialog.css']
})
export class CourseworkRequestDialogComponent {

  constructor(
    public dialogRef: MatDialogRef<CourseworkRequestDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: CourseworkRequest,
    private $courseworkRequestService: CourseworkRequestService
  ) {
  }

  public save(): void {
    this.$courseworkRequestService.save(this.data).subscribe(() => this.close(true));
  }

  public close(success?: boolean): void {
    this.dialogRef.close(success);
  }
}
