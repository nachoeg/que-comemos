import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, catchError, Observable, tap, throwError } from 'rxjs';
import { Usuario } from '../../models/usuario/usuario';
@Injectable({
    providedIn: "root",
  })
export class RegisterService {
    private baseUrl = 'http://localhost:8080/api/registro';
    
    constructor(private http: HttpClient) {}

    registerUser(user: Usuario): Observable<any>{
        return this.http.post(this.baseUrl,user);
      }
}
