import { Component, OnInit } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  Validators,
  ReactiveFormsModule,
  FormsModule
} from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { UsuarioService } from '../../../services/usuario.service';

@Component({
  selector: 'app-configuracion-cuenta',
  standalone: true,
  templateUrl: './configuracion-cuenta.component.html',
  styleUrls: ['./configuracion-cuenta.component.css'],
  imports: [CommonModule, ReactiveFormsModule, FormsModule]
})
export class ConfiguracionCuentaComponent implements OnInit {
  formulario!: FormGroup;
  imagenPreview: string = 'images/user-icon.png';
  usuarioId: number = 0;

  editarNombre = false;
  editarEmail = false;
  editarPassword = false;
  archivoSeleccionado!: File;

  constructor(
    private fb: FormBuilder,
    private usuarioService: UsuarioService,
    private router: Router
  ) {}

  ngOnInit(): void {
    const usuarioGuardado = localStorage.getItem('usuario');
    if (usuarioGuardado) {
      const user = JSON.parse(usuarioGuardado);
      this.usuarioId = user.id;
      this.imagenPreview = user.fotoPerfilUrl || this.imagenPreview;

      this.formulario = this.fb.group({
        nombre: [{ value: user.nombre || '', disabled: true }],
        email: [{ value: user.email || '', disabled: true }, [Validators.email]],
        password: [{ value: '', disabled: true }, Validators.minLength(6)],
        fotoPerfilUrl: [user.fotoPerfilUrl || this.imagenPreview]
      });
    }
  }

  onImagenSeleccionada(event: any) {
    const archivo = event.target.files[0];
    if (!archivo) return;
  
    this.archivoSeleccionado = archivo;
  
    const lector = new FileReader();
    lector.onload = () => {
      this.imagenPreview = lector.result as string;
    };
    lector.readAsDataURL(archivo);
  
    // Subir al backend y actualizar campo fotoPerfilUrl
    this.usuarioService.subirFoto(this.usuarioId, archivo).subscribe({
      next: (rutaRelativa: string) => {
        // Esto asegura que fotoPerfilUrl se guarde al hacer submit
        this.formulario.patchValue({ fotoPerfilUrl: rutaRelativa });
      },
      error: (err) => {
        console.error('❌ Error al subir la imagen', err);
        alert('❌ No se pudo subir la imagen');
      }
    });
  }

  volverAlInicio() {
    this.router.navigate(['/dashboard']);
  }

  guardarCambios() {
    const datos = { ...this.formulario.value };

    if (!datos.password || datos.password.trim() === '') {
      delete datos.password;
    }

    // Asegura ruta de imagen válida
    if (!datos.fotoPerfilUrl) {
      datos.fotoPerfilUrl = 'images/user-icon.png';
    }

    this.usuarioService.actualizarUsuario(this.usuarioId, datos).subscribe({
      next: (usuarioActualizado: any) => {
        alert('✅ Cambios guardados correctamente');
        localStorage.setItem('usuario', JSON.stringify(usuarioActualizado));
        this.router.navigate(['/dashboard']);
      },
      error: (err: any) => {
        console.error('❌ Error al actualizar usuario:', err);
        alert('❌ Error al guardar los cambios');
      }
    });
  }

  toggleCampo(campo: 'nombre' | 'email' | 'password') {
    const control = this.formulario.get(campo);
    if (control?.disabled) {
      control.enable();
    } else {
      control?.disable();
    }
  }
}
