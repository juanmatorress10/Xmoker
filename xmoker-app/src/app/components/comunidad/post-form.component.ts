import { Component, EventEmitter, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Component({
  standalone: true,
  selector: 'app-post-form',
  imports: [CommonModule, FormsModule],
  template: `
<form (submit)="publicar()" class="bg-white p-4 rounded-xl shadow-md space-y-4 border border-blue-200">
  <textarea
    [(ngModel)]="contenido"
    name="contenido"
    rows="3"
    placeholder="Comparte tu experiencia o motiva a otros..."
    class="w-full border border-blue-300 p-3 rounded resize-none focus:outline-none focus:ring-2 focus:ring-blue-400"
    required
  ></textarea>

  <label class="block text-sm text-blue-700 cursor-pointer">
    <span class="btn btn-outline-secondary btn-sm">📷 Subir imagen</span>
    <input type="file" (change)="seleccionarImagen($event)" accept="image/*" hidden />
  </label>

  <img *ngIf="imagenPreview" [src]="imagenPreview" class="mt-2 rounded max-h-48 w-full object-cover" />

  <button class="bg-blue-600 hover:bg-blue-700 text-white py-2 px-4 w-full rounded-lg transition">
    📢 Publicar
  </button>
</form>
`
})
export class PostFormComponent {
  contenido = '';
  imagenPreview?: string;
  imagenUrlSubida?: string;

  @Output() onPost = new EventEmitter<{ contenido: string; imagenUrl?: string }>();

  constructor(private http: HttpClient) {}

  seleccionarImagen(event: Event) {
    const input = event.target as HTMLInputElement;
    const archivo = input.files?.[0];
    if (!archivo) return;

    const lector = new FileReader();
    lector.onload = () => {
      this.imagenPreview = lector.result as string;
    };
    lector.readAsDataURL(archivo);

    // Subir imagen al backend
    const formData = new FormData();
    formData.append('archivo', archivo);

    const headers = new HttpHeaders({
      Authorization: `Bearer ${localStorage.getItem('token') || ''}`
    });

    this.http.post('http://localhost:9090/api/posts-grupo/subir-imagen', formData, {
      headers,
      responseType: 'text'
    }).subscribe({
      next: (rutaRelativa: string) => {
        this.imagenUrlSubida = rutaRelativa;
        console.log('✅ Imagen subida a:', rutaRelativa);
      },
      error: (err) => {
        console.error('❌ Error al subir imagen', err);
        alert('No se pudo subir la imagen');
      }
    });

  }

  publicar() {
    if (!this.contenido.trim()) return;

    this.onPost.emit({
      contenido: this.contenido,
      imagenUrl: this.imagenUrlSubida
    });

    this.contenido = '';
    this.imagenPreview = undefined;
    this.imagenUrlSubida = undefined;
  }
}
