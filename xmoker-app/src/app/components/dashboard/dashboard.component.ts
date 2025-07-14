import { Component, ViewContainerRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { ConsejosModalComponent } from './consejos-modal/consejos-modal.component';
import { DashboardService } from '../../services/dashboard.service';
import { RecaidaModalComponent } from './recaida-modal/recaida-modal.component';
import { AnsiedadModalComponent } from './ansiedad-modal/ansiedad-modal.component';
import type { ChartData } from 'chart.js';
import { NavbarInferiorComponent } from '../navbar-inferior.component';
import { NavbarSuperiorComponent } from '../navbar-superior/navbar-superior.component';
import { Chart, registerables } from 'chart.js';

Chart.register(...registerables);

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, NavbarInferiorComponent, NavbarSuperiorComponent],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent {
  nombreUsuario = '';
  tipoFumador = '';
  descripcionFumador = '';
  usuarioId: number = 0;
  motivacionesUsuario: string[] = [];

  progresoTiempoFormateado = '';
  diasSinFumar = 0;
  cigarrillosEvitados = 0;
  dineroAhorrado = 0;
  horasVidaGanadas = 0;

  isLoading = true;
  errorMsg = '';

  donutPulmonarData: ChartData<'doughnut'> = {
    labels: ['Recuperado', 'Por recuperar'],
    datasets: [{ data: [0, 100], backgroundColor: ['#4CAF50', '#E0E0E0'] }]
  };

  donutAhorroData: ChartData<'doughnut'> = {
    labels: ['Ahorrado', 'Restante'],
    datasets: [{ data: [0, 100], backgroundColor: ['#2196F3', '#E0E0E0'] }]
  };

  constructor(
    private viewContainerRef: ViewContainerRef,
    private dashboardService: DashboardService,
    private router: Router,
  ) {}

  ngOnInit() {
    const usuarioGuardado = localStorage.getItem('usuario');
    if (usuarioGuardado) {
      const usuario = JSON.parse(usuarioGuardado); // 👈 primero parseas
  
      console.log('🧍 Usuario cargado:', usuario);
      console.log('🧪 Tipo de fumador (bruto):', usuario.estiloFumador); 
  
      this.nombreUsuario = usuario.nombre;
      this.usuarioId = usuario.id;
      this.tipoFumador = usuario.estiloFumador || '';
      this.descripcionFumador = usuario.descripcionEstiloFumador || '';
      this.motivacionesUsuario = (usuario.motivaciones || '')
      .split(',')
      .map((m: string) => m.trim())
      .filter((m: string) => m.length > 0);
      this.cargarMetricas();
    } else {
      this.isLoading = false;
      this.errorMsg = 'No hay información de usuario disponible.';
    }
  }

  formatearTipo(tipo: string): string {
    const mapa: { [key: string]: string } = {
      "Estimulación": "Fumador estimulado",
      "Refuerzo gestual": "Fumador por gestos",
      "Placer-relajación": "Fumador relajante",
      "Reducción estados negativos": "Fumador por estrés",
      "Adicción": "Fumador dependiente",
      "Automatismo": "Fumador automático",
      "Moderado": "Fumador ocasional"
    };
    return tipo.split(',').map(t => mapa[t.trim()] || t.trim()).join(', ');
  }

  mostrarConsejos() {
    const modalRef = this.viewContainerRef.createComponent(ConsejosModalComponent);
    modalRef.instance.tipoFumador = this.tipoFumador;
  }

  cargarMetricas() {
    this.dashboardService.obtenerMetricas(this.usuarioId).subscribe({
      next: (metricas) => {
        this.progresoTiempoFormateado = metricas.progresoTiempo;
        this.diasSinFumar = metricas.diasSinFumar;
        this.cigarrillosEvitados = metricas.cigarrillosEvitados;
        this.dineroAhorrado = metricas.dineroAhorrado;
        this.horasVidaGanadas = metricas.horasVidaGanadas;
        localStorage.setItem('metricasAvanzadas', JSON.stringify([
          { nombre: 'Días sin fumar', valor: String(metricas.diasSinFumar) },
          { nombre: 'Cigarrillos evitados', valor: String(metricas.cigarrillosEvitados) },
          { nombre: 'Dinero ahorrado', valor: `${metricas.dineroAhorrado}` },
          { nombre: 'Vida ganada', valor: `${Number(metricas.horasVidaGanadas).toFixed(2)} h` },
          { nombre: 'Capacidad pulmonar recuperada', valor: `${((Number(metricas.horasVidaGanadas) / 48) * 100).toFixed(1)}%` },
          { nombre: 'Reducción riesgo cardiovascular', valor: `${((Number(metricas.diasSinFumar) / 90) * 100).toFixed(1)}%` },
          { nombre: 'Mejora sistema inmune', valor: `${((Number(metricas.diasSinFumar) / 30) * 100).toFixed(1)}%` },
          { nombre: 'Mejora calidad del sueño', valor: `${((Number(metricas.diasSinFumar) / 15) * 100).toFixed(1)}%` },
          { nombre: 'Reducción estrés', valor: `${((Number(metricas.diasSinFumar) / 60) * 100).toFixed(1)}%` },
          { nombre: 'CO₂ no emitido', valor: `${(Number(metricas.cigarrillosEvitados) * 0.014).toFixed(2)} g` }
        ]));
        

        const porcentajePulmonar = Math.min((this.horasVidaGanadas / 48) * 100, 100);
        const porcentajeAhorro = Math.min((this.dineroAhorrado / 500) * 100, 100);

        this.donutPulmonarData = {
          labels: ['Recuperado', 'Por recuperar'],
          datasets: [{ data: [porcentajePulmonar, 100 - porcentajePulmonar], backgroundColor: ['#4CAF50', '#E0E0E0'] }]
        };

        this.donutAhorroData = {
          labels: ['Ahorrado', 'Restante'],
          datasets: [{ data: [porcentajeAhorro, 100 - porcentajeAhorro], backgroundColor: ['#2196F3', '#E0E0E0'] }]
        };

        this.isLoading = false;
      },
      error: (err) => {
        console.error('❌ Error cargando métricas:', err);
        this.isLoading = false;
        this.errorMsg = 'Error cargando tu progreso. Intenta más tarde.';
      }
    });
  }



  abrirFormularioDiario() {
    this.router.navigate(['/dashboard/diario']);
  }

  verEstadisticas() {
    this.router.navigate(['/dashboard/metricas-avanzadas']);
  }

  abrirFormularioRecaida() {
    const modal = document.createElement('div');
    modal.classList.add('modal-host');
    document.body.appendChild(modal);
  
    const modalRef = this.viewContainerRef.createComponent(RecaidaModalComponent);
  
    modalRef.instance.userId = this.usuarioId;
  
    // 👇 ASEGÚRATE de pasar el array ya formateado
    modalRef.instance.motivacionesUsuario = this.motivacionesUsuario;
  }


abrirModalAnsiedad() {
  import('./ansiedad-modal/ansiedad-modal.component').then(({ AnsiedadModalComponent }) => {
    const modalHost = document.createElement('div');
    modalHost.classList.add('modal-host');
    document.body.appendChild(modalHost);

    const modalRef = this.viewContainerRef.createComponent(AnsiedadModalComponent);
    const instancia = modalRef.instance as any;

    instancia.userId = this.usuarioId;

    // ✅ PASAMOS LAS MOTIVACIONES
    instancia.motivacionesUsuario = this.motivacionesUsuario;

    // ✅ PARA permitirle auto-destruirse
    instancia.selfRef = modalRef;
  });
}
}
