import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Sugerencia } from '../../models/sugerencias/sugerencias';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class SugerenciasService {
  private baseUrl = environment.apiUrl + '/sugerencias'; 

  constructor(private http: HttpClient) {}

  getSugerencias(): Observable<Sugerencia[]> {
    return this.http.get<Sugerencia[]>(this.baseUrl);
  }

  getSugerencia(id: number): Observable<Sugerencia> {
    return this.http.get<Sugerencia>(`${this.baseUrl}/${id}`);
  }

  createSugerencia(sugerencia: Sugerencia): Observable<Sugerencia> {
    console.log('createSugerencia', sugerencia);
    return this.http.post<Sugerencia>(this.baseUrl, sugerencia);
  }

  updateSugerencia(id: number, sugerencia: Sugerencia): Observable<Sugerencia> {
    return this.http.put<Sugerencia>(`${this.baseUrl}/${id}`, sugerencia);
  }

  deleteSugerencia(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}
