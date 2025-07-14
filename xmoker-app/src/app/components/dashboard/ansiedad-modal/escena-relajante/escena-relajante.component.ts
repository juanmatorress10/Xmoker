import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-escena-relajante',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './escena-relajante.component.html',
  styleUrls: ['./escena-relajante.component.css']
})
export class EscenaRelajanteComponent {
  @Input() titulo: string = '';
  @Input() textoGuiado: string[] = [];
  @Input() audioSrc: string = '';
  @Input() tipoRegistro: string = '';
  @Input() onRegistrar!: () => void;

  audioRef!: HTMLAudioElement;

  fraseActual = 0;
  mostrarTexto = false;
  intervalId: any;

  get frase(): string {
    return this.textoGuiado[this.fraseActual] || '';
  }

  avanzar() {
    if (this.fraseActual < this.textoGuiado.length - 1) {
      this.fraseActual++;
      this.mostrarTexto = false;
      setTimeout(() => this.mostrarTexto = true, 100); // Para activar animación
    }
  }

  iniciarNarrativa() {
    this.mostrarTexto = true;
    // Si deseas automatizar cada frase con 5s:
    // this.intervalId = setInterval(() => this.avanzar(), 5000);
  }

  pausarAudio() {
    if (this.audioRef) {
      this.audioRef.pause();
    }
    clearInterval(this.intervalId);
  }

  registrarYSalir() {
    this.pausarAudio();
    this.onRegistrar();
  }

  guardarReferencia(audio: HTMLAudioElement) {
    this.audioRef = audio;
  }
}
