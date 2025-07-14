import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ComunidadService } from '../../services/comunidad.service';
import { NavbarInferiorComponent } from '../navbar-inferior.component';
import { NavbarSuperiorComponent } from '../navbar-superior/navbar-superior.component';
import { Router, RouterModule } from '@angular/router';
import { Clipboard } from '@angular/cdk/clipboard';
import { ClipboardModule } from '@angular/cdk/clipboard';

@Component({
  standalone: true,
  selector: 'app-grupo-crear',
  templateUrl: './grupo-crear.component.html',
  styleUrls: ['./grupo-crear.component.css'],
  imports: [
    CommonModule,
    FormsModule,
    RouterModule,
    ClipboardModule,
    NavbarInferiorComponent,
    NavbarSuperiorComponent
  ],
})
export class GrupoCrearComponent {
  grupoCreado: any = null;

  grupo = {
    nombre: '',
    descripcion: '',
    esPrivado: true
  };

  constructor(
    private comunidadService: ComunidadService,
    private clipboard: Clipboard,
    private router: Router
  ) {}

  crearGrupo() {
    if (!this.grupo.nombre.trim()) {
      alert('El nombre del grupo es obligatorio.');
      return;
    }

    this.comunidadService.crearGrupo(this.grupo).subscribe({
      next: (res) => {
        this.grupoCreado = res;
      },
      error: () => alert('Error al crear el grupo')
    });
  }

  copiarAlPortapapeles(codigo: string) {
    this.clipboard.copy(codigo);
    alert('¡Código copiado!');
  }

  irAComunidad() {
    this.router.navigate(['/comunidad']);
  }
}
