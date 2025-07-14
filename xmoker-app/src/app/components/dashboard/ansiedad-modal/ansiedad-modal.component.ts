import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ComponentRef } from '@angular/core';
import { EscenaRelajanteComponent } from './escena-relajante/escena-relajante.component';
import { LogrosService } from '../../../services/logros.service';

@Component({
  selector: 'app-ansiedad-modal',
  standalone: true,
  imports: [CommonModule, EscenaRelajanteComponent],
  templateUrl: './ansiedad-modal.component.html',
  styleUrls: ['./ansiedad-modal.component.css']
})




export class AnsiedadModalComponent {
  @Input() userId: number = 0;
  @Input() motivacionesUsuario: string[] = [];
  constructor(private logrosService: LogrosService) {}

  selfRef!: ComponentRef<any>;

  seccionActiva: string = 'inicio';
  experienciaGanada = 0;

  claseDeFondo: string = 'fondo-neutro';

  // ---------------- RESPIRACIÓN ----------------
  mensajeRespiracion: string = '';
  estadoAnimacion: string = '';
  enRespiracion: boolean = false;
  respiracionFinalizada: boolean = false;
  ciclosRespiracion: number = 0;

  iniciarRespiracion() {
    this.enRespiracion = true;
    this.respiracionFinalizada = false;
    this.ciclosRespiracion = 0;
    this.empezarCiclo();
  }

  empezarCiclo() {
    if (this.ciclosRespiracion >= 3) {
      this.estadoAnimacion = '';
      this.mensajeRespiracion = '🌟 ¡Has completado 3 ciclos! Vuelve aquí cuando necesites reconectar contigo.';
      this.enRespiracion = false;
      this.respiracionFinalizada = true;
      return;
    }

    this.estadoAnimacion = 'expandir';
    this.mensajeRespiracion = 'Inhala profundo… (4s)';
    setTimeout(() => {
      this.estadoAnimacion = 'retener';
      this.mensajeRespiracion = 'Retén el aire… (7s)';
      setTimeout(() => {
        this.estadoAnimacion = 'contraer';
        this.mensajeRespiracion = 'Exhala lentamente… (8s)';
        setTimeout(() => {
          this.ciclosRespiracion++;
          this.empezarCiclo();
        }, 8000);
      }, 7000);
    }, 4000);
  }

  // ---------------- FONDOS Y NAVEGACIÓN ----------------
  cambiarSeccion(seccion: string) {
    this.seccionActiva = seccion;

    switch (seccion) {
      case 'sonido-mar':
        this.claseDeFondo = 'fondo-mar';
        break;
      case 'sonido-lluvia':
        this.claseDeFondo = 'fondo-lluvia';
        break;
      case 'sonido-bosque':
        this.claseDeFondo = 'fondo-bosque';
        break;
      case 'juego-emojis':
        this.generarEmojis();
        break;
      case 'juego-stroop':
        this.generarStroop();
        break;
      case 'juego-memoria':
        this.generarMemoria();
        break;
      default:
        this.claseDeFondo = 'fondo-neutro';
    }
  }

  cerrar() {
    this.selfRef?.destroy();
    document.querySelector('.modal-host')?.remove();
  }

  registrarAccion(tipo: string) {
      if (this.userId) {
        this.logrosService.registrarRetoAnsiedad(this.userId).subscribe({
          next: () => {
            console.log(`✅ Acción de ansiedad registrada: ${tipo}`);
          },
          error: (err) => {
            console.error('❌ Error al registrar reto de ansiedad', err);
          }
        });
      }

      this.cerrar();
    }

  registrarSonidoMar() {
    this.registrarAccion('sonido-mar');
  }

  registrarSonidoLluvia() {
    this.registrarAccion('sonido-lluvia');
  }

  registrarSonidoBosque() {
    this.registrarAccion('sonido-bosque');
  }

  // ---------------- JUEGO 1: CUENTA REGRESIVA ----------------
  contador: number = 100;
  enProgreso: boolean = false;
  terminado: boolean = false;
  intervalCuenta: any;

  iniciarCuentaRegresiva() {
    this.contador = 100;
    this.enProgreso = true;
    this.terminado = false;
    this.intervalCuenta = setInterval(() => {
      this.contador -= 7;
      if (this.contador <= 0) {
        this.detenerCuentaRegresiva();
        this.terminado = true;
      }
    }, 700);
  }

