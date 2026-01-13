import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Block } from '../../shared/models';

@Injectable({
  providedIn: 'root'
})
export class BlockService {
  private apiUrl = 'http://localhost:8080/api';

  constructor(private http: HttpClient) {}

  // Get all blocks in a page
  getBlocksByPage(pageId: string): Observable<Block[]> {
    return this.http.get<Block[]>(`${this.apiUrl}/pages/${pageId}/blocks`);
  }

  // Get a specific block
  getBlockById(pageId: string, blockId: string): Observable<Block> {
    return this.http.get<Block>(`${this.apiUrl}/pages/${pageId}/blocks/${blockId}`);
  }

  // Create a new block
  createBlock(pageId: string, type: string, content: string = ''): Observable<Block> {
    const payload = { type, content };
    return this.http.post<Block>(`${this.apiUrl}/pages/${pageId}/blocks`, payload);
  }

  // Update block content
  updateBlock(pageId: string, blockId: string, content: string, type?: string): Observable<Block> {
    const payload: any = { content };
    if (type) {
      payload.type = type;
    }
    return this.http.put<Block>(`${this.apiUrl}/pages/${pageId}/blocks/${blockId}`, payload);
  }

  // Update block order
  updateBlockOrder(pageId: string, blockId: string, order: number): Observable<Block> {
    return this.http.patch<Block>(`${this.apiUrl}/pages/${pageId}/blocks/${blockId}/order`, { order });
  }

  // Delete a block
  deleteBlock(pageId: string, blockId: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/pages/${pageId}/blocks/${blockId}`);
  }

  // Reorder multiple blocks
  reorderBlocks(pageId: string, blockIds: string[]): Observable<Block[]> {
    return this.http.post<Block[]>(`${this.apiUrl}/pages/${pageId}/blocks/reorder`, { blockIds });
  }
}
