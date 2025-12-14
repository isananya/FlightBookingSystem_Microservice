import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private baseUrl = environment.apiGatewayUrl + '/auth';

  constructor(private http: HttpClient) {}

  login(data: any) {
    return this.http.post(`${this.baseUrl}/login`, data, {
      responseType: 'text'
    });
  }
}
