import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Coursework } from '../model/coursework';

@Injectable({
  providedIn: 'root'
})
export class ApiService {

  constructor(private httpClient: HttpClient) {
  }

  public save(coursework: Coursework): Observable<Coursework | void> {
    if (coursework.id) {
      return this.httpClient.put<void>('coursework', coursework);
    }

    return this.httpClient.post<Coursework>('coursework', coursework);
  }

  public uploadFile(courseworkId: string, file: File): Observable<void> {
    const formData: FormData = new FormData();
    formData.append('file', file, file.name);
    return this.httpClient.put<void>(`coursework/${courseworkId}/file`, formData);
  }

  public downloadFile(courseworkId: string): void {
    this.httpClient.get(`coursework/${courseworkId}/file`, {
      responseType: 'blob',
      observe: 'response'
    }).subscribe(data => {
      const blob = new Blob([data.body], {
        type: data.headers.get('content-type')
      });
      const url = window.URL.createObjectURL(blob);
      window.open(url);
    });
  }

  public search(searchQuery: string, page: number, size: number, sortDirection?: string, sortField?: string): Observable<any> {
    const params: any = {
      searchQuery,
      page: `${page}`,
      size: `${size}`
    };

    if (sortDirection) {
      params.sortDirection = sortDirection;
    }

    if (sortField) {
      params.sortField = sortField;
    }

    return this.httpClient.get('coursework/search', {
      params
    });
  }

  public delete(courseworkId: string): Observable<void> {
    return this.httpClient.delete<void>('coursework', {
      params: {
        id: courseworkId
      }
    });
  }
}
