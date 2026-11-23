
import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap } from 'rxjs';

export interface UserSession {
  userId: number;
  username: string;
  email: string;
  displayName: string;
}

export interface AuthResponse {
  token: string;
  tokenType: string;
  userId: number;
  username: string;
  email: string;
  displayName: string;
}

export interface LoginRequest {
  usernameOrEmail: string;
  password: string;
}

export interface RegisterRequest {
  username: string;
  email: string;
  password: string;
  displayName: string;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly http = inject(HttpClient);

  private readonly apiUrl = 'http://localhost:8080/api/auth';
  private readonly tokenKey = 'article_space_token';
  private readonly userKey = 'article_space_user';

  private readonly currentUserSubject =
    new BehaviorSubject<UserSession | null>(this.getCurrentUser());

  readonly currentUser$ = this.currentUserSubject.asObservable();

  login(request: LoginRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(
      `${this.apiUrl}/login`,
      request
    ).pipe(
      tap(response => this.saveSession(response))
    );
  }

  register(request: RegisterRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(
      `${this.apiUrl}/register`,
      request
    ).pipe(
      tap(response => this.saveSession(response))
    );
  }

  private saveSession(response: AuthResponse): void {
    if (typeof localStorage === 'undefined') {
      return;
    }

    localStorage.setItem(this.tokenKey, response.token);

    const user: UserSession = {
      userId: response.userId,
      username: response.username,
      email: response.email,
      displayName: response.displayName
    };

    localStorage.setItem(this.userKey, JSON.stringify(user));

    this.currentUserSubject.next(user);
  }

  getToken(): string | null {
    if (typeof localStorage === 'undefined') {
      return null;
    }

    return localStorage.getItem(this.tokenKey);
  }

  getCurrentUser(): UserSession | null {
    if (typeof localStorage === 'undefined') {
      return null;
    }

    const user = localStorage.getItem(this.userKey);

    if (!user) {
      return null;
    }

    try {
      return JSON.parse(user) as UserSession;
    } catch {
      return null;
    }
  }

  isLoggedIn(): boolean {
    return !!this.getToken();
  }

  logout(): void {
    if (typeof localStorage !== 'undefined') {
      localStorage.removeItem(this.tokenKey);
      localStorage.removeItem(this.userKey);
    }

    this.currentUserSubject.next(null);
  }
}
