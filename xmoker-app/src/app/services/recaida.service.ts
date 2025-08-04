import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface Recaida {
  fecha: Date;
  cantidadFumada: number;
  motivo: string;
}

@Injectable({ providedIn: 'root' })
export class RecaidaService {
  private apiUrl = `${environment.apiUrl}/recaidas`;

  constructor(private http: HttpClient) {}

  registrarRecaida(userId: number, recaida: Recaida): Observable<string> {
    return this.http.post(`${this.apiUrl}/${userId}`, recaida, {
      responseType: 'text' as const
    });
  }
}
