import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, throwError, tap, map, catchError } from 'rxjs';
import { Router } from '@angular/router';

interface AuthResponse {
  token: string;
  refreshToken: string;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private http = inject(HttpClient);
  private router = inject(Router);
  private apiUrl = '/api/auth';

  register(credentials: { email: string; password: string }): Observable<any> {
    return this.http.post(`${this.apiUrl}/register`, credentials);
  }

  login(credentials: { email: string; password: string }): Observable<any> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/login`, credentials).pipe(
      tap((response) => {
        localStorage.setItem('token', response.token);
        localStorage.setItem('refreshToken', response.refreshToken);
        this.router.navigate(['/dashboard']);
      })
    );
  }

  refreshTokens(): Observable<string> {
    const refreshToken = localStorage.getItem('refreshToken');
    if (!refreshToken) {
      this.logout();
      return throwError(() => new Error('No refresh token'));
    }
    return this.http.post<AuthResponse>(`${this.apiUrl}/refresh`, { refreshToken }).pipe(
      tap((response) => {
        localStorage.setItem('token', response.token);
        localStorage.setItem('refreshToken', response.refreshToken);
      }),
      map((response) => response.token),
      catchError((err) => {
        this.logout();
        return throwError(() => err);
      })
    );
  }

  logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('refreshToken');
    this.router.navigate(['/login']);
  }
}