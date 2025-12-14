import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { FlightService } from '../../services/flight';
import { Router } from '@angular/router';

@Component({
  selector: 'app-flight-search',
  imports: [CommonModule, FormsModule],
  templateUrl: './flight-search.html',
  styleUrl: './flight-search.css',
})
export class FlightSearch {
  sourceAirport = '';
  destinationAirport = '';
  departureDate = '';
  returnDate = '';
  passengerCount = 1;
  tripType: 'ONE_WAY' | 'ROUND_TRIP' = 'ONE_WAY';

  result: any;
  error = '';

  constructor(private flightService: FlightService, private router: Router) {}

  search() {
    if (!this.sourceAirport || !this.destinationAirport || !this.departureDate) {
      this.error = 'Fill all required fields';
      return;
    }

    if (this.tripType === 'ROUND_TRIP' && !this.returnDate) {
      this.error = 'Return date required for round trip';
      return;
    }

    const queryParams: any = {
      sourceAirport: this.sourceAirport,
      destinationAirport: this.destinationAirport,
      departureDate: this.departureDate,
      passengerCount: this.passengerCount,
      roundTrip: this.tripType === 'ROUND_TRIP'
    };

    if (this.tripType === 'ROUND_TRIP') {
      queryParams.returnDate = this.returnDate;
    }

    this.error = '';

    this.router.navigate(['/results'], { queryParams });

  }
}
