import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { NewEstructura } from '../../models/new-estructura/new-estructura';
import { Injectable } from '@angular/core';
import { Estructura } from '../../models/estructura/estructura';

@Injectable({
  providedIn: 'root',
})
export class EstructuraService {
  private baseUrl = 'http://localhost:8080/api/estructuras';

  constructor(private http: HttpClient) {}

  createEstructura(menuId: number, estructura: NewEstructura): Observable<any> {
    const url = `${this.baseUrl}/${menuId}`;
    return this.http.post(url, estructura);
  }

  deleteEstructura(estructuraId: number): Observable<any> {
    const url = `${this.baseUrl}/${estructuraId}`;
    console.log('url: ' + url);
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
    console.log(url);
    return this.http.get<Estructura>(url);
  }
}
