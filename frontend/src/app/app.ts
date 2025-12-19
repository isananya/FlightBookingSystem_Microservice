import { Component, signal } from '@angular/core';
import { Router, RouterOutlet } from '@angular/router';
import { AppBar } from "./components/app-bar/app-bar";
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-root',
  imports: [CommonModule, RouterOutlet, AppBar],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  showNavbar = true;

  constructor(private router: Router) {
    this.router.events.subscribe(() => {
      const hiddenRoutes = ['/login', '/signup'];
      this.showNavbar = !hiddenRoutes.includes(this.router.url);
    });
  }

  protected readonly title = signal('frontend');
}
