import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { FlightService } from '../../services/flight';

@Component({
  selector: 'app-book',
  imports: [CommonModule, FormsModule],
  templateUrl: './book.html',
  styleUrl: './book.css',
})
export class Book {
  request = {
    emailId: "ananya20@gmail.com",
    roundTrip: false,
    departureScheduleId: 1,
    passengerCount: 1,
    passengers: [{
      firstName: "ananya",
      lastName: "nayak",
      age: 21,
      departureSeatNumber: "11B",
      gender: "FEMALE",
      mealOption: "VEG"
    }]
  };

  error = '';

  constructor(private flightService: FlightService, private router: Router) { }

  ngOnInit(): void { }

  confirmBooking() {
    this.flightService.bookFlight(this.request).subscribe(
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
