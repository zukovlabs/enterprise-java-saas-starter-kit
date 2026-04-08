import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatDividerModule } from '@angular/material/divider';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';

import { UserService } from '../../services/user.service';

@Component({
  selector: 'app-settings',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    MatDividerModule,
    ReactiveFormsModule,
    MatSnackBarModule
  ],
  templateUrl: './settings.html',
  styleUrl: './settings.scss'
})
export class Settings implements OnInit {

  private fb = inject(FormBuilder);
  private userService = inject(UserService);
  private snackBar = inject(MatSnackBar);

  profileForm: FormGroup;
  passwordForm: FormGroup;

  constructor() {
    this.profileForm = this.fb.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      email: [{value: '', disabled: true}]
    });

    this.passwordForm = this.fb.group({
      currentPassword: ['', Validators.required],
      newPassword: ['', [Validators.required, Validators.minLength(6)]],
      confirmPassword: ['', Validators.required]
    });
  }

  ngOnInit() {
    this.loadProfile();
  }

  loadProfile() {
    this.userService.getProfile().subscribe({
      next: (user) => {
        this.profileForm.patchValue({
          firstName: user.firstName,
          lastName: user.lastName,
          email: user.email
        });
      },
      error: (err) => {
        console.error(err);
        this.showError('Failed to load profile data');
      }
    });
  }

  saveProfile() {
    if (this.profileForm.valid) {
      const request = {
        firstName: this.profileForm.value.firstName,
        lastName: this.profileForm.value.lastName
      };

      this.userService.updateProfile(request).subscribe({
        next: (updatedUser) => {
          this.showSuccess('Profile updated successfully');
          this.profileForm.patchValue({
            firstName: updatedUser.firstName,
            lastName: updatedUser.lastName
          });
        },
        error: (err) => {
          console.error(err);
          this.showError('Failed to update profile');
        }
      });
    }
  }

  changePassword() {
    if (this.passwordForm.valid) {
      const vals = this.passwordForm.value;

      if (vals.newPassword !== vals.confirmPassword) {
        this.showError('Passwords do not match');
        return;
      }

      const request = {
        currentPassword: this.passwordForm.value.currentPassword,
        newPassword: this.passwordForm.value.newPassword
      };

      this.userService.changePassword(request).subscribe({
        next: () => {
          this.showSuccess('Password changed successfully');
          this.passwordForm.reset();
          Object.keys(this.passwordForm.controls).forEach(key => {
            this.passwordForm.get(key)?.setErrors(null);
          });
        },
        error: (err) => {
          console.error(err);
          this.showError('Failed to change password. Check your current password.');
        }
      });
    }
  }

  private showSuccess(message: string) {
    this.snackBar.open(message, 'OK', {
      duration: 3000,
      horizontalPosition: 'right',
      verticalPosition: 'bottom',
      panelClass: ['success-snackbar']
    });
  }

  private showError(message: string) {
    this.snackBar.open(message, 'Close', {
      duration: 4000,
      horizontalPosition: 'center',
      verticalPosition: 'bottom',
      panelClass: ['error-snackbar']
    });
  }
}
