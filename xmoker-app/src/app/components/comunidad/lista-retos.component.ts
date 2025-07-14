// src/app/components/lista-retos.component.ts

import {
  Component,
  Input,
  OnInit,
  OnChanges,
  SimpleChanges,
  ChangeDetectionStrategy,
  ChangeDetectorRef
} from '@angular/core';
import { CommonModule } from '@angular/common';
import { ComunidadService } from '../../services/comunidad.service';
import { forkJoin, of } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { RetoCardComponent } from './reto-card.component';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';

@Component({
  selector: 'app-lista-retos',
  standalone: true,
  imports: [CommonModule, RetoCardComponent, MatPaginatorModule],
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="pb-32">
      <div *ngIf="cargando" class="text-center text-gray-500">Cargando retos...</div>
      <div *ngIf="!cargando && retos.length === 0" class="text-center text-gray-500">
        Aún no hay retos publicados.
      </div>
      <div *ngIf="!cargando && retos.length > 0">
        <!-- filtros igual que antes -->
        <div class="flex justify-center gap-2 mb-4">
          <button (click)="filtroEstado='TODOS'" [class.font-bold]="filtroEstado==='TODOS'">🌐 Todos</button>
          <button (click)="filtroEstado='COMPLETADO'" [class.font-bold]="filtroEstado==='COMPLETADO'">✅ Completados</button>
          <button (click)="filtroEstado='NO_COMPLETADO'" [class.font-bold]="filtroEstado==='NO_COMPLETADO'">❌ No completados</button>
          <button (click)="filtroEstado='PENDIENTE'" [class.font-bold]="filtroEstado==='PENDIENTE'">⌛ Pendientes</button>
        </div>

        <!-- tarjetas ordenadas del más reciente al más antiguo -->
        <div class="flex flex-wrap justify-center gap-6">
          <app-reto-card
            *ngFor="let reto of paginados; trackBy: trackByRetoId"
            [reto]="reto"
            [currentUserId]="currentUserId"
            (onReload)="cargarRetos()">
          </app-reto-card>
        </div>

        <!-- paginador -->
        <mat-paginator
          [length]="retosFiltrados.length"
          [pageSize]="pageSize"
          [pageIndex]="pageIndex"
          [pageSizeOptions]="[3,5,10]"
          (page)="onPageChange($event)">
        </mat-paginator>
      </div>
    </div>
  `
})
export class ListaRetosComponent implements OnInit, OnChanges {
  @Input() grupoId!: number;
  @Input() currentUserName!: string;

  retos: any[] = [];
  cargando = true;
  currentUserId = Number(JSON.parse(localStorage.getItem('usuario') || '{}').id || 0);

  filtroEstado: 'TODOS'|'COMPLETADO'|'NO_COMPLETADO'|'PENDIENTE' = 'TODOS';
  pageSize = 3;
  pageIndex = 0;

  constructor(
    private comunidadService: ComunidadService,
    private cd: ChangeDetectorRef
  ) {}

  ngOnInit() {
    if (this.grupoId) {
      this.cargarRetos();
    }
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes['grupoId'] && !changes['grupoId'].firstChange) {
      this.cargarRetos();
    }
  }

  get retosFiltrados(): any[] {
    let list = [...this.retos];
    list.sort((a, b) => new Date(b.fechaInicio).getTime() - new Date(a.fechaInicio).getTime());
    if (this.filtroEstado === 'TODOS') return list;
    return list.filter(r =>
      r.participantes.some((p: any) =>
        p.usuarioId === this.currentUserId && p.estado === this.filtroEstado
      )
    );
  }

  get paginados(): any[] {
    const start = this.pageIndex * this.pageSize;
    return this.retosFiltrados.slice(start, start + this.pageSize);
  }

  trackByRetoId(_idx: number, item: any) {
    return item.retoId || item.id;
  }

  cargarRetos() {
    this.cargando = true;
    this.cd.markForCheck();               // le decimos a Angular que hay cambio
    this.comunidadService.getRetosGrupo(this.grupoId).subscribe({
      next: list => {
        if (!list.length) {
          this.retos = [];
          this.cargando = false;
          this.cd.markForCheck();
          return;
        }
        const calls = list.map(r =>
          this.comunidadService.getResumenReto(r.id).pipe(
            catchError(() => of({ ...r, participantes: [], totalParticipantes: 0, totalCompletados: 0, porcentajeCompletado: 0 }))
          )
        );
        forkJoin(calls).subscribe({
          next: res => {
            this.retos = res;
            this.cargando = false;
            this.cd.markForCheck();
          },
          error: () => {
            this.retos = [];
            this.cargando = false;
            this.cd.markForCheck();
          }
        });
      },
      error: () => {
        this.retos = [];
        this.cargando = false;
        this.cd.markForCheck();
      }
    });
  }

  onPageChange(evt: PageEvent) {
    this.pageIndex = evt.pageIndex;
    this.pageSize = evt.pageSize;
    this.cd.markForCheck();
  }
}
