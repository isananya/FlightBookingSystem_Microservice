import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { Observable } from 'rxjs';

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

  bookFlight(params: any){
    return this.http.post(
      environment.apiGatewayUrl + '/booking',
      params
    );
  }
}
