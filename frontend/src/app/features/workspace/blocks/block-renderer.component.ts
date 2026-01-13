import { Component, Input, Output, EventEmitter, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatTooltipModule } from '@angular/material/tooltip';
import { Block } from '../../../shared/models';

@Component({
  selector: 'app-block-renderer',
  standalone: true,
  imports: [CommonModule, FormsModule, MatIconModule, MatButtonModule, MatTooltipModule],
  templateUrl: './block-renderer.component.html',
  styleUrls: ['./block-renderer.component.scss']
})
export class BlockRendererComponent implements OnInit {
  @Input() block!: Block;
  @Input() pageId!: string;
  @Output() contentUpdated = new EventEmitter<string>();
  @Output() blockDeleted = new EventEmitter<string>();

  editing = false;
  contentValue = '';

  ngOnInit(): void {
    this.contentValue = this.block.content;
  }

  startEdit(): void {
    this.editing = true;
  }

  saveContent(): void {
    if (this.contentValue !== this.block.content) {
      this.block.content = this.contentValue;
      this.contentUpdated.emit(this.contentValue);
    }
    this.editing = false;
  }

  cancelEdit(): void {
    this.contentValue = this.block.content;
    this.editing = false;
  }

  delete(): void {
    if (confirm('Delete this block?')) {
      this.blockDeleted.emit(this.block.id);
    }
  }

  getBlockClass(): string {
    return `block-${this.block.type}`;
  }

  getBlockTag(): string {
    switch (this.block.type) {
      case 'heading1':
        return 'h1';
      case 'heading2':
        return 'h2';
      case 'heading3':
        return 'h3';
      case 'quote':
        return 'blockquote';
      default:
        return 'p';
    }
  }
}
