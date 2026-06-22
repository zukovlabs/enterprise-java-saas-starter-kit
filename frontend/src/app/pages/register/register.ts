import { Component, inject, ChangeDetectionStrategy } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { AuthService } from '../../services/auth.service';
import { MatSnackBarModule, MatSnackBar } from '@angular/material/snack-bar';
import { Router, RouterLink } from '@angular/router';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    FormsModule,
    MatSnackBarModule,
    RouterLink
  ],
  templateUrl: './register.html',
  changeDetection: ChangeDetectionStrategy.Eager,
  styleUrl: './register.scss'
})
export class Register {
  private authService = inject(AuthService);
  private snackBar = inject(MatSnackBar);
  private router = inject(Router);

  email = '';
  password = '';
  confirmPassword = '';

  onRegister() {
    if (!this.email || !this.password || !this.confirmPassword) {
      this.showError('Please fill in all fields.');
      return;
    }

    if (this.password !== this.confirmPassword) {
      this.showError('Passwords do not match!');
      return;
    }
    const registerData = { email: this.email, password: this.password };

    this.authService.register(registerData).subscribe({
      next: () => {
        this.snackBar.open('Registration successful! Check your email.', 'Close', {
          duration: 3000,
          horizontalPosition: 'center',
          verticalPosition: 'bottom',
          panelClass: ['success-snackbar']
        });
        this.router.navigate(['/login']);
      },
      error: (err) => {
        console.error(err);
        console.error(err);
        const errorMsg = err.error?.error || 'Registration failed. Please try again.';
        this.showError(errorMsg);
      }
    });
  }

  private showError(message: string) {
    this.snackBar.open(message, 'Close', {
      duration: 3000,
      horizontalPosition: 'center',
      verticalPosition: 'bottom',
      panelClass: ['error-snackbar']
    });
  }
}
