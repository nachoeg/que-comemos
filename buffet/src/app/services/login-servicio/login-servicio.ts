import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { Usuario } from '../../models/usuario/usuario';
import { tap } from 'rxjs/operators';

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

  //private userLoggedSubject = new BehaviorSubject<Usuario | null>(null); 
  //public userLogged$ = this.userLoggedSubject.asObservable(); 

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
    //this.userLoggedSubject.next(null); // <--- Crucial: Emitir null al cerrar sesión
  }
  private baseUrl = 'http://localhost:8080/api/autenticacion/login';
  private isUserLoggedIn: boolean = false;
  public userLogged: Usuario | null = null;

  constructor(private http: HttpClient) {

    const userString = localStorage.getItem('currentUser');

    if (userString) {
      try {
        // Parse JSON and update userLogged and isUserLoggedIn
        const user: Usuario = JSON.parse(userString);
        //this.userLoggedSubject.next(user); // <--- Crucial: Emitir el usuario al inicio
        this.userLogged = user;
        this.isUserLoggedIn = true;
        this.isUserLoggedInSubject.next(true); // Notify subscribers
      } catch (error) {
        // Handle errors during JSON parsing
        console.error('Error parsing user data from localStorage:', error);
        //this.isUserLoggedIn = false;
        this.isUserLoggedInSubject.next(false);
      }
    } else {
      // No user found in localStorage, set initial state
      //this.isUserLoggedIn = false;
      this.isUserLoggedInSubject.next(false);
    }
  }
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
//   getUserLoggedIn(): Usuario {
//     return this.userLoggedSubject.value; // Acceder al valor actual del BehaviorSubject
// }


  // login(user: any): Observable<any> {
  //     const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
  //     return this.http.post(this.baseUrl, user, { headers });
  // }
  login(user: any): Observable<any> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });

    return this.http.post<LoginResponse>(this.baseUrl, user, { headers })
      .pipe(
        tap(response => {
          if (response && response.user) { // Verifica si la respuesta contiene datos del usuario
            //this.userLoggedSubject.next(response.user);  // <--- Crucial: Emitir el usuario logueado
            this.setUserLoggedIn(response.user);
            this.isUserLoggedInSubject.next(true);
          }
          console.log('Full login response:', response);
        })
      );
  }


}
