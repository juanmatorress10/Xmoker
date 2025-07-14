import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UsuarioService {
  private API_URL = 'http://localhost:9090/api/usuarios';

  constructor(private http: HttpClient) {}

  actualizarUsuario(id: number, datos: {
    nombre?: string;
    email?: string;
    password?: string;
    fotoPerfilUrl?: string;
  }): Observable<any> {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json'
    });

    return this.http.put(`${this.API_URL}/${id}`, datos, { headers });
  }

  subirFoto(id: number, archivo: File): Observable<string> {
    const formData = new FormData();
    formData.append('file', archivo);
    return this.http.post<string>(`${this.API_URL}/${id}/foto`, formData, {
      responseType: 'text' as 'json'
    });
  }
}
