import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Workspace {
  id: string;
  name: string;
  description: string;
  owner: {
    id: string;
    email: string;
  };
  members: Array<{
    id: string;
    email: string;
  }>;
  createdAt: string;
  updatedAt: string;
}

export interface CreateWorkspaceRequest {
  name: string;
  description: string;
  ownerId: string;
}

@Injectable({
  providedIn: 'root'
})
export class WorkspaceService {
  private apiUrl = '/api/workspaces';

  constructor(private http: HttpClient) {}

  getAllWorkspaces(): Observable<Workspace[]> {
    return this.http.get<Workspace[]>(this.apiUrl);
  }

  getWorkspaceById(id: string): Observable<Workspace> {
    return this.http.get<Workspace>(`${this.apiUrl}/${id}`);
  }

  getWorkspacesByOwner(ownerId: string): Observable<Workspace[]> {
    return this.http.get<Workspace[]>(`${this.apiUrl}/owner/${ownerId}`);
  }

  getWorkspacesByMember(userId: string): Observable<Workspace[]> {
    return this.http.get<Workspace[]>(`${this.apiUrl}/member/${userId}`);
  }

  createWorkspace(data: CreateWorkspaceRequest): Observable<Workspace> {
    return this.http.post<Workspace>(this.apiUrl, data);
  }

  updateWorkspaceName(id: string, name: string): Observable<Workspace> {
    return this.http.patch<Workspace>(`${this.apiUrl}/${id}/name`, { name });
  }

  updateWorkspaceDescription(id: string, description: string): Observable<Workspace> {
    return this.http.patch<Workspace>(`${this.apiUrl}/${id}/description`, { description });
  }

  addMember(workspaceId: string, userId: string): Observable<Workspace> {
    return this.http.post<Workspace>(`${this.apiUrl}/${workspaceId}/members/${userId}`, {});
  }

  removeMember(workspaceId: string, userId: string): Observable<Workspace> {
    return this.http.delete<Workspace>(`${this.apiUrl}/${workspaceId}/members/${userId}`);
  }

  deleteWorkspace(id: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}