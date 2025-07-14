import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule }     from '@angular/common';
import { FormsModule }      from '@angular/forms';
import { Router }           from '@angular/router';
import { switchMap }        from 'rxjs/operators';
import { AuthService }      from '../../services/auth.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit, OnDestroy {
  formData = {
    email: '',
    password: '',
    nombre: '',
    fechaInicioProceso: '',
    nivelConsumo: 0,
    motivacionesSeleccionadas: [] as string[],
    motivaciones: '',
    tipoCuenta: ''  // sólo 'USUARIO'
  };

  motivosDisponibles = [
    "Quiero recuperar mi salud y sentirme mejor cada día.",
    "Me preocupa no poder estar sano para disfrutar con mi familia.",
    "Quiero ahorrar dinero y usarlo en cosas que realmente me hagan feliz.",
    "Quiero manejar mejor mi estrés sin depender del tabaco.",
    "Quiero rendir más en el deporte y sentirme fuerte.",
    "Quiero ganar autoestima y demostrarme que puedo lograrlo.",
    "Tengo otras razones personales que me motivan a dejar de fumar."
  ];

  paso = 0;
  errorMsg = '';

  // Hold-to-start
  holdDuration = 2000;       // ms requeridos
  holdTime = 0;              // ms actuales
  private holdInterval!: number;

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.formData.fechaInicioProceso = new Date().toISOString().substring(0, 10);
    localStorage.removeItem('token');
    localStorage.removeItem('usuario');
  }

  ngOnDestroy(): void {
    this.clearHoldInterval();
  }

  // Inicia el temporizador de “hold”
  startHold() {
    this.clearHoldInterval();
    this.holdTime = 0;
    this.holdInterval = window.setInterval(() => {
      this.holdTime += 50;
      if (this.holdTime >= this.holdDuration) {
        this.clearHoldInterval();
        this.beginRegistration();
      }
    }, 50);
  }

  // Cancela el “hold”
  cancelHold() {
    this.clearHoldInterval();
    this.holdTime = 0;
  }

  private clearHoldInterval() {
    if (this.holdInterval) {
      clearInterval(this.holdInterval);
    }
  }

  // Se dispara al completar el hold
  private beginRegistration() {
    this.formData.tipoCuenta = 'USUARIO';
    this.paso = 1;
  }

  siguientePaso() {
    this.errorMsg = '';
    switch (this.paso) {
      case 1:
        if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(this.formData.email)) {
          this.errorMsg = 'Introduce un correo válido.'; return;
        }
        break;
      case 2:
        if (this.formData.password.length < 6) {
          this.errorMsg = 'La contraseña debe tener 6+ caracteres.'; return;
        }
        break;
      case 3:
        if (!this.formData.nombre.trim()) {
          this.errorMsg = 'El nombre no puede estar vacío.'; return;
        }
        break;
      case 5:
        if (this.formData.nivelConsumo < 1) {
          this.errorMsg = 'Nivel de consumo debe ser al menos 1.'; return;
        }
        break;
      case 6:
        if (this.formData.motivacionesSeleccionadas.length === 0) {
          this.errorMsg = 'Selecciona al menos una motivación.'; return;
        }
        break;
    }
    this.paso++;
  }

  toggleMotivacion(motivo: string) {
    const idx = this.formData.motivacionesSeleccionadas.indexOf(motivo);
    if (idx >= 0) this.formData.motivacionesSeleccionadas.splice(idx, 1);
    else this.formData.motivacionesSeleccionadas.push(motivo);
  }

  registrar() {
    this.formData.motivaciones = this.formData.motivacionesSeleccionadas.join(', ');
    this.authService.registrar(this.formData).pipe(
      switchMap(resp => {
        localStorage.setItem('token', resp.token);
        return this.authService.login({
          email: this.formData.email,
          password: this.formData.password
        });
      })
    ).subscribe({
      next: token => {
        localStorage.setItem('token', token);
        this.authService.obtenerUsuarioActual().subscribe({
          next: usuario => {
            localStorage.setItem('usuario', JSON.stringify(usuario));
            this.router.navigate(['/cuestionario']);
          },
          error: () => this.router.navigate(['/login'])
        });
      },
      error: () => {
        alert('Algo falló. Prueba a iniciar sesión manualmente.');
        this.router.navigate(['/login']);
      }
    });
  }
}
