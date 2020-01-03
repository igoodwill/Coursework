import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material';
import { Coursework } from '../../model/coursework';
import { CourseworkService } from '../../service/coursework.service';

@Component({
  selector: 'app-coursework-dialog',
  templateUrl: './coursework.dialog.html',
  styleUrls: ['./coursework.dialog.css']
})
export class CourseworkDialogComponent {

  constructor(
    public dialogRef: MatDialogRef<CourseworkDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: Coursework,
    private $courseworkService: CourseworkService
  ) {
  }

  public save(): void {
    this.$courseworkService.save(this.data).subscribe(() => this.close(true));
  }

  public close(success?: boolean): void {
    this.dialogRef.close(success);
  }
}
