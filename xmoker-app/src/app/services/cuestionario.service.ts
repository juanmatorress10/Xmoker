import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class CuestionarioService {
  private apiUrl = 'http://localhost:9090/api/cuestionario';

  constructor(private http: HttpClient) {}

  enviarRespuestas(usuarioId: number, respuestas: { valor: number }[]): Observable<any> {
    return this.http.post(`${this.apiUrl}/respuestas/${usuarioId}`, respuestas);
  }

  obtenerPerfil(usuarioId: number): Observable<any> {
    return this.http.get(`${this.apiUrl}/perfil/${usuarioId}`);
  }
}
