import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ComunidadService } from '../../services/comunidad.service';

export interface ComentarioDTO {
  id: number;
  contenido: string;
  fechaComentario: string;
  autorId: number;
  autorNombre: string;
  autorFotoUrl: string;
}

@Component({
  standalone: true,
  selector: 'app-comment',
  imports: [CommonModule],
  template: `
<div class="bg-gray-50 border border-gray-200 rounded-lg p-3 text-sm text-gray-800 relative group">
  <div class="flex items-center gap-2 mb-1">
    <p class="font-medium text-blue-700">{{ comentario?.autorNombre }}</p>
  </div>

  <p>{{ comentario?.contenido }}</p>

  <p class="text-xs text-gray-400 mt-1">
    {{ comentario?.fechaComentario | date: 'short' }}
  </p>

  <button
    *ngIf="comentario && comentario.autorId === currentUserId"
    (click)="eliminarComentario()"
    class="absolute top-2 right-2 text-red-500 text-xs hover:underline opacity-0 group-hover:opacity-100 transition-opacity"
  >
    🗑️
  </button>
</div>
`
})
export class CommentComponent {
  @Input() comentario: ComentarioDTO | null = null;
  @Input() currentUserId: number = 0;
  @Output() onDelete = new EventEmitter<void>();

  constructor(private comunidadService: ComunidadService) {}

  eliminarComentario(): void {
    if (!this.comentario) return;

    const confirmado = confirm('¿Eliminar este comentario?');
    if (!confirmado) return;

    this.comunidadService.eliminarComentario(this.comentario.id).subscribe({
      next: () => this.onDelete.emit(),
      error: err => {
        console.error('❌ Error al eliminar comentario', err);
        alert('No se pudo eliminar el comentario');
      }
    });
  }

  cargarImagenPorDefecto(event: Event) {
    (event.target as HTMLImageElement).src = 'http://localhost:9090/images/user_icon.png';
  }

  getFotoPerfilUrl(): string {
    return 'http://localhost:9090' + (this.comentario?.autorFotoUrl || '/images/user_icon.png');
  }
}
