// src/app/components/comunidad/estadisticas-grupo.component.ts

import { Component, Input, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { catchError, of } from 'rxjs';
import { ComunidadService } from '../../services/comunidad.service';

export interface EstadisticasGrupo {
  grupoId: number;
  totalMiembros: number;
  totalDiasSinFumar: number;
  totalPublicaciones: number;
  totalRetos: number;
  retosActivos: number;
  mediaRachaActual: number;
  mediaRachaMaxima: number;
}

export interface ComparativaGrupo {
  diferenciaDias: number;
  diferenciaDinero: number;
}

@Component({
  standalone: true,
  selector: 'app-estadisticas-grupo',
  imports: [
    CommonModule
  ],
  template: `
    <section class="w-full px-6 py-10 bg-gradient-to-b from-white to-blue-100">
      <div class="mx-auto w-full max-w-6xl">

        <!-- Header -->
        <div class="flex items-center mb-8">
          <h1 class="text-4xl font-extrabold text-purple-700 flex items-center gap-2">
            <span>📊</span> Estadísticas XMoker
          </h1>
          <button
            class="ml-auto btn btn-outline-info btn-sm"
            (click)="openPageInfoModal()"
          >
            ℹ️ ¿Qué es esto?
          </button>
        </div>

        <!-- Miembros del grupo -->
        <div *ngIf="miembrosGrupo.length" class="mb-8">
          <h2 class="text-xl font-semibold text-gray-800 mb-2">👥 Miembros del grupo:</h2>
          <ul class="list-disc list-inside text-gray-700 space-y-1">
            <li *ngFor="let m of miembrosGrupo">{{ m.nombre }}</li>
          </ul>
        </div>

        <!-- Tarjetas: siempre 1 por fila -->
        <div *ngIf="stats; else noStats"
             class="grid grid-cols-1 gap-8 justify-items-center">
          <div *ngFor="let card of tarjetas"
               class="w-full max-w-sm bg-white rounded-2xl shadow-lg p-6 cursor-pointer hover:shadow-xl transition"
               (click)="abrirInfo(card.label)">
            <div class="flex items-center justify-between mb-4">
              <span class="text-3xl">{{ card.icon }}</span>
              <span class="text-2xl font-semibold">{{ card.value }}</span>
            </div>
            <div class="flex items-center mb-3">
              <p class="text-sm text-gray-500 flex-1">{{ card.label }}</p>
              <button aria-label="Info" class="text-gray-400 hover:text-gray-600">ℹ️</button>
            </div>
            <div class="h-2 bg-gray-200 rounded-full overflow-hidden">
              <div class="h-full" [ngClass]="card.barColor" [style.width.%]="card.barPct"></div>
            </div>
          </div>
        </div>
        <ng-template #noStats>
          <p class="text-center text-gray-500 italic mt-12">
            Aún no hay estadísticas disponibles.
          </p>
        </ng-template>

        <!-- Comparativa global -->
        <div *ngIf="comparativa"
             class="bg-purple-50 p-6 rounded-2xl shadow-inner mt-10">
          <h2 class="text-2xl font-semibold text-purple-700 mb-4 flex items-center gap-2">
            <span>📈</span> Comparativa global
          </h2>
          <div class="space-y-4 text-gray-800">
            <div class="flex justify-between">
              <span class="underline cursor-pointer" (click)="abrirInfo('Días vs. media')">
                Días vs. media:
              </span>
              <strong>{{ comparativa.diferenciaDias | number:'1.0-0' }} días</strong>
            </div>
            <div class="flex justify-between">
              <span class="underline cursor-pointer" (click)="abrirInfo('€ vs. media')">
                € vs. media:
              </span>
              <strong>{{ comparativa.diferenciaDinero | currency:'EUR':'symbol' }}</strong>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- Modal de detalle -->
    <div *ngIf="modalAbierto"
         class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div class="bg-white rounded-2xl p-6 max-w-md w-full animate-fade-in relative">
        <h3 class="text-xl font-bold mb-4">ℹ️ {{ descripcionTitulo }}</h3>
        <p class="text-gray-800">{{ descripcionTexto }}</p>
        <button class="absolute top-4 right-4 text-gray-500 hover:text-gray-700"
                (click)="cerrarModal()">✖️</button>
      </div>
    </div>

    <!-- Modal información general -->
    <div *ngIf="showPageInfoModal"
         class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-40">
      <div class="bg-white rounded-2xl p-6 max-w-lg w-full animate-fade-in relative">
        <h3 class="text-2xl font-semibold mb-4">ℹ️ ¿Qué muestran estas métricas?</h3>
        <ul class="list-disc list-inside text-gray-700 space-y-2">
          <li><strong>Porcentaje de recuperación:</strong> Pulmonar, sistema inmune, sueño, estrés.</li>
          <li><strong>Días sin fumar:</strong> Media de días que llevan sin fumar los miembros.</li>
          <li><strong>€ ahorrados:</strong> Estimación de ahorro basado en la racha.</li>
          <li><strong>Retos activos:</strong> Retos cuya fecha de fin es posterior a hoy.</li>
          <li><strong>Días vs. media / € vs. media:</strong> Diferencia respecto a la media global.</li>
        </ul>
        <button class="btn btn-primary mt-6" (click)="closePageInfoModal()">Cerrar</button>
      </div>
    </div>
  `,
  styles: [`
    :host { display: block; }
    .animate-fade-in { animation: fadeIn 0.3s ease-out; }
    @keyframes fadeIn {
      from { opacity: 0; transform: scale(0.95); }
      to   { opacity: 1; transform: scale(1); }
    }
    .bg-purple-500, .bg-green-500, .bg-blue-500, .bg-yellow-500 {
      transition: width 0.8s ease-in-out;
    }
  `]
})
export class EstadisticasGrupoComponent implements OnInit {
  @Input() grupoId!: number;
  stats!: EstadisticasGrupo;
  comparativa!: ComparativaGrupo;
  miembrosGrupo: { nombre: string }[] = [];

  modalAbierto = false;
  descripcionTitulo = '';
  descripcionTexto = '';
  showPageInfoModal = false;

  readonly COSTO_POR_DIA = 5;

  tarjetas!: {
    icon: string;
    label: string;
    value: string | number;
    barPct: number;
    barColor: string;
  }[];

  constructor(private comunidadService: ComunidadService) {}

  ngOnInit() {
    this.comunidadService.getEstadisticasGrupo(this.grupoId)
      .pipe(catchError(() => of(null)))
      .subscribe(res => {
        if (res) {
          this.stats = res;
          this.initTarjetas();
        }
      });

    this.comunidadService.getComparativaGrupo(this.grupoId)
      .pipe(catchError(() => of({ diferenciaDias: 0, diferenciaDinero: 0 })))
      .subscribe(res => this.comparativa = res);

    this.comunidadService.getMiembrosGrupo(this.grupoId)
      .pipe(catchError(() => of([])))
      .subscribe(list => this.miembrosGrupo = list);
  }

  private initTarjetas() {
    const md = this.stats.totalMiembros || 1;
    const mediaDias = this.stats.totalDiasSinFumar / md;
    const mediaDinero = mediaDias * this.COSTO_POR_DIA;
    const maxDias = 60, maxDinero = 300, maxMiembros = 50;

    this.tarjetas = [
      {
        icon: '🚭',
        label: 'Días sin fumar',
        value: Math.round(mediaDias),
        barPct: Math.min((mediaDias / maxDias) * 100, 100),
        barColor: 'bg-purple-500'
      },
      {
        icon: '💰',
        label: '€ ahorrados',
        value: mediaDinero.toFixed(2),
        barPct: Math.min((mediaDinero / maxDinero) * 100, 100),
        barColor: 'bg-green-500'
      },
      {
        icon: '👥',
        label: 'Miembros',
        value: this.stats.totalMiembros,
        barPct: Math.min((this.stats.totalMiembros / maxMiembros) * 100, 100),
        barColor: 'bg-blue-500'
      },
      {
        icon: '🏆',
        label: 'Retos activos',
        value: this.stats.retosActivos,
        barPct: this.stats.totalRetos
          ? Math.min((this.stats.retosActivos / this.stats.totalRetos) * 100, 100)
          : 0,
        barColor: 'bg-yellow-500'
      }
    ];
  }

  abrirInfo(label: string) {
    const infoMap: Record<string,string> = {
      'Días sin fumar': 'Media de días sin fumar de los miembros con progreso registrado.',
      '€ ahorrados': 'Estimación del dinero ahorrado basada en los días sin fumar.',
      'Miembros': 'Número de miembros activos del grupo con progreso registrado.',
      'Retos activos': 'Cantidad de retos cuya fecha de fin es posterior a hoy.',
      'Días vs. media': 'Diferencia de tu media de días sin fumar respecto a la media global.',
      '€ vs. media': 'Diferencia de tu ahorro medio respecto al ahorro medio global.'
    };
    this.descripcionTitulo = label;
    this.descripcionTexto = infoMap[label] || 'Información no disponible.';
    this.modalAbierto = true;
  }

  cerrarModal() {
    this.modalAbierto = false;
  }

  openPageInfoModal() {
    this.showPageInfoModal = true;
  }
  closePageInfoModal() {
    this.showPageInfoModal = false;
  }
}
