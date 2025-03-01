import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { NewEstructura } from '../../models/new-estructura/new-estructura';
import { Injectable } from '@angular/core';
import { Estructura } from '../../models/estructura/estructura';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class EstructuraService {
  private baseUrl = environment.apiUrl + '/estructuras';

  constructor(private http: HttpClient) {}

  createEstructura(menuId: number, estructura: NewEstructura): Observable<any> {
    const url = `${this.baseUrl}/${menuId}`;
    return this.http.post(url, estructura);
  }

  deleteEstructura(estructuraId: number): Observable<any> {
    const url = `${this.baseUrl}/${estructuraId}`;
    return this.http.delete(url);
  }

  addFoodToEstructura(estructuraId: number, foodId: number): Observable<any> {
    const url = `${this.baseUrl}/${estructuraId}/addComida/${foodId}`;
    return this.http.post(url, {});
  }

  removeFoodFromEstructura(
    estructuraId: number,
    foodId: number
  ): Observable<any> {
    const url = `${this.baseUrl}/${estructuraId}/removeComida/${foodId}`;
    return this.http.post(url, {});
  }

  getEstructuraById(estructuraId: number): Observable<Estructura> {
    const url = `${this.baseUrl}/${estructuraId}`;
    return this.http.get<Estructura>(url);
  }
}
