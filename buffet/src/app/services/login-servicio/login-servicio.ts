import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Usuario } from '../../models/usuario/usuario';
@Injectable({
    providedIn: "root",
})
export class LoginServicio {
    private baseUrl = 'http://localhost:8080/api/autenticacion/login';
    private isUserLoggedIn;
    public userLogged: Usuario | null = null;

    constructor(private http: HttpClient) {
        this.isUserLoggedIn = false;
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


    login(user: any): Observable<any> {
        const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
        return this.http.post(this.baseUrl, user, { headers });
    }


}
