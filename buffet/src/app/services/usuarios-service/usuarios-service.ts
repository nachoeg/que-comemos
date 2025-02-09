import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { Usuario } from '../../models/usuario/usuario';
@Injectable({
    providedIn: "root",
  })
export class UsuariosService {
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

    getUsuarios(): Observable<Usuario[]> {
      return this.http.get<Usuario[]>(this.baseUrl);
    }
  
    getUsuario(id: number): Observable<Usuario> {
      return this.http.get<Usuario>(`${this.baseUrl}/${id}`);
    }
  
    createUsuario(usuario: Usuario): Observable<Usuario> {
      return this.http.post<Usuario>(this.baseUrl, usuario);
    }
  
    updateUsuario(id: number, usuario: FormData): Observable<Usuario> {
      return this.http.put<Usuario>(`${this.baseUrl}/${id}`, usuario);
    }
  
    deleteUsuario(id: number): Observable<string> {
      return this.http.delete<string>(`${this.baseUrl}/${id}`);
    }
}
