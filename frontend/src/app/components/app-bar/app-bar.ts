import { Component, HostListener, OnInit } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../services/auth';

@Component({
  selector: 'app-app-bar',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './app-bar.html',
  styleUrls: ['./app-bar.css']
})
export class AppBar implements OnInit{

  isLoggedIn: boolean = false;
  userEmail: string = '';
  isAdmin: boolean = false;

  constructor(private authService: AuthService, private router: Router) {}

  ngOnInit() {
    this.authService.currentUserRole$.subscribe(role => {
      this.isAdmin = role === 'ROLE_ADMIN';
    });
    
    this.authService.currentUserEmail$.subscribe(email => {
      if (email) {
        this.isLoggedIn = true;
        this.userEmail = email.split('@')[0];
      } else {
        this.isLoggedIn = false;
        this.userEmail = '';
      }
    });
  }

  logout() {
    this.authService.logout();
    this.router.navigate(['/']);
  }
}
