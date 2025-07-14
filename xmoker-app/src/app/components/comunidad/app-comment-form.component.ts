import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ComunidadService } from '../../services/comunidad.service'; // ajusta según tu estructura

@Component({
  selector: 'app-comment-form',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <form (submit)="enviar()" class="flex items-center gap-2 mt-2">
      <input
        [(ngModel)]="contenido"
        name="contenido"
        class="flex-1 border border-blue-200 rounded-lg p-2 text-sm focus:outline-none focus:ring focus:ring-blue-300"
        placeholder="Escribe un comentario..."
        required
      />
      <button class="text-blue-600 hover:text-blue-800 text-sm" type="submit">💬 Comentar</button>
    </form>
  `
})
export class CommentFormComponent {
  @Input() postId!: number;
  @Output() onComment = new EventEmitter<void>();

  contenido = '';

  constructor(private comunidadService: ComunidadService) {}

  enviar() {
    if (!this.contenido.trim()) return;

    this.comunidadService.comentarPost(this.postId, this.contenido).subscribe({
      next: () => {
        this.onComment.emit(); // notifica al padre para recargar comentarios
        this.contenido = '';
      },
      error: err => {
        console.error('❌ Error al comentar', err);
        alert('Error al publicar comentario');
      }
    });
  }
}
