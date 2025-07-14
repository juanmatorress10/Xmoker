// src/app/components/reto-card.component.ts

import {
  Component,
  Input,
  Output,
  EventEmitter,
  ChangeDetectionStrategy,
  OnInit
} from '@angular/core';
import { CommonModule } from '@angular/common';
import { ComunidadService } from '../../services/comunidad.service';
import { ToastrService } from 'ngx-toastr';

interface Participante {
  usuarioId: number;
  nombre: string;
  estado: 'COMPLETADO' | 'PENDIENTE' | 'NO_COMPLETADO';
}

interface Reto {
  retoId: number;
  titulo: string;
  descripcion: string;
  fechaInicio: string;
  fechaFin: string;
  creadorNombre?: string;
  participantes: Participante[];
  totalParticipantes: number;
  totalCompletados: number;
  porcentajeCompletado: number;
}

@Component({
  selector: 'app-reto-card',
  standalone: true,
  imports: [CommonModule],
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="bg-white border border-gray-200 rounded-2xl shadow-md p-5 mb-6">
      <!-- Barra de progreso -->
      <div class="w-full bg-gray-200 rounded-full h-2 mb-4">
        <div class="bg-blue-500 h-2 rounded-full" [style.width.%]="reto.porcentajeCompletado"></div>
      </div>

      <ng-container [ngSwitch]="estadoUsuario">

        <!-- COMPLETADO -->
        <div *ngSwitchCase="'COMPLETADO'" class="text-center bg-green-50 border border-green-200 rounded-2xl p-6 shadow-md">
          <h3 class="text-2xl font-bold text-green-700 mb-2">🌟 ¡Felicidades, campeón! 🌟</h3>
          <p class="text-lg text-gray-800 mb-2">Has completado con éxito el reto:</p>
          <h4 class="text-xl font-semibold text-green-900 italic mb-3">"{{ reto.titulo }}"</h4>
          <p class="text-sm text-gray-600 mb-2">Tu compromiso inspira a tus íntimos Xmokers.</p>
          <p class="text-sm text-gray-600 mb-4">
            📅 Finalizado el <strong>{{ reto.fechaFin | date:'longDate' }}</strong>
          </p>
          <div class="flex flex-wrap gap-2 justify-center mt-4">
            <span *ngFor="let p of reto.participantes"
                  class="px-3 py-1 text-sm rounded-full shadow-sm"
                  [ngClass]="{
                    'bg-green-100 text-green-800': p.estado === 'COMPLETADO',
                    'bg-red-100 text-red-800': p.estado === 'NO_COMPLETADO',
                    'bg-yellow-100 text-yellow-800': p.estado === 'PENDIENTE'
                  }">
              {{ p.nombre }} {{ p.estado === 'COMPLETADO' ? '✔️' 
                              : p.estado === 'NO_COMPLETADO'  ? '❌' 
                              : '⌛' }}
            </span>
          </div>
        </div>

        <!-- NO_COMPLETADO -->
        <div *ngSwitchCase="'NO_COMPLETADO'" class="text-center bg-red-50 border border-red-200 rounded-2xl p-6 shadow-md">
          <h3 class="text-2xl font-bold text-red-700 mb-2">😞 ¡Ánimo!</h3>
          <p class="text-lg text-gray-800 mb-2">No lograste completar el reto:</p>
          <h4 class="text-xl font-semibold text-red-900 italic mb-3">"{{ reto.titulo }}"</h4>
          <p class="text-sm text-gray-600 mb-2">Cada intento cuenta. ¡Lo importante es seguir intentándolo!</p>
          <p class="text-sm text-gray-600 mb-4">
            📅 Finalizó el <strong>{{ reto.fechaFin | date:'longDate' }}</strong>
          </p>
        </div>

        <!-- NORMAL (PENDIENTE o no unido) -->
        <div *ngSwitchDefault>
          <h3 class="text-xl font-bold text-blue-600 mb-2">{{ reto.titulo }}</h3>
          <p class="text-sm text-gray-700 mb-2">{{ reto.descripcion }}</p>
          <p class="text-sm text-gray-400 mb-2">
            👤 Creador: {{ reto.creadorNombre || 'Desconocido' }}
          </p>

          <div class="text-sm text-gray-500 mb-3">
            📅 {{ reto.fechaInicio | date }} - {{ reto.fechaFin | date }}
            <div *ngIf="diasRestantes > 0" class="text-xs text-blue-700 mt-1">
              ⏳ Quedan {{ diasRestantes }} día{{ diasRestantes === 1 ? '' : 's' }}
            </div>
            <div *ngIf="diasRestantes === 0" class="text-xs text-red-600 mt-1">
              ⏱ Hoy es el último día
            </div>
          </div>

          <div class="flex items-center justify-between text-sm text-gray-700 mb-3">
            <p>👥 Participantes: {{ reto.totalParticipantes }}</p>
            <p>✅ Completados: {{ reto.totalCompletados }}</p>
            <p>📈 Éxito: {{ reto.porcentajeCompletado }}%</p>
          </div>

          <!-- Lista de participantes -->
          <div class="flex flex-wrap gap-2 mb-3">
            <span *ngFor="let p of reto.participantes"
                  class="px-2 py-1 text-xs rounded-full"
                  [ngClass]="{
                    'bg-green-100 text-green-800': p.estado === 'COMPLETADO',
                    'bg-red-100 text-red-800': p.estado === 'NO_COMPLETADO',
                    'bg-yellow-100 text-yellow-800': p.estado === 'PENDIENTE'
                  }">
              {{ p.nombre }} {{ p.estado === 'COMPLETADO' ? '✔️' 
                              : p.estado === 'NO_COMPLETADO'  ? '❌' 
                              : '⌛' }}
            </span>
          </div>

          <!-- Acciones sólo si el reto no expiró -->
          <div *ngIf="!expirado" class="flex flex-wrap gap-4 mt-4">
            <button *ngIf="!estaUnido"
                    (click)="unirse()"
                    [disabled]="cargandoAccion"
                    class="bg-blue-500 text-white px-4 py-1 rounded-xl hover:bg-blue-600 text-sm transition">
              🤝 Unirse al reto
            </button>

            <button *ngIf="puedeMarcarComoCompletado"
                    (click)="completar()"
                    [disabled]="cargandoAccion"
                    class="bg-green-500 text-white px-4 py-1 rounded-xl hover:bg-green-600 text-sm transition">
              🏁 Marcar como completado
            </button>

            <button *ngIf="puedeMarcarNoCompletado"
                    (click)="noCompletado()"
                    [disabled]="cargandoAccion"
                    class="bg-red-500 text-white px-4 py-1 rounded-xl hover:bg-red-600 text-sm transition">
              ❌ Marcar como no completado
            </button>
          </div>

          <div *ngIf="expirado" class="text-center text-red-600 mt-2">
            📌 Este reto ha vencido y ya no puedes unirte ni marcarlo.
          </div>
        </div>

      </ng-container>
    </div>
  `
})
export class RetoCardComponent implements OnInit {
  @Input() reto!: Reto;
  @Input() currentUserId!: number;
  @Output() onReload = new EventEmitter<void>();

  estaUnido = false;
  estadoUsuario: Participante['estado'] | null = null;
  diasRestantes = 0;
  cargandoAccion = false;

  constructor(
    private comunidadService: ComunidadService,
    private toastr: ToastrService
  ) {}

  ngOnInit() {
    this.evaluarEstadoUsuario();
    this.calcularDiasRestantes();
  }

  private evaluarEstadoUsuario() {
    const p = this.reto.participantes.find(p => p.usuarioId === this.currentUserId);
    this.estaUnido = !!p;
    this.estadoUsuario = p?.estado || null;
  }

  private calcularDiasRestantes() {
    const fin = new Date(this.reto.fechaFin);
    this.diasRestantes = Math.max(Math.ceil((fin.getTime() - Date.now()) / (1000 * 60 * 60 * 24)), 0);
  }

  /** True si la fecha de fin ya pasó */
  get expirado(): boolean {
    return new Date() > new Date(this.reto.fechaFin);
  }

  get puedeMarcarComoCompletado(): boolean {
    return !this.expirado
        && this.estaUnido
        && this.estadoUsuario === 'PENDIENTE';
  }

  get puedeMarcarNoCompletado(): boolean {
    return this.puedeMarcarComoCompletado;
  }

  unirse() {
    this.cargandoAccion = true;
    this.comunidadService.unirseAReto(this.reto.retoId).subscribe({
      next: () => { this.toastr.success('Unido al reto'); this.reloadReto(); },
      error: () => { this.toastr.error('Error al unirte'); this.cargandoAccion = false; }
    });
  }

  completar() {
    this.cargandoAccion = true;
    this.comunidadService.completarReto(this.reto.retoId).subscribe({
      next: () => { this.toastr.success('¡Completado!'); this.reloadReto(); },
      error: () => { this.toastr.error('Error al completar'); this.cargandoAccion = false; }
    });
  }

  noCompletado() {
    this.cargandoAccion = true;
    this.comunidadService.marcarRetoNoCompletado(this.reto.retoId).subscribe({
      next: () => { this.toastr.info('Marcado no completado'); this.reloadReto(); },
      error: () => { this.toastr.error('Error'); this.cargandoAccion = false; }
    });
  }

  private reloadReto() {
    this.comunidadService.getResumenReto(this.reto.retoId).subscribe({
      next: nuevo => {
        this.reto = { ...nuevo };
        this.evaluarEstadoUsuario();
        this.calcularDiasRestantes();
        this.cargandoAccion = false;
        this.onReload.emit();
      },
      error: () => { this.toastr.error('Error al recargar'); this.cargandoAccion = false; }
    });
  }
}
