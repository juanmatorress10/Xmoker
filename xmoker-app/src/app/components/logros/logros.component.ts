import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { UsuarioLogro } from '../../models/logros.model';
import { LogrosService } from '../../services/logros.service';
import { LogroCardComponent } from './logro-card/logro-card.component';
import { NavbarInferiorComponent } from '../navbar-inferior.component';
import { NavbarSuperiorComponent } from '../navbar-superior/navbar-superior.component';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-logros',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    LogroCardComponent,
    NavbarInferiorComponent,
    NavbarSuperiorComponent
  ],
  templateUrl: './logros.component.html',
  styleUrls: ['./logros.component.css']
})
export class LogrosComponent implements OnInit {
  logrosCompletados: UsuarioLogro[] = [];
  logrosDisponibles: UsuarioLogro[] = [];
  logrosEnProgreso: UsuarioLogro[] = [];

  categorias: string[] = [];
  categoriaSeleccionada = 'Todos';

  usuarioId: number | null = null;
  nombreUsuario = '';
  nivel = 0;
  experiencia = 0;
  experienciaMax = 0;
  porcentajeNivel = 0;

  seccionesVisibles = {
    completados: true,
    disponibles: true,
    enProgreso: true
  };

  // Control del modal de Info
  showInfoModal = false;

  constructor(
    private logrosService: LogrosService,
    private authService: AuthService
  ) {}

  ngOnInit() {
    this.authService.obtenerUsuarioActual().subscribe({
      next: usuario => {
        this.usuarioId = usuario.id;
        this.nombreUsuario = usuario.nombre;
        this.nivel = usuario.nivel || 0;
        this.experiencia = usuario.experiencia || 0;

        this.experienciaMax = (this.nivel + 1) * 1000;
        this.porcentajeNivel = Math.min(100, (this.experiencia / this.experienciaMax) * 100);

        this.cargarLogros();
      },
      error: err => {
        console.error('❌ No se pudo obtener el usuario desde el backend', err);
      }
    });
  }

  cargarLogros() {
    if (!this.usuarioId) return;
    this.logrosService.getLogrosPorUsuario(this.usuarioId).subscribe(data => {
      this.categorias = Array.from(new Set(data.map(l => l.logro.categoria))).sort();
      this.categorias.unshift('Todos');

      this.logrosCompletados = data.filter(l => l.completado && l.reclamado);
      this.logrosDisponibles = data.filter(l => l.completado && !l.reclamado);
      this.logrosEnProgreso = data.filter(l => !l.completado);
    });
  }

  toggle(seccion: keyof typeof this.seccionesVisibles) {
    this.seccionesVisibles[seccion] = !this.seccionesVisibles[seccion];
  }

  filtrarPorCategoria(logros: UsuarioLogro[]): UsuarioLogro[] {
    if (this.categoriaSeleccionada === 'Todos') return logros;
    return logros.filter(l => l.logro.categoria === this.categoriaSeleccionada);
  }

  recargarLogros() {
    this.cargarLogros();
  }

  // Métodos para el modal Info
  openInfoModal() {
    this.showInfoModal = true;
  }

  closeInfoModal() {
    this.showInfoModal = false;
  }
}
