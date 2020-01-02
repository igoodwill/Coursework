import { Component, OnInit } from '@angular/core';
import { ApiService } from '../service/api.service';
import { MatDialog, MatTableDataSource } from '@angular/material';
import { CourseworkDialogComponent } from '../dialog/coursework/coursework.dialog';
import { Coursework } from '../model/coursework';
import { FileUploadComponent } from '../dialog/file-upload/file-upload.component';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  public displayedColumns: string[] = ['title', 'filename', 'actions'];
  public dataSource = new MatTableDataSource<Coursework>();

  public searchQuery = '';
  public pageIndex = 0;
  public pageSize = 10;
  public pageTotal: number;
  public sortDirection: string;
  public sortField: string;

  constructor(
    private $apiService: ApiService,
    private dialog: MatDialog
  ) {
  }

  public ngOnInit(): void {
    this.search();
  }

  public create(): void {
    this.dialog.open(CourseworkDialogComponent, {
      data: {}
    }).afterClosed()
      .subscribe(result => {
        if (result) {
          this.search();
        }
      });
  }

  public edit(coursework: Coursework): void {
    this.dialog.open(CourseworkDialogComponent, {
      data: coursework
    }).afterClosed()
      .subscribe(result => {
        if (result) {
          this.search();
        }
      });
  }

  public uploadFile(courseworkId: string): void {
    this.dialog.open(FileUploadComponent, {
      data: {
        courseworkId
      }
    }).afterClosed()
      .subscribe(result => {
        if (result) {
          this.search();
        }
      });
  }

  public delete(courseworkId: string): void {
    this.$apiService.delete(courseworkId).subscribe(this.search.bind(this));
  }

  public search(): void {
    this.$apiService.search(this.searchQuery, this.pageIndex, this.pageSize, this.sortDirection, this.sortField).subscribe(result => {
      this.dataSource.data = result.content;
      this.pageTotal = result.totalElements;
    });
  }
}
