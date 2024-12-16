import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Menu } from '../../models/menu/menu';
@Injectable({
    providedIn: 'root'
  })
export class MenuesServicio {
    private baseUrl = 'http://localhost:8080/api/menus'; // Reemplaza con la URL de tu backend

  constructor(private http: HttpClient) { }

  getMenus(): Observable<Menu[]> {
    return this.http.get<Menu[]>(this.baseUrl);
  }

}
