import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, catchError, Observable, tap, throwError } from 'rxjs';
import { Usuario } from '../../models/usuario/usuario';
import { environment } from '../../../environments/environment';
@Injectable({
    providedIn: "root",
  })
export class UsuariosService {
    private baseUrl = environment.apiUrl + '/usuarios';
    
    constructor(private http: HttpClient) {}

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
    
    updateRolUsuario(id: number, rol: FormData): Observable<any> {
      return this.http.put<any>(`${this.baseUrl}/${id}/rol`, rol);
    }
  
    deleteUsuario(id: number): Observable<string> {
      return this.http.delete<string>(`${this.baseUrl}/${id}`);
    }
}
