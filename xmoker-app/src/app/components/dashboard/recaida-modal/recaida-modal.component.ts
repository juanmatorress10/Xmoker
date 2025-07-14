import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RecaidaService, Recaida } from '../../../services/recaida.service';

@Component({
  standalone: true,
  selector: 'app-recaida-modal',
  imports: [CommonModule, FormsModule],
  templateUrl: './recaida-modal.component.html',
  styleUrls: ['./recaida-modal.component.css']
})
export class RecaidaModalComponent {
  @Input() userId!: number;

  @Input()
  set motivacionesUsuarioRaw(value: string | string[]) {
    if (Array.isArray(value)) {
      this.motivacionesUsuario = value;
    } else {
      this.motivacionesUsuario = (value || '')
        .split(',')
        .map((m: string) => m.trim())
        .filter((m: string) => m.length > 0);
    }
  }

  motivacionesUsuario: string[] = [];

  recaida: Recaida = {
    fecha: new Date(),
    cantidadFumada: 1,
    motivo: ''
  };

  motivosPredefinidos: string[] = [
    'Ansiedad o estrés',
    'Presión social',
    'Rutina o aburrimiento',
    'Consumo de alcohol',
    'Necesidad física (mono)',
    'Otro'
  ];

  motivoSeleccionado = this.motivosPredefinidos[0];
  motivoLibre = '';

  mostrarConfirmacionLeve = false;
  mostrarMensajeFinal = false;
  mostrarMotivacionesVista = false;
  mensajeFinal = '';

  constructor(private recaidaService: RecaidaService) {}

  enviar() {
    if (this.recaida.cantidadFumada === 1) {
      this.mostrarConfirmacionLeve = true;
      return;
    }
    this.registrarRecaida();
  }

  registrarRecaida() {
    const motivoFinal = this.motivoSeleccionado === 'Otro' ? this.motivoLibre : this.motivoSeleccionado;
    const recaidaFinal: Recaida = { ...this.recaida, motivo: motivoFinal };

    this.recaidaService.registrarRecaida(this.userId, recaidaFinal).subscribe({
      next: () => {
        this.mensajeFinal =
          '✅ Lo estás haciendo bien.\n\nUna recaída no borra tu esfuerzo. Estás aquí, consciente y comprometido. Recuerda tus motivos y sigue adelante 💙';
        this.resetVistas();
        this.mostrarMensajeFinal = true;
      },
      error: () => {
        this.mensajeFinal =
          '❌ Hubo un error al registrar la recaída. Inténtalo de nuevo más tarde.';
        this.resetVistas();
        this.mostrarMensajeFinal = true;
      }
    });
  }

  omitirRegistro() {
    this.mensajeFinal =
      'Has decidido no registrar esta recaída, y está bien.\nUn cigarro no define tu proceso. Seguimos contigo 💪';
    this.resetVistas();
    this.mostrarMensajeFinal = true;
  }

  recordarMotivaciones() {
    this.resetVistas();
    this.mostrarMotivacionesVista = true;
  }

  cerrar() {
    const modal = document.querySelector('.modal-overlay') as HTMLElement;
    modal?.remove();
  }

  private resetVistas() {
    this.mostrarMensajeFinal = false;
    this.mostrarConfirmacionLeve = false;
    this.mostrarMotivacionesVista = false;
  }
}
