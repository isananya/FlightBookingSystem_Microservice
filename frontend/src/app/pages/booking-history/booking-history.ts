import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { BookingResponse } from '../../models/booking';
import { FlightService } from '../../services/flight';
import { AuthService } from '../../services/auth';

@Component({
  selector: 'app-booking-history',
  imports: [CommonModule],
  templateUrl: './booking-history.html',
  styleUrl: './booking-history.css',
})
export class BookingHistory {
  bookings: BookingResponse[] = [];
  isLoading: boolean = true;
  userEmail: string = '';

  constructor(
    private flightService: FlightService,
    private authService: AuthService
  ) { }

  ngOnInit() {
    this.userEmail = this.authService.getEmail();
    this.loadBookings();
  }

  loadBookings() {
    if (!this.userEmail) return;

    this.isLoading = true;
    this.flightService.getBookings(this.userEmail).subscribe({
      next: (data) => {
        this.bookings = data;
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Error fetching bookings', err);
        this.isLoading = false;
      }
    });
  }

  cancelBooking(pnr: String) {
    if (!confirm('Are you sure you want to cancel this booking?')) {
      return;
    }

    this.flightService.cancelBooking(pnr).subscribe({
      next: () => {
        alert('Booking Cancelled Successfully');
        this.loadBookings();
      },
      error: (err) => {
        console.error('Cancellation failed', err);
        alert('Could not cancel booking.');
      }
    });
  }
}
