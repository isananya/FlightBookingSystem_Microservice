import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { BehaviorSubject, tap } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private baseUrl = environment.apiGatewayUrl + '/auth';

  private userEmailSubject = new BehaviorSubject<string | null>(localStorage.getItem('userEmail'));
  public currentUserEmail$ = this.userEmailSubject.asObservable();

  constructor(private http: HttpClient) {}

  login(data: any) {
    return this.http.post(`${this.baseUrl}/login`, data, {
      responseType: 'text'
    }).pipe(
      tap(()=>{
        this.setUser(data.email);
      })
    );
  }

  signup(data: any) {
    return this.http.post(`${this.baseUrl}/signup`,data,{ 
      responseType: 'text' 
    }).pipe(
      tap(()=>{
        this.setUser(data.email);
      })
    );
  }

  logout() {
    localStorage.removeItem('userEmail');
    this.userEmailSubject.next(null);
  }

  private setUser(email: string) {
    localStorage.setItem('userEmail', email);
    this.userEmailSubject.next(email);
  }

  getEmail(): string {
    return localStorage.getItem('userEmail') || '';
  }

  isLoggedIn(): boolean {
    return !!localStorage.getItem('userEmail');
  }

}
