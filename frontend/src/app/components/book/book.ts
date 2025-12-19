import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { FlightService } from '../../services/flight';
import { BookingRequest, Passenger } from '../../models/booking';

@Component({
  selector: 'app-book',
  imports: [CommonModule, FormsModule],
  templateUrl: './book.html',
  styleUrl: './book.css',
})
export class Book implements OnInit {
  passengerCount: number = 1;
  isRoundTrip: boolean = false;
  depId: number = 0;
  retId: number | null = null;
  email: string = '';
  passengers: Passenger[] = [];
  error = '';

  constructor(private flightService: FlightService, private router: Router, private route: ActivatedRoute) { }

  ngOnInit() {
    this.route.queryParams.subscribe(params => {
      this.passengerCount = +params['count'] || 1;
      this.isRoundTrip = params['round'] === 'true';
      this.depId = +params['depId'];
      this.retId = params['retId'] ? +params['retId'] : null;

      this.passengers = Array.from({ length: this.passengerCount }, () => ({
        firstName: '',
        lastName: '',
        age: null,
        gender: '',
        mealOption: '',
        departureSeatNumber: '',
        returnSeatNumber: ''
      }));
    });
  }

  confirmBooking() {
    const request: BookingRequest = {
      emailId: this.email,
      roundTrip: this.isRoundTrip,
      departureScheduleId: this.depId,
      passengerCount: this.passengerCount,
      passengers: this.passengers
    };

    if (this.isRoundTrip && this.retId) {
      request.returnScheduleId = this.retId;
    }

    if (!this.isRoundTrip) {
      request.passengers.forEach(p => delete p.returnSeatNumber);
    }
    this.flightService.bookFlight(request).subscribe(
      {
        next: (response) => {
          console.log(response);
          alert("booking confirmed")
        },
        error: () => {
          this.error = 'Booking not successful'
        }
      }
    )
  }
}
