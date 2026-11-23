
import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [FormsModule, RouterLink],
  templateUrl: './register.html',
  styleUrl: './register.css'
})
export class Register {
  private readonly authService = inject(AuthService);
  private readonly router = inject(Router);

  username = '';
  email = '';
  displayName = '';
  password = '';

  loading = false;
  errorMessage = '';

  onRegister(): void {
    this.errorMessage = '';

    if (
      !this.username.trim() ||
      !this.email.trim() ||
      !this.displayName.trim() ||
      !this.password
    ) {
      this.errorMessage = 'Please fill in all required fields.';
      return;
    }

    if (this.username.trim().length < 3 ||
        this.username.trim().length > 30) {
      this.errorMessage = 'Username must be between 3 and 30 characters.';
      return;
    }

    if (this.password.length < 8) {
      this.errorMessage = 'Password must contain at least 8 characters.';
      return;
    }

    this.loading = true;

    this.authService.register({
      username: this.username.trim(),
      email: this.email.trim(),
      displayName: this.displayName.trim(),
      password: this.password
    }).subscribe({
      next: () => {
        this.loading = false;
        this.router.navigate(['/']);
      },
      error: (error) => {
        this.loading = false;
        this.errorMessage =
          error.error?.message ||
          'Registration failed. Please try again.';
      }
    });
  }
}
