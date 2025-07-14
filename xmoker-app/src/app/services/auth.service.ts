import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private apiUsuarios = 'http://localhost:9090/api/usuarios';
  private apiAuth     = 'http://localhost:9090/api/auth';

  constructor(private http: HttpClient) {}

  // 🔹 Registro de usuario
  registrar(usuario: any): Observable<{ usuario: any; token: string }> {
    // Cambia tu endpoint backend para que devuelva { usuario, token }
    return this.http.post<{ usuario: any; token: string }>(
      `${this.apiUsuarios}`,
      usuario
    );
  }

  // 🔹 Login de usuario
  login(credentials: any): Observable<string> {
    // Te devuelve sólo el token en texto
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

  // — getters para localStorage —
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
