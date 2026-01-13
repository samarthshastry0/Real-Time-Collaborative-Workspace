import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';
import { WorkspaceService, Workspace } from '../../core/services/workspace.service';
import { CreateWorkspaceComponent } from './create-workspace/create-workspace.component';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, RouterModule, CreateWorkspaceComponent],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss'
})
export class DashboardComponent implements OnInit {
  workspaces: Workspace[] = [];
  loading = true;
  error = '';
  showCreateModal = false;

  constructor(
    private authService: AuthService,
    private workspaceService: WorkspaceService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadWorkspaces();
  }

  loadWorkspaces(): void {
    this.loading = true;
    this.workspaceService.getAllWorkspaces().subscribe({
      next: (workspaces) => {
        this.workspaces = workspaces;
        this.loading = false;
      },
      error: (err) => {
        console.error('Error loading workspaces:', err);
        this.error = 'Failed to load workspaces';
        this.loading = false;
      }
    });
  }

  openCreateModal(): void {
    this.showCreateModal = true;
  }

  closeCreateModal(): void {
    this.showCreateModal = false;
  }

  onWorkspaceCreated(): void {
    this.closeCreateModal();
    this.loadWorkspaces();
  }

  viewWorkspace(id: string): void {
    this.router.navigate(['/workspace', id]);
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/auth/login']);
  }

  getMemberCount(workspace: Workspace): number {
    return workspace.members?.length || 0;
  }

  isOwner(workspace: Workspace): boolean {
    // We'll need to compare with current user
    return true; // Placeholder
  }
}