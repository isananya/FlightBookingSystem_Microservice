import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../services/auth';
import { Router } from '@angular/router';

@Component({
  selector: 'app-change-password',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './change-password.html',
  styleUrl: './change-password.css',
})
export class ChangePassword {
  passwordObj = {
    currentPassword: '',
    newPassword: '',
    confirmPassword: ''
  };

  errorMessage: string = '';

  constructor(private authService: AuthService, private router: Router) {}

  onSubmit() {
    if (this.passwordObj.newPassword !== this.passwordObj.confirmPassword) {
      this.errorMessage = "New passwords do not match!";
      return;
    }

    if (this.passwordObj.newPassword.length < 6) {
      this.errorMessage = "New password must be at least 6 characters.";
      return;
    }

    const payload = {
      currentPassword: this.passwordObj.currentPassword,
      newPassword: this.passwordObj.newPassword
    };

    this.authService.changePassword(payload).subscribe({
      next: (res) => {
        alert('Password Changed Successfully! Please Login again.');
        this.authService.logout();
        this.router.navigate(['/login']);
      },
      error: (err) => {
        this.errorMessage = "Failed to change password. Check your current password.";
      }
    });
  }
}
