import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-perfil-fumador',
  standalone: true,  // 🔥 importante si es standalone
  imports: [CommonModule],
  templateUrl: './perfil-fumador.component.html',
  styleUrls: ['./perfil-fumador.component.css']
})
export class PerfilFumadorComponent {
  perfil: any;

  constructor(private router: Router) {
    const navigation = this.router.getCurrentNavigation();
    this.perfil = navigation?.extras?.state?.['perfil'];

    if (!this.perfil) {
      this.router.navigate(['/dashboard']);
    }
  }

  irAlDashboard() {
    this.router.navigate(['/dashboard']);
  }
}
