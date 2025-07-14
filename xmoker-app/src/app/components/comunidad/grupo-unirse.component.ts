import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ComunidadService } from '../../services/comunidad.service';
import { RouterModule, Router } from '@angular/router';
import { NavbarSuperiorComponent } from '../navbar-superior/navbar-superior.component';
import { NavbarInferiorComponent } from '../navbar-inferior.component';

@Component({
  standalone: true,
  selector: 'app-grupo-unirse',
  templateUrl: './grupo-unirse.component.html',
  styleUrls: ['./grupo-unirse.component.css'],
  imports: [
    CommonModule,
    FormsModule,
    RouterModule,
    NavbarSuperiorComponent,
    NavbarInferiorComponent
  ]
})
export class GrupoUnirseComponent {
  private comunidadService = inject(ComunidadService);
  private router = inject(Router);

  codigo = '';
  loading = false;
  errorMsg = '';

  unirse() {
    const limpio = this.codigo.trim();
    if (!limpio) {
      this.errorMsg = 'Debes ingresar un código válido.';
      return;
    }

    this.loading = true;
    this.errorMsg = '';

    this.comunidadService.unirsePorCodigo(limpio)
      .subscribe({
        next: (mensaje: string) => {
          // Aquí podrías usar un toast; pongo alert() como placeholder
          alert(mensaje);
          this.router.navigate(['/comunidad']);
        },
        error: (err) => {
          // err.error viene como texto plano
          this.errorMsg = typeof err.error === 'string'
            ? err.error
            : 'Código inválido o error al unirse.';
        }
      })
      .add(() => {
        this.loading = false;
      });
  }
}
