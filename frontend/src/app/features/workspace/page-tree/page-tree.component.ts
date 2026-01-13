import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatTooltipModule } from '@angular/material/tooltip';
import { Page } from '../../../shared/models';
import { PageService } from '../../../core/services/page.service';
import { PageTreeItemComponent } from './page-tree-item.component';

@Component({
  selector: 'app-page-tree',
  standalone: true,
  imports: [CommonModule, MatIconModule, MatButtonModule, MatTooltipModule, PageTreeItemComponent],
  templateUrl: './page-tree.component.html',
  styleUrls: ['./page-tree.component.scss']
})
export class PageTreeComponent implements OnInit {
  @Input() workspaceId!: string;
  @Input() selectedPageId: string | null = null;
  @Output() pageSelected = new EventEmitter<string>();
  @Output() newPageCreated = new EventEmitter<void>();

  pages: Page[] = [];
  expandedPages = new Set<string>();
  loading = false;

  constructor(private pageService: PageService) {}

  ngOnInit(): void {
    this.loadPages();
  }

  loadPages(): void {
    if (!this.workspaceId) return;
    
    this.loading = true;
    this.pageService.getPagesByWorkspace(this.workspaceId).subscribe({
      next: (pages) => {
        this.pages = pages.filter(p => !p.parentPage);
        this.loading = false;
      },
      error: (err) => {
        console.error('Error loading pages:', err);
        this.loading = false;
      }
    });
  }

  selectPage(pageId: string): void {
    this.selectedPageId = pageId;
    this.pageSelected.emit(pageId);
  }

  createNewPage(parentPageId?: string): void {
    const title = prompt('Enter page title:');
    if (!title) return;

    this.pageService.createPage(this.workspaceId, title, parentPageId).subscribe({
      next: () => {
        this.newPageCreated.emit();
        this.loadPages();
      },
      error: (err) => console.error('Error creating page:', err)
    });
  }

  toggleExpand(pageId: string): void {
    if (this.expandedPages.has(pageId)) {
      this.expandedPages.delete(pageId);
    } else {
      this.expandedPages.add(pageId);
    }
  }

  isExpanded(pageId: string): boolean {
    return this.expandedPages.has(pageId);
  }

  getChildPages(page: Page): Page[] {
    return page.childPages || [];
  }

  deletePage(pageId: string): void {
    if (confirm('Are you sure you want to delete this page?')) {
      this.pageService.deletePage(this.workspaceId, pageId).subscribe({
        next: () => this.loadPages(),
        error: (err) => console.error('Error deleting page:', err)
      });
    }
  }
}
