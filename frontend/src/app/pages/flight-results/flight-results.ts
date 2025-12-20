import { Component, inject, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { FlightService } from '../../services/flight';
import { finalize } from 'rxjs';
import { FlightList } from '../../components/flight-list/flight-list';

@Component({
  selector: 'app-flight-results',
  imports: [CommonModule, FlightList],
  templateUrl: './flight-results.html',
  styleUrl: './flight-results.css',
})
export class FlightResults implements OnInit{
  searchParams: any;
  results: any;
  loading = true;
  error = '';

  selectedDepartureId: number | null = null;
  selectedReturnId: number | null = null;
  selectedDepFlight: any = null;
  selectedRetFlight: any = null;

  onDepartureSelect(id: number | null) {
    this.selectedDepartureId = id;

    if (id !== null && this.results?.departure) {
      this.selectedDepFlight = this.results.departure.find((f: any) => f.id === id);
    } else {
      this.selectedDepFlight = null;
    }
    
    console.log("Selected Dep Flight:", this.selectedDepFlight);
  }

  onReturnSelect(id: number | null) {
    this.selectedReturnId = id;

    if (id !== null && this.results?.return) {
      this.selectedRetFlight = this.results.return.find((f: any) => f.id === id);
    } else {
      this.selectedRetFlight = null;
    }
  }
  
  constructor(
    private route: ActivatedRoute,
    private flightService: FlightService,
    private cd: ChangeDetectorRef,
    private router: Router
  ) { }

  ngOnInit() {
    this.route.queryParams.subscribe(params => {

      this.results = null;
      this.error = '';
      this.loading = true;
      this.searchParams = params;

      const apiParams: any = {
        sourceAirport: params['source'],
        destinationAirport: params['dest'],
        departureDate: params['departureDate'],
        passengerCount: Number(params['passengers']),
        roundTrip: params['roundTrip'] === 'true'
      };

      if (params['roundTrip'] === 'true') {
        apiParams.returnDate = params['returnDate'];
      }

      console.log('Sending API Params:', apiParams);

      this.flightService.searchFlights(apiParams)
      .pipe(
          finalize(() => {
            this.loading = false;
            this.cd.detectChanges();
          })
        )
        .subscribe({
          next: res => {
            console.log('RESULTS:', res);
            this.results = res;
            this.cd.detectChanges();
          },
          error: err => {
            console.error(err);
            this.error = 'Failed to load flights.';
            this.cd.detectChanges();
          }
        });
    });
  }

  calculateTotal(): number {
    let total = 0;
    if (this.selectedDepFlight) {
      total += this.selectedDepFlight.basePrice;
    }
    if (this.selectedRetFlight) {
      total += this.selectedRetFlight.basePrice;
    }
    return total * this.searchParams['passengers'];
  }

  book() {
    this.router.navigate(["/book"],{
      queryParams: {
        depId: this.selectedDepartureId,
        retId: this.selectedRetFlight ? this.selectedReturnId : null,
        count: this.searchParams["passengers"],
        round: this.searchParams["roundTrip"]
      },
      state: {
        departureFlight: this.selectedDepFlight,
        returnFlight: this.selectedRetFlight
      }
    });
     console.log("Booking initiated");
  }
}