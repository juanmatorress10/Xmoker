import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LogrosService } from '../../../services/logros.service';
import { UsuarioLogro } from '../../../models/logros.model';

@Component({
  selector: 'app-logro-card',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './logro-card.component.html',
  styleUrls: ['./logro-card.component.css']
})
export class LogroCardComponent {
  @Input() usuarioLogro!: UsuarioLogro;
  @Input() completado = false;
  @Input() disponibleParaReclamar = false;
  @Output() onReclamado = new EventEmitter<void>();

  reclamando = false;
  mensajeExito = '';
  animacionReclamado = false;

  constructor(private logroService: LogrosService) {}

reclamarLogro() {
  if (!this.usuarioLogro.id || this.usuarioLogro.reclamado) return;

  const puedeReclamar = this.usuarioLogro.progresoActual >= this.usuarioLogro.logro.valorObjetivo;

  if (!puedeReclamar) return;

  this.reclamando = true;

  this.logroService.reclamarLogro(this.usuarioLogro.id).subscribe({
    next: () => {
      this.usuarioLogro.reclamado = true;
      this.usuarioLogro.completado = true; // ya estaba completo realmente
      this.mensajeExito = '🎉 ¡Recompensa reclamada!';
      this.reclamando = false;

      this.animacionReclamado = true;
      this.onReclamado.emit(); // notifica al padre para recargar

      setTimeout(() => {
        this.animacionReclamado = false;
        this.mensajeExito = '';
      }, 1500);
    },
    error: (err) => {
      console.error('Error al reclamar logro:', err);
      this.reclamando = false;
    }
  });
}

  calcularPorcentaje(): number {
    const progreso = this.usuarioLogro.progresoActual;
    const objetivo = this.usuarioLogro.logro.valorObjetivo;
    return Math.min(100, (progreso / objetivo) * 100);
  }
}
