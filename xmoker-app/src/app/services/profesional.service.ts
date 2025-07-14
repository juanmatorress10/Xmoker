import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ProfesionalService {

  private apiUrl = 'http://localhost:8080/api/profesionales'; // Ajusta tu URL backend aquí

  constructor(private http: HttpClient) {}

  guardarDatosProfesional(datos: any): Observable<any> {
    return this.http.post(`${this.apiUrl}`, datos);
  }
}
