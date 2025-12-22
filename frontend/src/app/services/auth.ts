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

  private userRoleSubject = new BehaviorSubject<string | null>(localStorage.getItem('userRole'));
  public currentUserRole$ = this.userRoleSubject.asObservable();

  private userNameSubject = new BehaviorSubject<string | null>(localStorage.getItem('userName'));
  public currentUserName$ = this.userNameSubject.asObservable();

  constructor(private http: HttpClient) { }

  login(data: any) {
    return this.http.post<any>(`${this.baseUrl}/login`, data).pipe(
      tap((response) => {
        if (response && response.email) {
          this.setUser(response.email, response.role, response.name);
        }
      })
    );
  }

  signup(data: any) {
    return this.http.post(`${this.baseUrl}/signup`, data, {
      responseType: 'text'
    });
  }

  logout() {
    localStorage.removeItem('userEmail');
    localStorage.removeItem('userRole');
    localStorage.removeItem('userName');

    this.userEmailSubject.next(null);
    this.userRoleSubject.next(null);
    this.userNameSubject.next(null);

    this.http.post(`${this.baseUrl}/logout`, {}).subscribe();
  }

  changePassword(data: any) {
    return this.http.put(
      `${this.baseUrl}/password`, data);
  }

  private setUser(email: string, role: string, name: string) {
    localStorage.setItem('userEmail', email);
    localStorage.setItem('userRole', role);
    localStorage.setItem('userName', name || '');

    this.userEmailSubject.next(email);
    this.userRoleSubject.next(role);
    this.userNameSubject.next(name || '');
  }

  getEmail(): string {
    return localStorage.getItem('userEmail') || '';
  }

  getRole(): string {
    return localStorage.getItem('userRole') || '';
  }

  getName(): string {
    return localStorage.getItem('userName') || '';
  }

  isLoggedIn(): boolean {
    return !!localStorage.getItem('userEmail');
  }

  isAdmin(): boolean {
    return this.getRole() === 'ROLE_ADMIN';
  }
}