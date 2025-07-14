import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';

@Component({
  selector: 'app-user-avatar',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './user-avatar.component.html',
  styleUrls: ['./user-avatar.component.css']
})
export class UserAvatarComponent {
  dropdownOpen = false;

  user: any = {};

  constructor(private router: Router) {
    const usuarioGuardado = localStorage.getItem('usuario');
    if (usuarioGuardado) {
      this.user = JSON.parse(usuarioGuardado);
    } else {
      this.user = { email: 'Desconocido' };
    }
  }
  toggleDropdown() {
    this.dropdownOpen = !this.dropdownOpen;
    if (this.dropdownOpen) {
      // Cargar siempre el usuario actualizado desde localStorage
      const usuarioGuardado = localStorage.getItem('usuario');
      this.user = usuarioGuardado ? JSON.parse(usuarioGuardado) : {};
    }
  }
  goToSettings() {
    this.router.navigate(['/cuenta/configuracion']);
  }

  logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('usuario');
    localStorage.removeItem('comunidad_tab');
    // cualquier otra clave que guardes
    this.router.navigate(['/login']);
  }

}

