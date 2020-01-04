import { Component, OnInit } from '@angular/core';
import { MatDialog, MatTableDataSource } from '@angular/material';
import { FileUploadComponent } from '../dialog/file-upload/file-upload.component';
import { CourseworkRequest } from '../model/coursework-request';
import { CourseworkRequestService } from '../service/coursework-request.service';
import { CourseworkRequestDialogComponent } from '../dialog/coursework-request/coursework-request.dialog';
import { RejectCourseworkRequestDialogComponent } from '../dialog/reject-coursework-request/reject-coursework-request.dialog';
import { CloseCourseworkRequestDialogComponent } from '../dialog/close-coursework-request/close-coursework-request.dialog';

@Component({
  selector: 'app-coursework-requests',
  templateUrl: './coursework-requests.component.html',
  styleUrls: ['./coursework-requests.component.css']
})
export class CourseworkRequestsComponent implements OnInit {

  public displayedColumns: string[] = ['title', 'filename', 'creator', 'status', 'comment', 'actions'];
  public dataSource = new MatTableDataSource<CourseworkRequest>();

  public searchQuery = '';
  public pageIndex = 0;
  public pageSize = 10;
  public pageTotal: number;
  public sortDirection: string;
  public sortField: string;

  constructor(
    private $courseworkRequestService: CourseworkRequestService,
    private dialog: MatDialog
  ) {
  }

  public ngOnInit(): void {
    this.search();
  }

  public create(): void {
    this.dialog.open(CourseworkRequestDialogComponent, {
      data: {}
    }).afterClosed()
      .subscribe(result => {
        if (result) {
          this.search();
        }
      });
  }

  public edit(request: CourseworkRequest): void {
    this.dialog.open(CourseworkRequestDialogComponent, {
      data: { ...request }
    }).afterClosed()
      .subscribe(result => {
        if (result) {
          this.search();
        }
      });
  }

  public uploadFile(requestId: string): void {
    this.dialog.open(FileUploadComponent).afterClosed().subscribe(file => {
      if (file) {
        this.$courseworkRequestService.uploadFile(requestId, file).subscribe(this.search.bind(this));
      }
    });
  }

  public approve(requestId: string): void {
    this.$courseworkRequestService.approve(requestId).subscribe(this.search.bind(this));
  }

  public reject(requestId: string): void {
    this.dialog.open(RejectCourseworkRequestDialogComponent, {
      data: {
        id: requestId
      }
    }).afterClosed()
      .subscribe(result => {
        if (result) {
          this.search();
        }
      });
  }

  public close(requestId: string): void {
    this.dialog.open(CloseCourseworkRequestDialogComponent, {
      data: {
        id: requestId
      }
    }).afterClosed()
      .subscribe(result => {
        if (result) {
          this.search();
        }
      });
  }

  public search(): void {
    this.$courseworkRequestService
      .search(this.searchQuery, this.pageIndex, this.pageSize, this.sortDirection, this.sortField)
      .subscribe(result => {
        this.dataSource.data = result.content;
        this.pageTotal = result.totalElements;
      });
  }
}
