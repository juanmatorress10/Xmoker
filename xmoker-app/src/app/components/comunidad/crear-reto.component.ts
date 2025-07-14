import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators, AbstractControl } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ComunidadService } from '../../services/comunidad.service';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatInputModule } from '@angular/material/input';
import { MatNativeDateModule } from '@angular/material/core';

@Component({
  standalone: true,
  selector: 'app-crear-reto',
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatDatepickerModule,
    MatInputModule,
    MatNativeDateModule
  ],
  template: `
  <div class="max-w-xl mx-auto bg-white p-6 rounded-xl shadow mt-6">
    <h2 class="text-xl font-bold text-blue-700 mb-4 text-center">Crear nuevo reto 💪</h2>

    <form [formGroup]="retoForm" (ngSubmit)="crearReto()">

      <!-- TÍTULO -->
      <div class="mb-4">
        <label class="block text-sm font-medium mb-1">Título</label>
        <input formControlName="titulo" type="text" class="w-full px-4 py-2 border rounded-lg" placeholder="Ej. 7 días sin fumar">
        <div *ngIf="titulo.invalid && (titulo.dirty || titulo.touched)" class="text-red-600 text-sm mt-1">
          El título es obligatorio.
        </div>
      </div>

      <!-- DESCRIPCIÓN -->
      <div class="mb-4">
        <label class="block text-sm font-medium mb-1">Descripción</label>
        <textarea formControlName="descripcion" rows="3" class="w-full px-4 py-2 border rounded-lg" placeholder="Objetivo, motivación, etc."></textarea>
      </div>

      <!-- FECHA INICIO -->
      <mat-form-field appearance="fill" class="w-full mb-4">
        <mat-label>Fecha de inicio</mat-label>
        <input matInput formControlName="fechaInicio" [matDatepicker]="inicioPicker">
        <mat-datepicker-toggle matSuffix [for]="inicioPicker"></mat-datepicker-toggle>
        <mat-datepicker #inicioPicker></mat-datepicker>
      </mat-form-field>

      <!-- FECHA FIN -->
      <mat-form-field appearance="fill" class="w-full mb-4">
        <mat-label>Fecha de fin</mat-label>
        <input matInput formControlName="fechaFin" [matDatepicker]="finPicker">
        <mat-datepicker-toggle matSuffix [for]="finPicker"></mat-datepicker-toggle>
        <mat-datepicker #finPicker></mat-datepicker>
      </mat-form-field>

      <!-- VALIDACIÓN DE RANGO -->
      <div *ngIf="fechaInvalida" class="text-red-600 text-sm text-center mb-4">
        ❌ La fecha de fin debe ser posterior a la de inicio.
      </div>

      <!-- BOTONES -->
      <div class="flex gap-4 justify-center mt-6">
        <button type="submit" [disabled]="retoForm.invalid"
                class="bg-blue-600 text-white px-6 py-2 rounded-lg hover:bg-blue-700 transition">
          ➕ Crear reto
        </button>

        <button type="button"
                (click)="cancelar()"
                class="bg-gray-300 text-gray-800 px-6 py-2 rounded-lg hover:bg-gray-400 transition">
          ❌ Cancelar
        </button>
      </div>

    </form>
  </div>
  `
})
export class CrearRetoComponent implements OnInit {
  retoForm!: FormGroup;
  grupoId = 0;
  fechaInvalida = false;

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private comunidadService: ComunidadService
  ) {}

  ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get('grupoId');
    if (!idParam || isNaN(Number(idParam))) {
      alert('❌ ID de grupo no válido');
      this.router.navigate(['/comunidad']);
      return;
    }

    this.grupoId = Number(idParam);

    this.retoForm = this.fb.group({
      titulo: ['', Validators.required],
      descripcion: [''],
      fechaInicio: ['', Validators.required],
      fechaFin: ['', Validators.required]
    });
  }

  get titulo(): AbstractControl {
    return this.retoForm.get('titulo')!;
  }

  get fechaInicio(): AbstractControl {
    return this.retoForm.get('fechaInicio')!;
  }

  get fechaFin(): AbstractControl {
    return this.retoForm.get('fechaFin')!;
  }

crearReto(): void {
  this.fechaInvalida = false;

  if (this.retoForm.invalid) return;

  const { fechaInicio, fechaFin } = this.retoForm.value;

  if (new Date(fechaFin) <= new Date(fechaInicio)) {
    this.fechaInvalida = true;
    return;
  }

  const reto = {
    ...this.retoForm.value,
    fechaInicio: this.formatearFecha(fechaInicio),
    fechaFin: this.formatearFecha(fechaFin)
  };

  this.comunidadService.crearReto(this.grupoId, reto).subscribe({
    next: (nuevoReto) => {
      if (!nuevoReto?.id) {
        alert('⚠️ Reto creado pero no se pudo obtener su ID');
        this.router.navigate(['/comunidad']);
        return;
      }

      alert('✅ Reto creado correctamente');
      this.router.navigate(['/comunidad'], {
        queryParams: { nuevoRetoId: nuevoReto.id }
      });
    },
    error: () => {
      alert('❌ Error al crear el reto');
    }
  });
}


  cancelar(): void {
    this.router.navigate(['/comunidad']);
  }

  private formatearFecha(date: Date): string {
    // Formato YYYY-MM-DD
    const iso = date.toISOString();
    return iso.split('T')[0];
  }
}
