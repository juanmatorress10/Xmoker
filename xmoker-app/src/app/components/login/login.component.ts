import { Component }         from '@angular/core';
import { CommonModule }      from '@angular/common';
import { FormsModule }       from '@angular/forms';
import { RouterModule, Router } from '@angular/router';
import { AuthService }       from '../../services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  formData = { email: '', password: '' };

  constructor(
    private auth: AuthService,
    private router: Router
  ) {}

  login() {
    // 1️⃣ limpia sesión anterior
    this.auth.logout();

    // 2️⃣ pide token
    this.auth.login(this.formData).subscribe({
      next: token => {
        // guarda token
        localStorage.setItem('token', token);

        // 3️⃣ carga usuario actual
        this.auth.obtenerUsuarioActual().subscribe({
          next: usuario => {
            localStorage.setItem('usuario', JSON.stringify(usuario));
            alert('✅ Login exitoso');
            this.router.navigate(['/dashboard']);
          },
          error: err => {
            console.error('❌ Error obteniendo usuario', err);
            alert('Error al cargar datos de usuario');
          }
        });
      },
      error: err => {
        console.error('❌ Error login:', err);
        alert('Credenciales incorrectas');
      }
    });
  }
}
