import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Menu } from '../../models/menu/menu';
import { NewMenu } from '../../models/new-menu/new-menu';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class MenusService {
  private baseUrl = environment.apiUrl + '/menus'; // API menu

  constructor(private http: HttpClient) {}

  getMenus(): Observable<Menu[]> {
    return this.http.get<Menu[]>(this.baseUrl);
  }

  getMenusActive(): Observable<Menu[]> {
    const url = `${this.baseUrl}/activos`;
    return this.http.get<Menu[]>(url);
  }

  createMenu(menu: FormData): Observable<any> {
    return this.http.post(this.baseUrl, menu);
  }

  deleteMenu(menuId: number): Observable<any> {
    const url = `${this.baseUrl}/${menuId}`;
    return this.http.delete(url);
  }

  updateMenu(menuId: number, menu: FormData): Observable<any> {
    const url = `${this.baseUrl}/${menuId}`;
    return this.http.put(url, menu);
  }

  getMenuById(menuId: number): Observable<Menu> {
    const url = `${this.baseUrl}/${menuId}`; //url+id del menu a buscar
    return this.http.get<Menu>(url);
  }

  getMenusPorDia(dia: string): Observable<Menu[]> {
    const params = new HttpParams().set('dia', dia);
    return this.http.get<Menu[]>(`${this.baseUrl}/dia`, { params });
  }
}
