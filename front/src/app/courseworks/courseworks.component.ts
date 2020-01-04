import { Component, OnInit } from '@angular/core';
import { CourseworkService } from '../service/coursework.service';
import { MatDialog, MatTableDataSource } from '@angular/material';
import { CourseworkDialogComponent } from '../dialog/coursework/coursework.dialog';
import { Coursework } from '../model/coursework';
import { FileUploadComponent } from '../dialog/file-upload/file-upload.component';

@Component({
  selector: 'app-courseworks',
  templateUrl: './courseworks.component.html',
  styleUrls: ['./courseworks.component.css']
})
export class CourseworksComponent implements OnInit {

  public displayedColumns: string[] = ['title', 'filename', 'username', 'actions'];
  public dataSource = new MatTableDataSource<Coursework>();

  public searchQuery = '';
  public pageIndex = 0;
  public pageSize = 10;
  public pageTotal: number;
  public sortDirection: string;
  public sortField: string;

  constructor(
    private $courseworkService: CourseworkService,
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
      data: { ...coursework }
    }).afterClosed()
      .subscribe(result => {
        if (result) {
          this.search();
        }
      });
  }

  public uploadFile(courseworkId: string): void {
    this.dialog.open(FileUploadComponent).afterClosed().subscribe(file => {
      if (file) {
        this.$courseworkService.uploadFile(courseworkId, file).subscribe(this.search.bind(this));
      }
    });
  }

  public delete(courseworkId: string): void {
    this.$courseworkService.delete(courseworkId).subscribe(this.search.bind(this));
  }

  public search(): void {
    this.$courseworkService
      .search(this.searchQuery, this.pageIndex, this.pageSize, this.sortDirection, this.sortField)
      .subscribe(result => {
        this.dataSource.data = result.content;
        this.pageTotal = result.totalElements;
      });
  }
}
