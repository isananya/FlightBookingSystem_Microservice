import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../services/auth';

@Component({
  selector: 'app-login',
  imports: [CommonModule, FormsModule],
  templateUrl: './login.html',
  styleUrl: './login.css',
})

export class Login {
  email = '';
  password = '';
  message = '';

  constructor(private authService: AuthService) {}

  login() {
    this.authService.login({
      email: this.email,
      password: this.password
    }).subscribe({
      next: res => this.message = res,
      error: err => this.message = err.error
    });
  }
}
