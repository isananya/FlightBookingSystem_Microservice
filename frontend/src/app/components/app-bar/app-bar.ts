import { Component, HostListener } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-app-bar',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './app-bar.html',
  styleUrls: ['./app-bar.css']
})
export class AppBar {
  isOpen = false;

  toggle(event: MouseEvent) {
    console.log(this.isOpen)
    this.isOpen = !this.isOpen;
  }

  close() {
    this.isOpen = false;
  }

  logout() {
    this.close();
    this.router.navigate(['/login']);
  }

  constructor(private router: Router) {}

  @HostListener('document:click')
  onDocumentClick() {
    this.close();
  }
}
