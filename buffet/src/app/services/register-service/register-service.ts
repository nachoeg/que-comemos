import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable} from 'rxjs';
import { Usuario } from '../../models/usuario/usuario';
import { environment } from '../../../environments/environment';
@Injectable({
    providedIn: "root",
  })
export class RegisterService {
    private baseUrl = environment.apiUrl + '/registro';
    
    constructor(private http: HttpClient) {}

    registerUser(user: Usuario): Observable<any>{
        return this.http.post(this.baseUrl,user);
      }
}
