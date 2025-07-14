import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { BaseChartDirective } from 'ng2-charts';
import { Chart, registerables } from 'chart.js';
import type { ChartData } from 'chart.js';
import { NavbarInferiorComponent } from '../../navbar-inferior.component';
import { NavbarSuperiorComponent } from '../../navbar-superior/navbar-superior.component';

Chart.register(...registerables);

@Component({
  selector: 'app-metricas-avanzadas',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    BaseChartDirective,
    NavbarInferiorComponent,
    NavbarSuperiorComponent
  ],
  templateUrl: './metricas-avanzadas.component.html',
  styleUrls: ['./metricas-avanzadas.component.css']
})
export class MetricasAvanzadasComponent {
  metricas: { nombre: string; valor: string }[] = [];
  filtro = '';

  // Modal de descripción
  descripcionModal = '';
  modalAbierto = false;
  modalCerrando = false;

  // Modal de info de página
  showPageInfoModal = false;

  // Métricas que se representan en %
  metricasPorcentaje = [
    'Capacidad pulmonar recuperada',
    'Reducción riesgo cardiovascular',
    'Mejora sistema inmune',
    'Mejora calidad del sueño',
    'Reducción estrés'
  ];

  constructor() {
    const guardadas = localStorage.getItem('metricasAvanzadas');
    if (guardadas) {
      this.metricas = JSON.parse(guardadas);
    }
  }

  get metricasFiltradas() {
    return this.filtro
      ? this.metricas.filter(m => m.nombre === this.filtro)
      : this.metricas;
  }

  // Abre modal de info de página
  openPageInfoModal() {
    this.showPageInfoModal = true;
  }

  // Cierra modal de info de página
  closePageInfoModal() {
    this.showPageInfoModal = false;
  }

  mostrarDescripcion(nombre: string) {
    const descripciones: Record<string,string> = {
      'CO₂ no emitido': '¡Cada cigarro que no fumas evita contaminar el aire! Tu impacto positivo en el planeta crece día a día. 🌍',
      'Capacidad pulmonar recuperada': 'Tus pulmones se regeneran y respiran mejor. ¡Cada día das un paso hacia una vida más plena y libre! 🫁✨',
      'Reducción riesgo cardiovascular': 'Dejar de fumar fortalece tu corazón. Cada día reduces el riesgo de enfermedades graves. ❤️💪',
      'Mejora sistema inmune': 'Tu cuerpo se vuelve más fuerte para defenderse de virus y enfermedades. ¡Te haces más imparable! 🛡️',
      'Mejora calidad del sueño': 'Duermes mejor, descansas más y tu mente se renueva. ¡Cada noche es más reparadora! 😴🌙',
      'Reducción estrés': 'Dejar de fumar reduce tus niveles de estrés a largo plazo. ¡Eres más sereno y equilibrado! 🧘‍♂️',
      'Días sin fumar': 'Cada día cuenta. ¡Eres más fuerte que ayer y cada paso importa! 🚀',
      'Cigarrillos evitados': 'Cada cigarro que no enciendes es una victoria. ¡Suma y sigue! 👏',
      'Dinero ahorrado': 'Tu bolsillo también respira. ¡Mira todo lo que estás ganando! 💰',
      'Vida ganada': 'Horas de vida extra para disfrutar de quienes amas. ¡Un regalo impagable! ❤️'
    };
    this.descripcionModal = descripciones[nombre] || '¡Sigue así! Cada avance cuenta en tu camino libre de humo. 🚀';
    this.modalAbierto = true;
  }

  cerrarModal() {
    this.modalCerrando = true;
    setTimeout(() => {
      this.modalAbierto = false;
      this.modalCerrando = false;
    }, 300);
  }

  esPorcentaje(nombre: string): boolean {
    return this.metricasPorcentaje.includes(nombre);
  }

  obtenerValorNumerico(valor: string): number {
    const num = parseFloat(valor.replace('%','').replace(',','.').trim());
    return isNaN(num) ? 0 : num;
  }

  volver() {
    window.history.back();
  }
}
