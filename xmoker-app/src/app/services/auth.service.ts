import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';



@Injectable({ providedIn: 'root' })
export class AuthService {
  private apiUsuarios = `${environment.apiUrl}/usuarios`;
  private apiAuth     = `${environment.apiUrl}/auth`;

  constructor(private http: HttpClient) {}

  // 🔹 Registro de usuario
  registrar(usuario: any): Observable<{ usuario: any; token: string }> {
    return this.http.post<{ usuario: any; token: string }>(
      this.apiUsuarios,
      usuario
    );
  }

  // 🔹 Login de usuario
  login(credentials: any): Observable<string> {
    return this.http.post(
      `${this.apiAuth}/login`,
      credentials,
      { responseType: 'text' }
    );
  }

  // 🔹 Obtener usuario autenticado
  obtenerUsuarioActual(): Observable<any> {
    const token = this.getToken();
    const headers = new HttpHeaders({ 'Authorization': `Bearer ${token}` });
    return this.http.get(`${this.apiUsuarios}/me`, { headers });
  }

  // — Getters para localStorage —
  getToken(): string | null {
    return localStorage.getItem('token');
  }

  getUsuario(): any {
    const raw = localStorage.getItem('usuario');
    return raw ? JSON.parse(raw) : null;
  }

  logout() {
    localStorage.clear();
  }
}

// realizo un cambio en el nombre de la variable para que sea más descriptivo
// y fácil de entender. En lugar de "apiUsuarios", uso "apiAuth" para la URL de autenticación.