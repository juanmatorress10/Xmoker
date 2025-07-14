import { Component, OnInit, AfterViewInit, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ChartData, ChartOptions } from 'chart.js';
import { BaseChartDirective } from 'ng2-charts';
import { NavbarInferiorComponent } from '../../navbar-inferior.component';
import { NavbarSuperiorComponent } from '../../navbar-superior/navbar-superior.component';
import { DiarioService, DiarioEntrada } from '../../../services/diario.service';
import { MatTabsModule, MatTabChangeEvent } from '@angular/material/tabs';
import { ChangeDetectorRef } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-estadisticas-diario',
  standalone: true,
  imports: [
    CommonModule,
    BaseChartDirective,
    NavbarInferiorComponent,
    MatTabsModule,
    NavbarSuperiorComponent
  ],
  templateUrl: './estadisticas-diario.component.html',
  styleUrls: ['./estadisticas-diario.component.css']
})
export class EstadisticasDiarioComponent implements OnInit, AfterViewInit {
  usuarioId: number = 0;
  entradas: DiarioEntrada[] = [];

  @ViewChild('emocionesChart') emocionesChart?: BaseChartDirective;
  @ViewChild('estrategiasChart') estrategiasChart?: BaseChartDirective;
  @ViewChild('complicacionesChart') complicacionesChart?: BaseChartDirective;

  chartDataEmociones: ChartData<'bar'> = {
    labels: [],
    datasets: [{ label: 'Cantidad de veces', data: [], backgroundColor: [] }]
  };

  chartDataEstrategias: ChartData<'pie'> = {
    labels: [],
    datasets: [{ data: [], backgroundColor: [] }]
  };

  chartDataComplicaciones: ChartData<'bar'> = {
    labels: [],
    datasets: [{ label: 'Repetición de complicaciones', data: [], backgroundColor: [] }]
  };

  chartOptions: ChartOptions = {
    responsive: true,
    maintainAspectRatio: false,
    indexAxis: 'y',
    scales: {
      x: {
        beginAtZero: true
      }
    }
  };

  modalInfoAbierto = false;
  modalInfoCerrando = false;

  constructor(
    private diarioService: DiarioService,
    private cdr: ChangeDetectorRef,
    private router: Router,
  ) {}

  ngOnInit() {
    const usuarioGuardado = localStorage.getItem('usuario');
    if (usuarioGuardado) {
      const usuario = JSON.parse(usuarioGuardado);
      this.usuarioId = usuario.id;
      this.cargarEntradas();
    }
  }

ngAfterViewInit() {
  setTimeout(() => {
    if (this.emocionesChart?.chart) {
      this.emocionesChart.chart.resize();  // fuerza a redimensionar
      this.emocionesChart.chart.render();  // fuerza a redibujar
    }
  }, 300);
}

  cargarEntradas() {
    this.diarioService.getEntradas(this.usuarioId).subscribe({
      next: (data) => {
        this.entradas = data;
        this.generarEstadisticas();
      },
      error: (err) => console.error('Error cargando entradas del diario:', err)
    });
  }

  generarEstadisticas() {
    this.contarEmociones();
    this.contarEstrategias();
    this.contarComplicaciones();
  }

  contarEmociones() {
    const frecuencias: { [key: string]: number } = {};
    for (const entrada of this.entradas) {
      const emocion = entrada.emocionDelDia?.trim();
      if (emocion) {
        frecuencias[emocion] = (frecuencias[emocion] || 0) + 1;
      }
    }

    const labels = Object.keys(frecuencias);
    this.chartDataEmociones.labels = labels;
    this.chartDataEmociones.datasets[0].data = Object.values(frecuencias);
    this.chartDataEmociones.datasets[0].backgroundColor = labels.map(() => this.colorAleatorio());
  }

  contarEstrategias() {
    const conteo: { [key: string]: number } = {};
    for (const entrada of this.entradas) {
      const estrategias = entrada.estrategiasUsadas?.split(',').map(e => e.trim()) || [];
      for (const estrategia of estrategias) {
        if (estrategia) {
          conteo[estrategia] = (conteo[estrategia] || 0) + 1;
        }
      }
    }

    const labels = Object.keys(conteo);
    this.chartDataEstrategias.labels = labels;
    this.chartDataEstrategias.datasets[0].data = Object.values(conteo);
    this.chartDataEstrategias.datasets[0].backgroundColor = labels.map(() => this.colorAleatorio());
  }

  contarComplicaciones() {
    const conteo: { [key: string]: number } = {};
    for (const entrada of this.entradas) {
      const dificultades = entrada.complicacionesEncontradas?.split(',').map(t => t.trim()) || [];
      for (const dificultad of dificultades) {
        if (dificultad) {
          conteo[dificultad] = (conteo[dificultad] || 0) + 1;
        }
      }
    }

    const labels = Object.keys(conteo);
    this.chartDataComplicaciones.labels = labels;
    this.chartDataComplicaciones.datasets[0].data = Object.values(conteo);
    this.chartDataComplicaciones.datasets[0].backgroundColor = labels.map(() => this.colorAleatorio());
  }

  onTabChange(event: MatTabChangeEvent) {
    setTimeout(() => {
      if (event.index === 0) this.emocionesChart?.update();
      if (event.index === 1) this.estrategiasChart?.update();
      if (event.index === 2) this.complicacionesChart?.update();
    }, 100);
  }

  colorAleatorio(): string {
    const colores = ['#4caf50', '#2196f3', '#ff9800', '#e91e63', '#9c27b0', '#00bcd4', '#ffc107', '#607d8b'];
    return colores[Math.floor(Math.random() * colores.length)];
  }

  cerrarInfo() {
    this.modalInfoCerrando = true;
    setTimeout(() => {
      this.modalInfoAbierto = false;
      this.modalInfoCerrando = false;
    }, 300);
  }

  volverAlDiario() {
    this.router.navigate(['/dashboard/diario']);
  }
}