  detenerCuentaRegresiva() {
    clearInterval(this.intervalCuenta);
    this.enProgreso = false;
  }

// ---------------- JUEGO 2: EMOJI DIFERENTE ----------------
nivelEmoji: number = 1;
emojis: string[] = [];
respuestaEmoji: boolean | null = null;
emojiSetIndex: number = 0;

emojiSets: { base: string; distinto: string }[] = [
  { base: '😀', distinto: '😎' },       // caras básicas
  { base: '🐶', distinto: '🐱' },       // perro vs gato
  { base: '🍎', distinto: '🍌' },       // manzana vs plátano
  { base: '🚗', distinto: '✈️' },       // coche vs avión
  { base: '⚽', distinto: '🏀' },       // fútbol vs baloncesto
  { base: '💡', distinto: '🔦' },       // bombilla vs linterna
  { base: '🌞', distinto: '🌜' },       // sol vs luna
  { base: '🎵', distinto: '🎶' },       // nota vs notas
  { base: '🍕', distinto: '🍔' },       // pizza vs hamburguesa
  { base: '🎲', distinto: '🎯' },       // dado vs diana
  { base: '🧊', distinto: '🔥' },       // hielo vs fuego
  { base: '🌈', distinto: '☁️' },       // arcoíris vs nube
  { base: '🔒', distinto: '🔓' },       // candado cerrado vs abierto
  { base: '🪐', distinto: '🌍' },       // planeta vs Tierra
  { base: '📱', distinto: '💻' },       // móvil vs portátil
];

generarEmojis() {
  // Selecciona nuevo set al comenzar un nuevo ciclo o mantener fijo por juego
  if (this.nivelEmoji === 1) {
    this.emojiSetIndex = Math.floor(Math.random() * this.emojiSets.length);
  }

  const set = this.emojiSets[this.emojiSetIndex];
  const cantidad = 6 + this.nivelEmoji * 2;

  this.emojis = Array(cantidad).fill(set.base);
  const pos = Math.floor(Math.random() * cantidad);
  this.emojis[pos] = set.distinto;

  this.respuestaEmoji = null;
}

seleccionarEmoji(index: number) {
  this.respuestaEmoji = this.emojis[index] !== this.emojiSets[this.emojiSetIndex].base;
}

resetEmoji() {
  if (this.respuestaEmoji === true) {
    this.nivelEmoji++;
  } else if (this.respuestaEmoji === false && this.nivelEmoji > 1) {
    this.nivelEmoji--;
  }

  let nuevoIndex;
  do {
    nuevoIndex = Math.floor(Math.random() * this.emojiSets.length);
  } while (nuevoIndex === this.emojiSetIndex); // evita repetir

  this.emojiSetIndex = nuevoIndex;
  this.generarEmojis();
}

  // ---------------- JUEGO 3: STROOP ----------------
palabraActual: string = '';
colorActual: string = '';
resultadoStroop: boolean | null = null;
nivelStroop: number = 1;

palabras = ['Rojo', 'Azul', 'Verde', 'Amarillo', 'Morado', 'Rosa'];
colores = ['red', 'blue', 'green', 'yellow', 'purple', 'deeppink'];

generarStroop() {
  const rango = Math.min(this.nivelStroop + 2, this.colores.length); // nivel 1 = 3 colores
  const color = this.colores[Math.floor(Math.random() * rango)];
  const palabra = this.palabras[Math.floor(Math.random() * rango)];
  this.colorActual = color;
  this.palabraActual = palabra;
  this.resultadoStroop = null;
}

seleccionarColor(colorSeleccionado: string) {
  this.resultadoStroop = colorSeleccionado === this.colorActual;
}

continuarStroop() {
  if (this.resultadoStroop === true && this.nivelStroop < 4) {
    this.nivelStroop++;
  } else if (this.resultadoStroop === false && this.nivelStroop > 1) {
    this.nivelStroop--;
  }
  this.generarStroop();
}


  // ---------------- JUEGO 4: MEMORIA ----------------
  emojisMemoria: string[] = [];
  opcionesMemoria: string[] = [];
  mostrarOpciones: boolean = false;
  resultadoMemoria: boolean | null = null;
  tiempoRestanteMemoria: number = 4;
  temporizadorMemoria: any;

generarMemoria() {
  const banco = ['🍎', '🚗', '🐶', '🌙', '📚', '🎵', '⚽', '💡', '🏖️'];
  const usados = new Set<string>();
  this.emojisMemoria = [];

  while (this.emojisMemoria.length < 5) {
    const e = banco[Math.floor(Math.random() * banco.length)];
    if (!usados.has(e)) {
      usados.add(e);
      this.emojisMemoria.push(e);
    }
  }

  this.mostrarOpciones = false;
  this.resultadoMemoria = null;
  this.tiempoRestanteMemoria = 4;

  this.temporizadorMemoria = setInterval(() => {
    this.tiempoRestanteMemoria--;
    if (this.tiempoRestanteMemoria <= 0) {
      clearInterval(this.temporizadorMemoria);

      // cuando termina, genera opciones
      const noEstaba = banco.find(e => !usados.has(e))!;
      this.opcionesMemoria = [...this.emojisMemoria];
      const insertIndex = Math.floor(Math.random() * 3);
      this.opcionesMemoria.splice(insertIndex, 0, noEstaba);
      this.mostrarOpciones = true;
    }
  }, 1000);
}

  seleccionarMemoria(emoji: string) {
    this.resultadoMemoria = !this.emojisMemoria.includes(emoji);
  }
}
