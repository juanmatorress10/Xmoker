// ✅ diario.component.ts modificado para emociones personalizadas y dificultades estructuradas

import { Component, OnInit, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { ChangeDetectorRef } from '@angular/core';
import { DiarioService, DiarioEntrada } from '../../../services/diario.service';
import { NavbarInferiorComponent } from '../../navbar-inferior.component';
import { NavbarSuperiorComponent } from '../../navbar-superior/navbar-superior.component';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { MatPaginatorModule } from '@angular/material/paginator';

@Component({
  selector: 'app-diario',
  standalone: true,
  imports: [CommonModule, FormsModule, NavbarInferiorComponent, MatPaginatorModule,NavbarSuperiorComponent],
  templateUrl: './diario.component.html',
  styleUrls: ['./diario.component.css']
})
export class DiarioComponent implements OnInit {
  usuarioId: number = 0;
  entradas: DiarioEntrada[] = [];
  entradasMostradas: DiarioEntrada[] = [];

  emocionDelDia = '';
  emocionPersonalizada = '';
  estrategiasUsadas: string[] = [];
  dificultadesSeleccionadas: string[] = [];
  otrasDificultades = '';

emocionesDisponibles = [
  { nombre: 'Feliz', descripcion: 'Te sientes lleno de alegría y motivado para seguir libre de humo. 😄' },
  { nombre: 'Triste', descripcion: 'Un día difícil, pero cada paso importa en tu proceso. 💙' },
  { nombre: 'Estresado', descripcion: 'La presión no define tu éxito, tú controlas tu camino. 😰' },
  { nombre: 'Relajado', descripcion: 'La calma es una gran aliada en tu progreso. 😌' },
  { nombre: 'Motivado', descripcion: 'Estás inspirado para seguir adelante y construir tu mejor versión. 🚀' },
  { nombre: 'Ansioso', descripcion: 'Es normal sentir ansiedad; cada emoción la enfrentas con valentía. 💪' },
  { nombre: 'Cansado', descripcion: 'Un día de baja energía. Descansar también es avanzar. 😴' },
  { nombre: 'Frustrado', descripcion: 'Sientes resistencia o bloqueo, pero estás en el camino. 🧱' },
  { nombre: 'Orgulloso', descripcion: '¡Reconoces tu progreso y te sientes fuerte! 🏆' },
  { nombre: 'Ninguna', descripcion: 'No sentiste una emoción destacada hoy. 🙃' }
];

  dificultadesDisponibles = [
    'Ansiedad',
    'Antojo de fumar',
    'Ambiente social desfavorable',
    'Estrés laboral',
    'Tristeza o soledad',
    'Falta de motivación',
    'Problemas de sueño',
    'Discusión con alguien',
    'Rutina monótona',
    'Síntomas físicos (dolor, fatiga)',
    'Recuerdo asociado al tabaco',
    'Falta de apoyo externo',
    'Presión social',
    'Ninguna'
  ];

estrategiasDisponibles = [
  { nombre: 'Deporte', descripcion: 'Hiciste actividad física para liberar tensiones. 🏃‍♂️' },
  { nombre: 'Meditación', descripcion: 'Practicaste técnicas de relajación y mindfulness. 🧘‍♂️' },
  { nombre: 'Hablar con alguien', descripcion: 'Compartiste tus sensaciones con alguien de confianza. 🗣️' },
  { nombre: 'Distracción activa', descripcion: 'Te ocupaste con una actividad positiva o creativa. 🎨' },
  { nombre: 'Lectura motivadora', descripcion: 'Leíste algo que reforzó tu motivación. 📚' },
  { nombre: 'Escuchar música', descripcion: 'La música te ayudó a calmarte o motivarte. 🎵' },
  { nombre: 'Respiración consciente', descripcion: 'Hiciste respiraciones profundas para calmarte. 🌬️' },
  { nombre: 'Hidratación o snack saludable', descripcion: 'Bebiste agua o comiste algo para cortar el antojo. 🥤' },
  { nombre: 'Contactar con tu motivo', descripcion: 'Recordaste tu propósito para dejar de fumar. 💭' },
  { nombre: 'Ninguna', descripcion: 'No aplicaste ninguna estrategia hoy. Está bien. Mañana será otro día. 🤍' }
];

  isAdding = false;
  isEditing = false;
  formularioEnviado: boolean = false;
  entradaEditando?: DiarioEntrada;

  isLoading = true;
  errorMsg = '';

  ordenEntradas: 'recientes' | 'antiguas' = 'recientes';
  pageSize = 4;
  pageIndex = 0;
  length = 0;

  @ViewChild(MatPaginator) paginator!: MatPaginator;

  modalInfoAbierto = false;
  modalInfoCerrando = false;

  constructor(
    private diarioService: DiarioService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit() {
    const usuarioGuardado = localStorage.getItem('usuario');
    if (usuarioGuardado) {
      const usuario = JSON.parse(usuarioGuardado);
      this.usuarioId = usuario.id;
      this.cargarEntradas();
    } else {
      this.isLoading = false;
      this.errorMsg = 'No hay información de usuario disponible.';
    }
  }

  cargarEntradas() {
    this.isLoading = true;
    this.diarioService.getEntradas(this.usuarioId).subscribe({
      next: (data) => {
        this.entradas = data;
        this.length = data.length;
        this.ordenarEntradas();
        this.actualizarEntradasMostradas();
        this.isLoading = false;
      },
      error: (err) => {
        this.errorMsg = 'No se pudieron cargar tus entradas.';
        this.isLoading = false;
      }
    });
  }

ordenarEntradas() {
  if (this.ordenEntradas === 'recientes') {
    this.entradas.sort((a, b) => new Date(b.fechaCreacion!).getTime() - new Date(a.fechaCreacion!).getTime());
  } else {
    this.entradas.sort((a, b) => new Date(a.fechaCreacion!).getTime() - new Date(b.fechaCreacion!).getTime());
  }

  // 👇 ACTUALIZA LA VISTA TRAS ORDENAR
  this.pageIndex = 0; // opcional: vuelve a la primera página
  this.actualizarEntradasMostradas();
}

  actualizarEntradasMostradas() {
    const inicio = this.pageIndex * this.pageSize;
    const fin = inicio + this.pageSize;
    this.entradasMostradas = this.entradas.slice(inicio, fin);
    this.cdr.detectChanges();
  }

  onPageChange(event: PageEvent) {
    this.pageIndex = event.pageIndex;
    this.pageSize = event.pageSize;
    this.actualizarEntradasMostradas();
  }

  enviarEntrada() {
    this.formularioEnviado = true;
    const emocion = this.emocionDelDia === 'Otra' ? this.emocionPersonalizada.trim() : this.emocionDelDia;
    const dificultades = [...this.dificultadesSeleccionadas];
    if (this.otrasDificultades.trim()) dificultades.push(this.otrasDificultades.trim());

    if (!emocion || this.estrategiasUsadas.length === 0 || dificultades.length === 0) {
      alert('Por favor completa todos los campos para registrar tu día.');
      return;
    }

    const nuevaEntrada: DiarioEntrada = {
      usuarioId: this.usuarioId,
      emocionDelDia: emocion,
      estrategiasUsadas: this.estrategiasUsadas.join(', '),
      complicacionesEncontradas: dificultades.join(', ')
    };

    if (this.isEditing && this.entradaEditando) {
      nuevaEntrada.id = this.entradaEditando.id;
      this.diarioService.eliminarEntrada(this.entradaEditando.id!).subscribe({
        next: () => {
          this.diarioService.crearEntrada(this.usuarioId, nuevaEntrada).subscribe(() => {
            this.resetFormulario();
            this.cargarEntradas();
            this.isAdding = false;
            this.isEditing = false;
          });
        }
      });
    } else {
      this.diarioService.crearEntrada(this.usuarioId, nuevaEntrada).subscribe({
        next: () => {
          this.resetFormulario();
          this.cargarEntradas();
          this.isAdding = false;
        }
      });
    }
  }

  eliminarEntrada(id: number) {
    if (confirm('¿Seguro que quieres eliminar esta entrada?')) {
      this.diarioService.eliminarEntrada(id).subscribe(() => {
        this.cargarEntradas();
      });
    }
  }

  editarEntrada(entrada: DiarioEntrada) {
    this.isAdding = true;
    this.isEditing = true;
    this.entradaEditando = entrada;
    this.emocionDelDia = this.emocionesDisponibles.find(e => e.nombre === entrada.emocionDelDia) ? entrada.emocionDelDia : 'Otra';
    this.emocionPersonalizada = this.emocionDelDia === 'Otra' ? entrada.emocionDelDia : '';
    this.estrategiasUsadas = entrada.estrategiasUsadas.split(', ').filter(e => e.trim() !== '');
    this.dificultadesSeleccionadas = entrada.complicacionesEncontradas.split(', ').filter(d => this.dificultadesDisponibles.includes(d));
    this.otrasDificultades = entrada.complicacionesEncontradas.split(', ').filter(d => !this.dificultadesDisponibles.includes(d)).join(', ');
  }

  mostrarFormulario() {
    this.isAdding = true;
    this.isEditing = false;
    this.resetFormulario();
  }

  cancelarFormulario() {
    this.isAdding = false;
    this.isEditing = false;
    this.resetFormulario();
  }

  resetFormulario() {
    this.emocionDelDia = '';
    this.emocionPersonalizada = '';
    this.estrategiasUsadas = [];
    this.dificultadesSeleccionadas = [];
    this.otrasDificultades = '';
    this.entradaEditando = undefined;
  }
  
  campoInvalido(valor: string): boolean {
    return valor.trim().length === 0;
  }

  toggleEstrategia(nombre: string) {
    const index = this.estrategiasUsadas.indexOf(nombre);
    if (index >= 0) this.estrategiasUsadas.splice(index, 1);
    else this.estrategiasUsadas.push(nombre);
  }

  toggleDificultad(nombre: string) {
    const index = this.dificultadesSeleccionadas.indexOf(nombre);
    if (index >= 0) this.dificultadesSeleccionadas.splice(index, 1);
    else this.dificultadesSeleccionadas.push(nombre);
  }

  obtenerEmojiEmocion(emocion: string): string {
    const texto = emocion.toLowerCase();
    if (texto.includes('feliz')) return '😄';
    if (texto.includes('triste')) return '😢';
    if (texto.includes('estresado')) return '😰';
    if (texto.includes('relajado')) return '😌';
    if (texto.includes('motivado')) return '🚀';
    if (texto.includes('ansioso')) return '😥';
    return '📝';
  }

  obtenerEmojiEstrategia(estrategia: string): string {
    const texto = estrategia.toLowerCase();
    if (texto.includes('deporte')) return '🏃‍♂️';
    if (texto.includes('meditación')) return '🧘‍♂️';
    if (texto.includes('motivación')) return '📚';
    if (texto.includes('amistad')) return '👨‍👩‍👧‍👦';
    return '🛡️';
  }

  mostrarInfoDiario() {
    this.modalInfoAbierto = true;
  }

  cerrarInfoDiario() {
    this.modalInfoCerrando = true;
    setTimeout(() => {
      this.modalInfoAbierto = false;
      this.modalInfoCerrando = false;
    }, 300);
  }

  entradasEsteMes() {
    const mesActual = new Date().getMonth();
    return this.entradas.filter(e => new Date(e.fechaCreacion!).getMonth() === mesActual).length;
  }

  entradasEstaSemana() {
    const hoy = new Date();
    return this.entradas.filter(e => {
      const fecha = new Date(e.fechaCreacion!);
      const diferencia = (hoy.getTime() - fecha.getTime()) / (1000 * 60 * 60 * 24);
      return diferencia <= 7;
    }).length;
  }

  irEstadisticasDiario() {
    this.router.navigate(['/dashboard/estadisticas-diario']);
  }

  trackById(index: number, item: DiarioEntrada) {
    return item.id;
  }
}
