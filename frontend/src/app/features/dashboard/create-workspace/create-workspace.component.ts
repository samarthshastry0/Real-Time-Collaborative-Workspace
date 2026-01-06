import { Component, EventEmitter, Output, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { WorkspaceService } from '../../../core/services/workspace.service';
import { AuthService } from '../../../core/services/auth.service';
import { take } from 'rxjs';

@Component({
  selector: 'app-create-workspace',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './create-workspace.component.html',
  styleUrl: './create-workspace.component.scss'
})
export class CreateWorkspaceComponent implements OnInit {
  @Output() workspaceCreated = new EventEmitter<void>();
  @Output() cancelled = new EventEmitter<void>();

  workspaceForm: FormGroup;
  loading = false;
  error = '';

  constructor(
    private fb: FormBuilder,
    private workspaceService: WorkspaceService,
    private authService: AuthService
  ) {
    this.workspaceForm = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(3)]],
      description: ['', [Validators.maxLength(500)]],
      ownerId: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    const existingUser = this.authService.getCurrentUserSync();
    if (existingUser?.id) {
      this.workspaceForm.patchValue({ ownerId: existingUser.id });
    } else {
      this.authService.currentUser$.pipe(take(1)).subscribe(user => {
        if (user?.id) {
          this.workspaceForm.patchValue({ ownerId: user.id });
        }
      });
    }
  }

  onSubmit(): void {
    if (this.workspaceForm.invalid) {
      return;
    }

    if (!this.workspaceForm.value.ownerId) {
      this.error = 'You must be logged in to create a workspace.';
      return;
    }

    this.loading = true;
    this.error = '';

    console.log('Creating workspace with payload:', this.workspaceForm.value);

    this.workspaceService.createWorkspace(this.workspaceForm.value).subscribe({
      next: () => {
        this.loading = false;
        this.workspaceCreated.emit();
      },
      error: (err) => {
        console.error('Error creating workspace:', err);
        this.error = 'Failed to create workspace';
        this.loading = false;
      }
    });
  }

  cancel(): void {
    this.cancelled.emit();
  }
}
