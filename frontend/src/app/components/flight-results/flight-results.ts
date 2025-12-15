import { Component, inject, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { FlightService } from '../../services/flight';
import { switchMap, map, finalize } from 'rxjs';
import { FlightList } from '../flight-list/flight-list';

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

  constructor(
    private route: ActivatedRoute,
    private flightService: FlightService,
    private cd: ChangeDetectorRef
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
        passengerCount: Number(params['passengers']), // Ensure this is a number
        roundTrip: params['roundTrip'] === 'true'
      };

      if (params['roundTrip'] === 'true') {
        apiParams.returnDate = params['returnDate'];
      }

      console.log('Sending API Params:', apiParams); // Debug log

      this.flightService.searchFlights(apiParams)
      .pipe(
          finalize(() => {
            this.loading = false;
            this.cd.detectChanges();
          })
        )
        .subscribe({
          next: res => {
            console.log('RESULTS:', res); // Debug log
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

  // ngOnInit() {
  //   this.route.queryParams.subscribe(params => {
  //     this.results = null;
  //     this.error = '';
  //     this.loading = true;

  //     this.searchParams = params;

  //     const apiParams: any = {
  //       sourceAirport: params['source'],
  //       destinationAirport: params['dest'],
  //       departureDate: params['departureDate'],
  //       passengerCount: params['passengers'],
  //       roundTrip: params['roundTrip'] === 'true'
  //     };

  //     if (params['roundTrip'] === 'true') {
  //       apiParams.returnDate = params['returnDate'];
  //     }


  //     this.flightService.searchFlights(apiParams)
  //     .pipe(
  //         finalize(() => {
  //           this.loading = false;
  //         })
  //       )
  //       .subscribe({
  //         next: res => {
  //           console.log('RESULTS:', res);
  //           this.results = res;
  //         },
  //         error: err => {
  //           console.error(err);
  //           this.error = 'Failed to load flights';
  //         }
  //       });;
  //   });
  // }


  // private route = inject(ActivatedRoute);
  // private flightService = inject(FlightService);

  // results$ = this.route.queryParams.pipe(
  //   map(params => ({
  //     apiParams: {
  //       sourceAirport: params['source'],
  //       destinationAirport: params['dest'],
  //       departureDate: params['departureDate'],
  //       passengerCount: params['passengers'],
  //       roundTrip: params['roundTrip'] === 'true',
  //       returnDate: params['roundTrip'] === 'true'
  //         ? params['returnDate']
  //         : undefined
  //     },
  //     params
  //   })),
  //   switchMap(({ apiParams, params }) =>
  //     this.flightService.searchFlights(apiParams).pipe(
  //       map(res => ({ res, params }))
  //     )
  //   )
  // );
}
