import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../services/auth';

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
  message = '';

  constructor(private authService: AuthService) {}

  signup() {
    this.authService.signup({
      name: this.name,
      email: this.email,
      password: this.password,
      role: this.role
    }).subscribe({
      next: ()=>{
        this.authService.login({
          email: this.email,
          password: this.password
        }).subscribe({
          next: res => this.message = 'Signup + Login successful',
          error: err => this.message = err.error
        });
      },
      error: err => this.message = err.error
    });
  }
}
