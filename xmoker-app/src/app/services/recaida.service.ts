import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

export interface Recaida {
  fecha: Date;
  cantidadFumada: number;
  motivo: string;
}

@Injectable({ providedIn: 'root' })
export class RecaidaService {
  private apiUrl = 'http://localhost:9090/api/recaidas';

  constructor(private http: HttpClient) {}

  registrarRecaida(userId: number, recaida: Recaida): Observable<string> {
    return this.http.post(`${this.apiUrl}/${userId}`, recaida, {
      responseType: 'text' as const
    });
  }
}
