import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { Usuario } from '../../models/usuario/usuario';
import { tap } from 'rxjs/operators';
import { Router } from '@angular/router';

interface User {
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
interface LoginResponse {
  token: string;
  user?: User;
}

@Injectable({
  providedIn: "root",
})
export class LoginServicio {
  private loginSuccessMessageSubject = new BehaviorSubject<string | null>(null);
  loginSuccessMessage$ = this.loginSuccessMessageSubject.asObservable();

  private isUserLoggedInSubject = new BehaviorSubject<boolean>(false);
  isUserLoggedIn$ = this.isUserLoggedInSubject.asObservable();

  private userLoggedSubject = new BehaviorSubject<User | null>(null);
  userLogged$ = this.userLoggedSubject.asObservable();


  constructor(private http: HttpClient, private router: Router) {

    const userString = localStorage.getItem('currentUser');

    if (userString) {
      try {
        // Parse JSON and update userLogged and isUserLoggedIn
        const user: Usuario = JSON.parse(userString);
        this.userLogged = user;
        this.isUserLoggedIn = true;
        this.isUserLoggedInSubject.next(true); // Notify subscribers
      } catch (error) {
        // Handle errors during JSON parsing
        console.error('Error parsing user data from localStorage:', error);
        localStorage.removeItem("currentUser"); // Clear invalid data
        this.isUserLoggedInSubject.next(false);
      }
    } else {
      // No user found in localStorage, set initial state
      this.isUserLoggedInSubject.next(false);
    }
  }

  loginSuccess(message: string) {
    this.loginSuccessMessageSubject.next(message);
  }
  logout() {
    console.log("se cerró sesion");
    this.isUserLoggedIn = false;
    this.userLogged = null;
    localStorage.removeItem('currentUser');
    localStorage.removeItem('token');
    // Actualizar el observable para notificar a los componentes suscritos
    this.isUserLoggedInSubject.next(false);
    this.router.navigate(['/iniciar-sesion']);
    
  }
  private baseUrl = 'http://localhost:8080/api/autenticacion/login';
  private isUserLoggedIn: boolean = false;
  public userLogged: Usuario | null = null;

  
  isLogged() {
    return this.isUserLoggedIn;
  }
  setUserLoggedIn(user: Usuario) {
    this.isUserLoggedIn = true;
    this.userLogged = user;
    localStorage.setItem('currentUser', JSON.stringify(user));

  }
  getUserLoggedIn() {
    const userString = localStorage.getItem('currentUser');
    if (userString) {
      return JSON.parse(userString);
    } else {
      // Manejar el caso en que el usuario no está logeado
      return null;
    }
  }

  login(user: any): Observable<any> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });

    return this.http.post<LoginResponse>(this.baseUrl, user, { headers })
      .pipe(
        tap(response => {
          if (response && response.user) { // Verifica si la respuesta contiene datos del usuario
            this.setUserLoggedIn(response.user);
            this.isUserLoggedInSubject.next(true);
          }
          console.log('Full login response:', response);
        })
      );
  }
}
