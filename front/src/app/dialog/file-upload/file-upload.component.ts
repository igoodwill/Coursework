import { Component } from '@angular/core';
import { MatDialogRef } from '@angular/material';

@Component({
  selector: 'app-file-upload',
  templateUrl: './file-upload.component.html',
  styleUrls: ['./file-upload.component.css']
})
export class FileUploadComponent {

  public fileData: File = null;

  constructor(
    public dialogRef: MatDialogRef<FileUploadComponent>
  ) {
  }

  public fileProgress(fileInput: any): void {
    this.fileData = <File>fileInput.target.files[0];
  }

  public upload(): void {
    this.close(this.fileData);
  }

  public close(file?: File): void {
    this.dialogRef.close(file);
  }
}
