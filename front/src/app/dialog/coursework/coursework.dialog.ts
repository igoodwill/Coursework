import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material';
import { Coursework } from '../../model/coursework';
import { ApiService } from '../../service/api.service';

@Component({
  selector: 'app-coursework-dialog',
  templateUrl: './coursework.dialog.html',
  styleUrls: ['./coursework.dialog.css']
})
export class CourseworkDialogComponent {

  constructor(
    public dialogRef: MatDialogRef<CourseworkDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: Coursework,
    private $apiService: ApiService
  ) {
  }

  public save(): void {
    this.$apiService.save(this.data).subscribe(() => this.close(true));
  }

  public close(success?: boolean): void {
    this.dialogRef.close(success);
  }
}
