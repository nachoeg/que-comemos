import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Food } from '../../models/food/food';
import { NewFood } from '../../models/new-food/new-food';

@Injectable({
  providedIn: 'root',
})
export class FoodsService {
  private baseUrl = 'http://localhost:8080/api/comidas'; // API comida

  constructor(private http: HttpClient) {}

  getFoods(): Observable<Food[]> {
    return this.http.get<Food[]>(this.baseUrl);
  }

  createFood(food: FormData): Observable<any> {
    return this.http.post(this.baseUrl, food);
  }

  deleteFood(foodId: number): Observable<any> {
    const url = `${this.baseUrl}/${foodId}`;
    return this.http.delete(url);
  }

  getFoodById(foodId: number): Observable<Food> {
    const url = `${this.baseUrl}/${foodId}`;
    return this.http.get<Food>(url);
  }

  updateFood(food: FormData, foodId: number): Observable<any> {
    const url = `${this.baseUrl}/${foodId}`;
    return this.http.put(url, food);
  }
}
