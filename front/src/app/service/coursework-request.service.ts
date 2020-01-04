import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CourseworkRequest } from '../model/coursework-request';

@Injectable({
  providedIn: 'root'
})
export class CourseworkRequestService {

  constructor(private httpClient: HttpClient) {
  }

  public save(request: CourseworkRequest): Observable<CourseworkRequest | void> {
    if (request.id) {
      return this.httpClient.put<void>('courseworkRequest', request);
    }

    return this.httpClient.post<CourseworkRequest>('courseworkRequest', request);
  }

  public uploadFile(requestId: string, file: File): Observable<void> {
    const formData: FormData = new FormData();
    formData.append('file', file, file.name);
    return this.httpClient.put<void>(`courseworkRequest/${requestId}/file`, formData);
  }

  public approve(id: string): Observable<CourseworkRequest> {
    return this.httpClient.put<CourseworkRequest>(`courseworkRequest/${id}/approve`, {});
  }

  public reject(id: string, comment: string): Observable<void> {
    return this.httpClient.put<void>(`courseworkRequest/${id}/reject`, comment);
  }

  public close(id: string, comment: string): Observable<void> {
    return this.httpClient.put<void>(`courseworkRequest/${id}/close`, comment);
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

    return this.httpClient.get('courseworkRequest/search', {
      params
    });
  }
}
