import { Component, EventEmitter, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { WorkspaceService } from '../../../core/services/workspace.service';

@Component({
  selector: 'app-create-workspace',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './create-workspace.component.html',
  styleUrl: './create-workspace.component.scss'
})
export class CreateWorkspaceComponent {
  @Output() workspaceCreated = new EventEmitter<void>();
  @Output() cancelled = new EventEmitter<void>();

  workspaceForm: FormGroup;
  loading = false;
  error = '';

  constructor(
    private fb: FormBuilder,
    private workspaceService: WorkspaceService
  ) {
    this.workspaceForm = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(3)]],
      description: ['', [Validators.maxLength(500)]],
      ownerId: ['user-id-placeholder'] // We'll need to get this from auth
    });
  }

  onSubmit(): void {
    if (this.workspaceForm.invalid) {
      return;
    }

    this.loading = true;
    this.error = '';

    // TODO: Get actual user ID from auth service
    this.workspaceForm.patchValue({
      ownerId: 'temp-user-id'
    });

    this.workspaceService.createWorkspace(this.workspaceForm.value).subscribe({
      next: () => {
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