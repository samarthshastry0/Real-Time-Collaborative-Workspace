import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { PageTreeComponent } from './page-tree/page-tree.component';
import { PageEditorComponent } from './page-editor/page-editor.component';

@Component({
  selector: 'app-workspace-layout',
  standalone: true,
  imports: [CommonModule, PageTreeComponent, PageEditorComponent],
  templateUrl: './workspace-layout.component.html',
  styleUrls: ['./workspace-layout.component.scss']
})
export class WorkspaceLayoutComponent implements OnInit {
  workspaceId: string = '';
  selectedPageId: string | null = null;

  constructor(
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.workspaceId = params['workspaceId'];
      this.route.queryParams.subscribe(queryParams => {
        this.selectedPageId = queryParams['pageId'] || null;
      });
    });
  }

  onPageSelected(pageId: string): void {
    this.selectedPageId = pageId;
    this.router.navigate([], {
      relativeTo: this.route,
      queryParams: { pageId },
      queryParamsHandling: 'merge'
    });
  }

  onNewPageCreated(): void {
    // Page tree will reload automatically
  }
}
