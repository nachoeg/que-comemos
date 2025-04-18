import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { Router } from '@angular/router';
import { environment } from '../../../environments/environment';

export interface User {
  id: number;
  dni: number;
  nombre: string;
  apellido: string;
  email: string;
  clave: string;
  foto: string;
  rol: number;
  rolName: string;
}

export interface LoginResponse {
  token: string;
  user?: User;
}

@Injectable({
  providedIn: 'root',
})
export class LoginServicio {
  private isUserLoggedInSubject = new BehaviorSubject<boolean>(false);
  isUserLoggedIn$ = this.isUserLoggedInSubject.asObservable();

  private userLoggedSubject = new BehaviorSubject<User | null>(null);
  userLogged$ = this.userLoggedSubject.asObservable();

  private baseUrl = environment.apiUrl + '/autenticacion/login';

  constructor(private http: HttpClient, private router: Router) {
    const userString = localStorage.getItem('currentUser');
    if (userString) {
      const user: User = JSON.parse(userString);
      this.userLoggedSubject.next(user);
      this.isUserLoggedInSubject.next(true);
    }
  }

  logout() {
    localStorage.clear();
    this.userLoggedSubject.next(null);
    this.isUserLoggedInSubject.next(false);
    this.router.navigate(['/iniciar-sesion']);
  }

  getUserLoggedIn(): User | null {
    return this.userLoggedSubject.value;
  }

  isUserLoggedIn(): boolean {
    return this.isUserLoggedInSubject.value;
  }

  getUserId(): number | null { // Or string | null if your IDs are strings
    const user = this.getUserLoggedIn();
    return user ? user.id : null; // Concise way to return id or null
}



  login(user: any): Observable<LoginResponse> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });

    return this.http.post<LoginResponse>(this.baseUrl, user, { headers }).pipe(
      tap((response) => {
        if (response && response.user) {
          localStorage.setItem('currentUser', JSON.stringify(response.user));
          localStorage.setItem('token', response.token);
          this.userLoggedSubject.next(response.user);
          this.isUserLoggedInSubject.next(true);
          console.log('Login successful! User info:', response.user);
          console.log('User in localStorage:', JSON.parse(localStorage.getItem('currentUser') || 'null'));
        }
      })
    );
  }

  // Nuevo método para recargar la información del usuario desde localStorage
  reloadUserInfoFromLocalStorage() {
    this.loadUserFromLocalStorage();
  }

  private loadUserFromLocalStorage() {
    const userString = localStorage.getItem('currentUser');
    if (userString) {
      try {
        const user: User = JSON.parse(userString);
        this.userLoggedSubject.next(user);
        this.isUserLoggedInSubject.next(true);
        console.log('User loaded from localStorage:', user);
      } catch (error) {
        console.error('Error parsing user from localStorage:', error);
        this.userLoggedSubject.next(null);
        this.isUserLoggedInSubject.next(false);
        localStorage.removeItem('currentUser'); // Limpia si hay un error al parsear
      }
    } else {
      console.log('No user found in localStorage.');
      this.userLoggedSubject.next(null);
      this.isUserLoggedInSubject.next(false);
    }
  }
}
