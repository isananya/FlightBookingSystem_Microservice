import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component } from '@angular/core';
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

  showModal: boolean = false;
  showErrorModal: boolean = false;
  
  pnrToCancel: string | null = null;

  constructor(
    private flightService: FlightService,
    private authService: AuthService,
    private cd: ChangeDetectorRef,
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
        this.cd.detectChanges();
      },
      error: (err) => {
        console.error('Error fetching bookings', err);
        this.isLoading = false;
      }
    });
  }

  openCancelModal(pnr: string) {
    this.pnrToCancel = pnr;
    this.showModal = true;
  }

  closeModal() {
    this.showModal = false;
    this.pnrToCancel = null;
  }

  onConfirmCancel() {
    if (!this.pnrToCancel) return;

    this.flightService.cancelBooking(this.pnrToCancel).subscribe({
      next: () => {
        this.closeModal();
        this.loadBookings();
      },
      error: (err) => {
        this.closeModal();
        this.showErrorModal = true;
        console.error('Cancellation failed', err);
      }
    });
  }

  closeErrorModal() {
    this.showErrorModal = false;
  }
}
