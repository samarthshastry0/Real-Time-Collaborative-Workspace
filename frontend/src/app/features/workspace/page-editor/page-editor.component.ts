import { Component, OnInit, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { Page, Block } from '../../../shared/models';
import { PageService } from '../../../core/services/page.service';
import { BlockService } from '../../../core/services/block.service';
import { BlockRendererComponent } from '../blocks/block-renderer.component';

@Component({
  selector: 'app-page-editor',
  standalone: true,
  imports: [CommonModule, FormsModule, MatIconModule, MatButtonModule, BlockRendererComponent],
  templateUrl: './page-editor.component.html',
  styleUrls: ['./page-editor.component.scss']
})
export class PageEditorComponent implements OnInit {
  @Input() workspaceId!: string;
  @Input() pageId!: string;

  page: Page | null = null;
  blocks: Block[] = [];
  loading = false;
  editingTitle = false;
  titleValue = '';

  blockTypes = [
    { label: 'Paragraph', value: 'paragraph' },
    { label: 'Heading 1', value: 'heading1' },
    { label: 'Heading 2', value: 'heading2' },
    { label: 'Heading 3', value: 'heading3' },
    { label: 'Bullet List', value: 'bulletList' },
    { label: 'Numbered List', value: 'numberedList' },
    { label: 'Code Block', value: 'codeBlock' },
    { label: 'Quote', value: 'quote' }
  ];

  constructor(
    private pageService: PageService,
    private blockService: BlockService
  ) {}

  ngOnInit(): void {
    this.loadPage();
  }

  ngOnChanges(): void {
    if (this.pageId) {
      this.loadPage();
    }
  }

  loadPage(): void {
    if (!this.workspaceId || !this.pageId) return;

    this.loading = true;
    this.pageService.getPageById(this.workspaceId, this.pageId).subscribe({
      next: (page) => {
        this.page = page;
        this.titleValue = page.title;
        this.loadBlocks();
      },
      error: (err) => {
        console.error('Error loading page:', err);
        this.loading = false;
      }
    });
  }

  loadBlocks(): void {
    if (!this.pageId) return;

    this.blockService.getBlocksByPage(this.pageId).subscribe({
      next: (blocks) => {
        this.blocks = blocks;
        this.loading = false;
      },
      error: (err) => {
        console.error('Error loading blocks:', err);
        this.loading = false;
      }
    });
  }

  startEditTitle(): void {
    this.editingTitle = true;
  }

  saveTitle(): void {
    if (this.titleValue.trim() && this.page) {
      this.pageService.updatePage(this.workspaceId, this.page.id, this.titleValue).subscribe({
        next: (updated) => {
          this.page = updated;
          this.editingTitle = false;
        },
        error: (err) => console.error('Error updating page title:', err)
      });
    }
  }

  cancelEditTitle(): void {
    this.titleValue = this.page?.title || '';
    this.editingTitle = false;
  }

  addBlock(type: string = 'paragraph'): void {
    if (!this.pageId) return;

    this.blockService.createBlock(this.pageId, type).subscribe({
      next: (newBlock) => {
        this.blocks.push(newBlock);
      },
      error: (err) => console.error('Error creating block:', err)
    });
  }

  deleteBlock(blockId: string): void {
    if (!this.pageId) return;

    this.blockService.deleteBlock(this.pageId, blockId).subscribe({
      next: () => {
        this.blocks = this.blocks.filter(b => b.id !== blockId);
      },
      error: (err) => console.error('Error deleting block:', err)
    });
  }

  updateBlockContent(blockId: string, content: string): void {
    if (!this.pageId) return;

    this.blockService.updateBlock(this.pageId, blockId, content).subscribe({
      error: (err) => console.error('Error updating block:', err)
    });
  }
}
