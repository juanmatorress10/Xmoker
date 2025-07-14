import { Component, Input, Output, EventEmitter, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { CommentComponent } from './app-comment.component';
import { CommentFormComponent } from './app-comment-form.component';
import { ComunidadService, ReaccionDTO } from '../../services/comunidad.service';

@Component({
  standalone: true,
  selector: 'app-post-card',
  imports: [CommonModule, CommentFormComponent, CommentComponent, MatPaginatorModule],
  template: `
<div class="group relative my-8 p-6 rounded-2xl bg-gradient-to-b from-white to-blue-50 shadow-xl transform transition-transform duration-300 hover:-translate-y-1">
  <!-- Autor -->
  <div class="flex items-center gap-3 mb-4">
    <img
      *ngIf="post?.autor?.fotoPerfilUrl"
      [src]="'http://localhost:9090/' + post.autor.fotoPerfilUrl"
      (error)="cargarImagenPorDefecto($event)"
      class="w-10 h-10 rounded-full border-2 border-blue-200"
    />
    <div>
      <p class="text-sm font-semibold text-gray-800">{{ post.autor?.nombre }}</p>
      <p class="text-xs text-gray-500">Publicado el {{ post.fechaPublicacion | date:'short' }}</p>
    </div>
    <button
      *ngIf="post.autor?.id === currentUserId"
      (click)="eliminar()"
      class="ml-auto text-red-500 text-xs hover:underline"
    >
      🗑️ Eliminar
    </button>
  </div>

  <!-- Contenido -->
  <p class="text-gray-800 mb-4 leading-relaxed whitespace-pre-line">{{ post.contenido }}</p>

  <!-- Imagen -->
  <img
    *ngIf="post.imagenUrl"
    [src]="'http://localhost:9090' + post.imagenUrl"
    alt="imagen post"
    class="w-full rounded-xl object-cover border-2 border-blue-100 mb-4"
  />

  <!-- Reacciones -->
  <div class="flex flex-wrap gap-3 mt-2">
    <button
      (click)="reaccionar('LIKE')"
      [class.bg-blue-100]="miReaccion === 'LIKE'"
      class="flex items-center gap-1 px-3 py-1 rounded-full bg-white hover:bg-blue-100 transition"
    >
      👍 <span class="text-sm font-medium">{{ reaccionCounts['LIKE'] }}</span>
    </button>
    <button
      (click)="reaccionar('APOYO')"
      [class.bg-blue-100]="miReaccion === 'APOYO'"
      class="flex items-center gap-1 px-3 py-1 rounded-full bg-white hover:bg-blue-100 transition"
    >
      🤝 <span class="text-sm font-medium">{{ reaccionCounts['APOYO'] }}</span>
    </button>
    <button
      (click)="reaccionar('UTIL')"
      [class.bg-blue-100]="miReaccion === 'UTIL'"
      class="flex items-center gap-1 px-3 py-1 rounded-full bg-white hover:bg-blue-100 transition"
    >
      💡 <span class="text-sm font-medium">{{ reaccionCounts['UTIL'] }}</span>
    </button>
    <button
      (click)="reaccionar('FUEGUITO')"
      [class.bg-blue-100]="miReaccion === 'FUEGUITO'"
      class="flex items-center gap-1 px-3 py-1 rounded-full bg-white hover:bg-blue-100 transition"
    >
      🔥 <span class="text-sm font-medium">{{ reaccionCounts['FUEGUITO'] }}</span>
    </button>
    <button
      (click)="toggleComentarios()"
      class="flex items-center gap-1 px-3 py-1 rounded-full bg-white hover:bg-blue-100 transition"
    >
      💬 <span class="text-sm font-medium">{{ comentarios.length }}</span>
    </button>
  </div>

  <!-- Comentarios -->
  <div *ngIf="mostrarComentarios" class="mt-4 border-t pt-4 space-y-4">
    <app-comment-form
      [postId]="post.id"
      (onComment)="cargarComentarios()"
    ></app-comment-form>

    <div *ngIf="comentarios.length === 0" class="text-sm text-gray-500">
      Sé el primero en comentar
    </div>

    <div *ngFor="let comentario of comentariosPaginados" class="mt-2">
      <app-comment
        [comentario]="comentario"
        [currentUserId]="currentUserId"
        (onDelete)="cargarComentarios()"
      ></app-comment>
    </div>

    <mat-paginator
      *ngIf="comentarios.length > comentariosPorPagina"
      [length]="comentarios.length"
      [pageSize]="comentariosPorPagina"
      [pageIndex]="paginaActual"
      [pageSizeOptions]="[3,5,10]"
      (page)="cambiarPagina($event)"
      class="mt-2"
    ></mat-paginator>
  </div>
</div>
`
})
export class PostCardComponent implements OnInit {
  @Input() post!: any;
  @Input() currentUserId!: number;
  @Output() onEliminar = new EventEmitter<void>();

  comentarios: any[] = [];
  mostrarComentarios = false;

  // Paginación
  comentariosPorPagina = 3;
  paginaActual = 0;

  // Reacciones
  reaccionCounts: Record<string, number> = {
    LIKE: 0,
    APOYO: 0,
    UTIL: 0,
    FUEGUITO: 0
  };
  miReaccion?: string;

  constructor(
    private comunidadService: ComunidadService,
    private http: HttpClient
  ) {}

  ngOnInit() {
    this.cargarComentarios();
    this.cargarReacciones();
  }

  // Carga comentarios
  cargarComentarios() {
    this.http.get<any[]>(`http://localhost:9090/api/comentarios/${this.post.id}`).subscribe({
      next: data => {
        this.comentarios = data;
        this.paginaActual = 0;
      },
      error: err => console.error('❌ Error cargando comentarios', err)
    });
  }

  get comentariosPaginados() {
    const start = this.paginaActual * this.comentariosPorPagina;
    return this.comentarios.slice(start, start + this.comentariosPorPagina);
  }

  cambiarPagina(event: PageEvent) {
    this.paginaActual = event.pageIndex;
    this.comentariosPorPagina = event.pageSize;
  }

  cargarImagenPorDefecto(event: Event) {
    (event.target as HTMLImageElement).src = 'http://localhost:9090/images/user_icon.png';
  }

  // Togglear comentarios
  toggleComentarios() {
    this.mostrarComentarios = !this.mostrarComentarios;
    if (this.mostrarComentarios) {
      this.cargarComentarios();
    }
  }

  // Eliminar post
  eliminar() {
    this.onEliminar.emit();
  }

  // Carga y cuenta las reacciones, determina miReaccion
  cargarReacciones() {
    this.comunidadService.getReacciones(this.post.id).subscribe({
      next: reacts => {
        // Reset counts
        for (const key of Object.keys(this.reaccionCounts)) {
          this.reaccionCounts[key] = 0;
        }
        this.miReaccion = undefined;

        reacts.forEach((r: ReaccionDTO) => {
          this.reaccionCounts[r.tipo] = (this.reaccionCounts[r.tipo] || 0) + 1;
          if (r.usuarioId === this.currentUserId) {
            this.miReaccion = r.tipo;
          }
        });
      },
      error: err => console.error('❌ Error cargando reacciones', err)
    });
  }

  // Reacciona y refresca contadores
  reaccionar(tipo: string) {
    this.comunidadService.reaccionarAPost(this.post.id, tipo).subscribe({
      next: () => this.cargarReacciones(),
      error: err => console.error('❌ Error al reaccionar', err)
    });
  }
}
