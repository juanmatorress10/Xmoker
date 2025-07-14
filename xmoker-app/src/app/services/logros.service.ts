import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { UsuarioLogro } from '../models/logros.model';

@Injectable({
  providedIn: 'root'
})
export class LogrosService {
  private API_URL = 'http://localhost:9090/api/logros';

  constructor(private http: HttpClient) {}

  getLogrosPorUsuario(id: number): Observable<UsuarioLogro[]> {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json'
    });

    return this.http.get<UsuarioLogro[]>(`${this.API_URL}/${id}`, { headers });
  }

  reclamarLogro(logroId: number): Observable<any> {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json'
    });

    return this.http.put(`${this.API_URL}/reclamar/${logroId}`, {}, { headers });
  }

  registrarRetoAnsiedad(userId: number): Observable<any> {
  return this.http.post(`${this.API_URL}/ansiedad/${userId}`, {});
}
}
