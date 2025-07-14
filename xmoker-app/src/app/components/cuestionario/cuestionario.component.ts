import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CuestionarioService } from '../../services/cuestionario.service';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-cuestionario',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './cuestionario.component.html',
  styleUrls: ['./cuestionario.component.css']
})
export class CuestionarioComponent implements OnInit {
  usuarioId: number = 0;
  nombre: string = 'Usuario';

  preguntas: string[] = [
    "Fumo cuando me siento feliz.",
    "Fumo para calmarme cuando estoy nervioso.",
    "Fumo por aburrimiento.",
    "Fumo por hábito después de comer.",
    "Fumo cuando bebo alcohol.",
    "Fumo por imitación social.",
    "Fumo cuando me siento solo.",
    "Fumo para concentrarme.",
    "Fumo para evitar subir de peso.",
    "Fumo cuando me siento frustrado.",
    "Fumo cuando estoy estresado.",
    "Fumo para recompensarme.",
    "Fumo cuando salgo con amigos.",
    "Fumo por costumbre en ciertas rutinas.",
    "Fumo cuando veo a alguien más fumando.",
    "Fumo para aliviar el dolor emocional.",
    "Fumo para sentirme en control.",
    "Fumo cuando estoy feliz por algo."
  ];

  respuestas: number[] = [];
  paso: number = 0;
  perfilResultado: string | null = null;
  perfilMensaje: string | null = null;
  cuestionarioEnviado: boolean = false; // Controlar si ya hemos enviado

  constructor(
    private cuestionarioService: CuestionarioService,
    private router: Router
  ) {}

  ngOnInit(): void {
    const usuarioGuardado = localStorage.getItem('usuario');
    if (usuarioGuardado) {
      const usuario = JSON.parse(usuarioGuardado);
      this.nombre = usuario.nombre;
      this.usuarioId = usuario.id;
    }
  }

  seleccionarRespuesta(valor: number) {
    this.respuestas[this.paso] = valor;
  
    if (this.paso < this.preguntas.length - 1) {
      // Todavía quedan preguntas
      this.paso++;
    } else {
      // Ya hemos respondido todas las preguntas
      this.enviarRespuestas();
    }
  }
  enviarRespuestas() {
    const respuestasDTO = this.respuestas
      .map((valor, index) => ({
        codigo: index + 1,
        valor: valor
      }))
      .filter(dto => dto.valor !== undefined && dto.valor !== null);
  
    this.cuestionarioService.enviarRespuestas(this.usuarioId, respuestasDTO).subscribe({
      next: () => {
        console.log('✅ Respuestas enviadas correctamente');
        this.cuestionarioService.obtenerPerfil(this.usuarioId).subscribe({
          next: perfilDto => {
            this.router.navigate(['/perfil-fumador'], { state: { perfil: perfilDto } });
          },
          error: error => {
            console.error('❌ Error obteniendo perfil:', error);
          }
        });
      },
      error: error => {
        console.error('❌ Error enviando respuestas:', error);
      }
    });
  }

  irAlDashboard() {
    this.router.navigate(['/dashboard']);
  }
}
