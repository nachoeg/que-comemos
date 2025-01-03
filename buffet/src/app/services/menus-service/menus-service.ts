import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Menu } from '../../models/menu/menu';
import { NewMenu } from '../../models/new-menu/new-menu';
@Injectable({
  providedIn: 'root',
})
export class MenusService {
  private baseUrl = 'http://localhost:8080/api/menus'; // API menu

  constructor(private http: HttpClient) {}

  getMenus(): Observable<Menu[]> {
    return this.http.get<Menu[]>(this.baseUrl);
  }

  createMenu(menu: NewMenu): Observable<any> {
    return this.http.post(this.baseUrl, menu);
  }

  deleteMenu(menuId: number): Observable<any> {
    const url = `${this.baseUrl}/${menuId}`;
    return this.http.delete(url);
  }

  updateMenu(menuId: number, menu: NewMenu): Observable<any> {
    const url = `${this.baseUrl}/${menuId}`;
    return this.http.put(url, menu);
  }

  getMenuById(menuId: number): Observable<Menu> {
    const url = `${this.baseUrl}/${menuId}`; //url+id del menu a buscar
    return this.http.get<Menu>(url);
  }
}
