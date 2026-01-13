import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Page } from '../../shared/models';

@Injectable({
  providedIn: 'root'
})
export class PageService {
  private apiUrl = 'http://localhost:8080/api';

  constructor(private http: HttpClient) {}

  // Get all pages in a workspace
  getPagesByWorkspace(workspaceId: string): Observable<Page[]> {
    return this.http.get<Page[]>(`${this.apiUrl}/workspaces/${workspaceId}/pages`);
  }

  // Get a specific page by ID
  getPageById(workspaceId: string, pageId: string): Observable<Page> {
    return this.http.get<Page>(`${this.apiUrl}/workspaces/${workspaceId}/pages/${pageId}`);
  }

  // Get child pages of a parent page
  getChildPages(parentPageId: string): Observable<Page[]> {
    return this.http.get<Page[]>(`${this.apiUrl}/pages/${parentPageId}/children`);
  }

  // Create a new page
  createPage(workspaceId: string, title: string, parentPageId?: string): Observable<Page> {
    const payload = {
      title,
      parentPageId: parentPageId || null
    };
    return this.http.post<Page>(`${this.apiUrl}/workspaces/${workspaceId}/pages`, payload);
  }

  // Update page title
  updatePage(workspaceId: string, pageId: string, title: string): Observable<Page> {
    return this.http.put<Page>(`${this.apiUrl}/workspaces/${workspaceId}/pages/${pageId}`, { title });
  }

  // Delete a page
  deletePage(workspaceId: string, pageId: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/workspaces/${workspaceId}/pages/${pageId}`);
  }

  // Reorder a page
  reorderPage(workspaceId: string, pageId: string, order: number): Observable<Page> {
    return this.http.patch<Page>(`${this.apiUrl}/workspaces/${workspaceId}/pages/${pageId}/order`, { order });
  }
}
