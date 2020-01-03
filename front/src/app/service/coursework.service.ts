import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Coursework } from '../model/coursework';

@Injectable({
  providedIn: 'root'
})
export class CourseworkService {

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
