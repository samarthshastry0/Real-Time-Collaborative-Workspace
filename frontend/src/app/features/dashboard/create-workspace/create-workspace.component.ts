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

  // TEMP: replace with a real UUID from your DB
  private readonly TEMP_OWNER_ID = 'bd51eb29-36b6-405e-865c-4fa8f6c0031f';

  constructor(
    private fb: FormBuilder,
    private workspaceService: WorkspaceService
  ) {
    this.workspaceForm = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(3)]],
      description: ['', [Validators.maxLength(500)]],
      ownerId: [this.TEMP_OWNER_ID, Validators.required]
    });
  }

  onSubmit(): void {
    if (this.workspaceForm.invalid) {
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
