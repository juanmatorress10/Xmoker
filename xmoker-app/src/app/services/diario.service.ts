import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface DiarioEntrada {
  id?: number;
  usuarioId: number;
  fechaCreacion?: string;
  emocionDelDia: string;
  estrategiasUsadas: string;
  complicacionesEncontradas: string;
}

@Injectable({
  providedIn: 'root'
})
export class DiarioService {
  private apiUrl = 'http://localhost:9090/api/diario'; // Asegúrate de cambiarlo si tu backend tiene otra URL

  constructor(private http: HttpClient) {}

  getEntradas(usuarioId: number): Observable<DiarioEntrada[]> {
    return this.http.get<DiarioEntrada[]>(`${this.apiUrl}/${usuarioId}`);
  }

  crearEntrada(usuarioId: number, entrada: DiarioEntrada): Observable<DiarioEntrada> {
    return this.http.post<DiarioEntrada>(`${this.apiUrl}/${usuarioId}`, entrada);
  }

  eliminarEntrada(idEntrada: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${idEntrada}`);
  }
}
