import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatTooltipModule } from '@angular/material/tooltip';
import { Page } from '../../../shared/models';

@Component({
  selector: 'app-page-tree-item',
  standalone: true,
  imports: [CommonModule, MatIconModule, MatButtonModule, MatTooltipModule],
  templateUrl: './page-tree-item.component.html',
  styleUrls: ['./page-tree-item.component.scss']
})
export class PageTreeItemComponent {
  @Input() page!: Page;
  @Input() selectedPageId: string | null = null;
  @Input() isExpanded = false;
  @Output() pageSelected = new EventEmitter<string>();
  @Output() pageDeleted = new EventEmitter<string>();
  @Output() expandToggled = new EventEmitter<string>();
  @Output() childPageCreated = new EventEmitter<string>();

  onSelect(): void {
    this.pageSelected.emit(this.page.id);
  }

  onToggleExpand(): void {
    this.expandToggled.emit(this.page.id);
  }

  onDelete(event: Event): void {
    event.stopPropagation();
    this.pageDeleted.emit(this.page.id);
  }

  onCreateChild(event: Event): void {
    event.stopPropagation();
    this.childPageCreated.emit(this.page.id);
  }

  hasChildren(): boolean {
    return !!(this.page.childPages && this.page.childPages.length > 0);
  }

  isSelected(): boolean {
    return this.selectedPageId === this.page.id;
  }
}
