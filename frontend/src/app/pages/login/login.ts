
import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, RouterLink],
  templateUrl: './login.html',
  styleUrl: './login.css'
})
export class Login {

  private readonly authService = inject(AuthService);
  private readonly router = inject(Router);

  usernameOrEmail = '';
  password = '';

  loading = false;
  errorMessage = '';

  onLogin(): void {
    this.errorMessage = '';

    if (!this.usernameOrEmail.trim() || !this.password) {
      this.errorMessage = 'Please enter your username/email and password.';
      return;
    }

    this.loading = true;

    this.authService.login({
      usernameOrEmail: this.usernameOrEmail.trim(),
      password: this.password
    }).subscribe({
      next: () => {
        this.loading = false;
        this.router.navigate(['/']);
      },
      error: (error) => {
        this.loading = false;
        this.errorMessage =
          error.error?.message || 'Invalid credentials. Please try again.';
      }
    });
  }
}
