import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { Usuario } from '../../models/usuario/usuario';
@Injectable({
    providedIn: "root",
  })
export class UsuariosServicio {
    private baseUrl = 'http://localhost:8080/api/usuarios';
    private registerSuccessMessageSubject = new BehaviorSubject<string | null>(null);
  registerSuccessMessage$ = this.registerSuccessMessageSubject.asObservable();
    constructor(private http: HttpClient) {}

    registerSuccess(message: string) {
      this.registerSuccessMessageSubject.next(message);
    }

    registerUser(user: Usuario): Observable<any>{
      return this.http.post(this.baseUrl,user);
    }
}
