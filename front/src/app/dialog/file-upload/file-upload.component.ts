import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material';
import { ApiService } from '../../service/api.service';

@Component({
  selector: 'app-file-upload',
  templateUrl: './file-upload.component.html',
  styleUrls: ['./file-upload.component.css']
})
export class FileUploadComponent {

  public fileData: File = null;

  constructor(
    public dialogRef: MatDialogRef<FileUploadComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { courseworkId: string },
    private $apiService: ApiService
  ) {
  }

  public fileProgress(fileInput: any): void {
    this.fileData = <File>fileInput.target.files[0];
  }

  public upload(): void {
    this.$apiService.uploadFile(this.data.courseworkId, this.fileData).subscribe(() => this.close(true));
  }

  public close(success?: boolean): void {
    this.dialogRef.close(success);
  }
}
