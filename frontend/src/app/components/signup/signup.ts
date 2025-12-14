import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../services/auth';
import { Router } from '@angular/router';

@Component({
  selector: 'app-signup',
  imports: [CommonModule, FormsModule],
  templateUrl: './signup.html',
  styleUrl: './signup.css',
})
export class Signup {
  name = '';
  email = '';
  password = '';
  role = 'ROLE_USER';
  error = '';

  constructor(private authService: AuthService, private router: Router) { }

  signup() {
    this.authService.signup({
      name: this.name,
      email: this.email,
      password: this.password,
      role: this.role
    }).subscribe({
      next: () => {
        this.authService.login({
          email: this.email,
          password: this.password
        }).subscribe({
          next: () => {
            this.router.navigate(['/']);
          },
          error: err => {
            this.error = err.error || 'Login failed';
          }
        });
      },
      error: err => {
        this.error = err.error || 'Signup failed';
      }
    });
  }
}
