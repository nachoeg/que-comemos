import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Menu } from '../../models/menu/menu';
import { NewMenu } from '../../models/new-menu/new-menu';
@Injectable({
    providedIn: 'root'
  })
export class MenuesServicio {
    private baseUrl = 'http://localhost:8080/api/menus'; // Reemplaza con la URL de tu backend

  constructor(private http: HttpClient) { }

  getMenus(): Observable<Menu[]> {
    return this.http.get<Menu[]>(this.baseUrl);
  }

  createMenu(menu: NewMenu): Observable<any> {
    return this.http.post(this.baseUrl, menu);
  }

  deleteMenu(menuId: number): Observable<any> {
    const url = `${this.baseUrl}/${menuId}`; // Construct the delete URL with id
    return this.http.delete(url);
  }

}
