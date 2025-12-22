import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { Observable } from 'rxjs';
import { BookingRequest, BookingResponse } from '../models/booking';
import { FlightModel, ScheduleRequestDTO } from '../models/inventory';

@Injectable({
  providedIn: 'root',
})
export class FlightService {

  constructor(private http: HttpClient) {}

  searchFlights(params: any) {
    return this.http.get(
      environment.apiGatewayUrl + '/flights/search',
      { params }
    );
  }

  bookFlight(params: BookingRequest){
    return this.http.post(
      environment.apiGatewayUrl + '/booking',
      params,
      { responseType: 'text' }
    );
  }

  getBookings(email: string): Observable<BookingResponse[]> {
    return this.http.get<BookingResponse[]>(
      environment.apiGatewayUrl+"/booking/history/"+email
    );
  }

  cancelBooking(pnr: String): Observable<any> {
    return this.http.delete(
      environment.apiGatewayUrl+"/booking/cancel/"+pnr, 
      { responseType: 'text' }
    );
  }

  addFlight(flight: FlightModel): Observable<string> {
    return this.http.post(
      environment.apiGatewayUrl+"/schedule/route", 
      flight, 
      { responseType: 'text' }
    );
  }

  addSchedule(schedule: ScheduleRequestDTO): Observable<string> {
    return this.http.post(
      environment.apiGatewayUrl+"/schedule/inventory",
      schedule, 
      { responseType: 'text' }
    );
  }
}
