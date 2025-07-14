import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';


export interface ReaccionDTO {
  id: number;
  tipo: string;
  usuarioId: number;
  postId: number;
}

@Injectable({
  providedIn: 'root'
})
export class ComunidadService {
  private API_URL = 'http://localhost:9090/api';

  private jsonHeaders = new HttpHeaders({
    'Content-Type': 'application/json'
  });

  constructor(private http: HttpClient) {}

  // ✔ Crear grupo privado
  crearGrupo(grupo: { nombre: string; descripcion: string; esPrivado: boolean }): Observable<any> {
    return this.http.post(`${this.API_URL}/grupos`, grupo, { headers: this.jsonHeaders });
  }

  // ✔ Obtener grupo por ID
  getGrupoPorId(grupoId: number): Observable<any> {
    return this.http.get<any>(`${this.API_URL}/grupos/${grupoId}`);
  }

  // ✔ Ver si tengo grupo
  tengoGrupo(): Observable<any> {
    return this.http.get(`${this.API_URL}/grupos/tengo-grupo`);
  }

  abandonarGrupo(grupoId: number): Observable<string> {
    return this.http.delete(
      `${this.API_URL}/miembros/abandonar/${grupoId}`,
      { responseType: 'text' }
    );
  }

  // ✔ Ver mis grupos
  getMisGrupos(): Observable<any[]> {
    return this.http.get<any[]>(`${this.API_URL}/miembros/mis-grupos`);
  }

  // ✔ Unirse a un grupo por código
unirsePorCodigo(codigo: string): Observable<string> {
  return this.http.post(
    `${this.API_URL}/miembros/unirse-codigo`,
    { codigo },
    {
      headers: this.jsonHeaders,
      responseType: 'text'  // <— aquí
    }
  );
}



  // ✔ Obtener miembros de un grupo
getMiembrosGrupo(grupoId: number): Observable<{ id: number; nombre: string }[]> {
  return this.http.get<{ id: number; nombre: string }[]>(
    `${this.API_URL}/grupos/${grupoId}/miembros`
  );
}
  // ✔ Obtener posts del grupo
  getPostsGrupo(grupoId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.API_URL}/posts-grupo/${grupoId}`);
  }

  // ✔ Crear publicación (permite imagen)
  crearPost(grupoId: number, contenido: string, imagen?: File): Observable<any> {
    const formData = new FormData();
    formData.append('contenido', contenido);
    if (imagen) {
      formData.append('imagen', imagen);
    }
    return this.http.post(`${this.API_URL}/posts-grupo/${grupoId}`, formData);
  }

  // ✔ Comentar en un post
  comentarPost(postId: number, contenido: string): Observable<any> {
    const body = new URLSearchParams();
    body.set('contenido', contenido);

    const formHeaders = new HttpHeaders({ 'Content-Type': 'application/x-www-form-urlencoded' });
    return this.http.post(`${this.API_URL}/comentarios/${postId}`, body.toString(), { headers: formHeaders });
  }

  // ✔ Obtener comentarios de un post
  getComentarios(postId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.API_URL}/comentarios/${postId}`);
  }

  // ✔ Reaccionar a un post
reaccionarAPost(postId: number, tipo: string): Observable<any> {
  const headers = new HttpHeaders({
    Authorization: `Bearer ${localStorage.getItem('token') || ''}`
  });
  return this.http.post(
    `${this.API_URL}/reacciones/${postId}?tipo=${tipo}`,
    {},
    { headers }
  );
}
  // ✔ Obtener reacciones de un post
  getReacciones(postId: number): Observable<ReaccionDTO[]> {
    return this.http.get<ReaccionDTO[]>(`${this.API_URL}/reacciones/${postId}`);
  }

eliminarPost(postId: number): Observable<string> {
  return this.http.delete(`${this.API_URL}/posts-grupo/eliminar/${postId}`, {
    responseType: 'text' as const
  });
}

  eliminarComentario(comentarioId: number): Observable<any> {
    return this.http.delete(`${this.API_URL}/comentarios/eliminar/${comentarioId}`);
  }


  // RETOS
getRetosGrupo(grupoId: number): Observable<any[]> {
  return this.http.get<any[]>(`${this.API_URL}/retos/${grupoId}`);
}

unirseAReto(retoId: number): Observable<any> {
  const headers = new HttpHeaders({
    Authorization: `Bearer ${localStorage.getItem('token') || ''}`
  });

  return this.http.post(`${this.API_URL}/retos/unirse/${retoId}`, {}, { headers });
}

crearReto(grupoId: number, reto: any): Observable<any> {
  return this.http.post(`${this.API_URL}/retos/${grupoId}`, reto, {
    headers: this.jsonHeaders
  });
}




completarReto(retoId: number): Observable<any> {
  const headers = new HttpHeaders({
    Authorization: `Bearer ${localStorage.getItem('token') || ''}`
  });

  return this.http.post(`${this.API_URL}/retos/completar/${retoId}`, {}, { headers });
}

marcarRetoNoCompletado(retoId: number): Observable<any> {
  const headers = new HttpHeaders({
    Authorization: `Bearer ${localStorage.getItem('token') || ''}`
  });

  return this.http.post(`${this.API_URL}/retos/no-completado/${retoId}`, {}, { headers });
}

obtenerRetoPorId(retoId: number): Observable<any> {
  return this.http.get(`${this.API_URL}/retos/resumen/${retoId}`);
}

// ESTADÍSTICAS
getEstadisticasGrupo(grupoId: number): Observable<any> {
  return this.http.get(`${this.API_URL}/estadisticas/${grupoId}`);
}

getComparativaGrupo(grupoId: number): Observable<any> {
  return this.http.get(`${this.API_URL}/estadisticas/${grupoId}/comparativa`);
}

crearPostConImagenUrl(grupoId: number, contenido: string, imagenUrl?: string): Observable<any> {
  const body = new URLSearchParams();
  body.set('contenido', contenido);
  if (imagenUrl) body.set('imagenUrl', imagenUrl);

  const headers = new HttpHeaders({
    'Content-Type': 'application/x-www-form-urlencoded',
    Authorization: `Bearer ${localStorage.getItem('token') || ''}`
  });

  return this.http.post(`http://localhost:9090/api/posts-grupo/${grupoId}/crear-con-url`, body.toString(), { headers });
}

getResumenReto(retoId: number): Observable<any> {
  return this.http.get(`${this.API_URL}/retos/resumen/${retoId}`);
}

}
