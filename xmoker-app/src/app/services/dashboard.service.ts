import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class DashboardService {

  private apiBaseUrl = 'http://localhost:9090/api'; // Ajusta según tu backend

  constructor(private http: HttpClient) {}

  // 🔹 MÉTRICAS
  obtenerMetricas(idUsuario: number): Observable<any> {
    return this.http.get(`${this.apiBaseUrl}/metricas/${idUsuario}`);
  }

  // 🔹 DIARIO
  listarEntradasDiario(idUsuario: number): Observable<any> {
    return this.http.get(`${this.apiBaseUrl}/diario/${idUsuario}`);
  }

  crearEntradaDiario(idUsuario: number, entrada: any): Observable<any> {
    return this.http.post(`${this.apiBaseUrl}/diario/${idUsuario}`, entrada);
  }

  eliminarEntradaDiario(idEntrada: number): Observable<any> {
    return this.http.delete(`${this.apiBaseUrl}/diario/${idEntrada}`);
  }
}
