import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Usuario } from '../../models/usuario/usuario';
@Injectable({
    providedIn: "root",
  })
export class UsuariosServicio {
    private baseUrl = 'http://localhost:8080/api/usuarios';
    constructor(private http: HttpClient) {}

    registerUser(user: Usuario): Observable<any>{
      return this.http.post(this.baseUrl,user);
    }
}
