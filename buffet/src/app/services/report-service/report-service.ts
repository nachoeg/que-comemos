import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ReportService {
    private baseUrl = environment.apiUrl + '/pedidos';

    constructor(private http: HttpClient) { }

    getReporteMenusPorPeriodo(periodo: string): Observable<{ [key: string]: number }> {
        return this.http.get<{ [key: string]: number }>(`${this.baseUrl}/reporte-menus/${periodo}`);
      }
    
      getReporteComidasPorPeriodo(periodo: string): Observable<{ [key: string]: number }> {
        return this.http.get<{ [key: string]: number }>(`${this.baseUrl}/reporte-comidas/${periodo}`);
      }
    
      getReporteMontosPorPeriodo(periodo: string): Observable<{ [key: string]: number }> {
        return this.http.get<{ [key: string]: number }>(`${this.baseUrl}/reporte-montos/${periodo}`);
      }
    
}
