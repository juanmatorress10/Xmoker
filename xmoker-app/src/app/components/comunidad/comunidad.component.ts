import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ClipboardModule, Clipboard } from '@angular/cdk/clipboard';
import { RouterModule, Router, ActivatedRoute } from '@angular/router';
import { ComunidadService } from '../../services/comunidad.service';
import { NavbarSuperiorComponent } from '../navbar-superior/navbar-superior.component';
import { NavbarInferiorComponent } from '../navbar-inferior.component';
import { PostFormComponent } from './post-form.component';
import { PostCardComponent } from './post-card.component';
import { ListaRetosComponent } from './lista-retos.component';
import { EstadisticasGrupoComponent } from './estadisticas-grupo.component';

@Component({
  standalone: true,
  selector: 'app-comunidad',
  imports: [
    CommonModule,
    RouterModule,
    ClipboardModule,
    NavbarSuperiorComponent,
    NavbarInferiorComponent,
    PostFormComponent,
    PostCardComponent,
    ListaRetosComponent,
    EstadisticasGrupoComponent
  ],
  template: `
    <app-navbar-superior></app-navbar-superior>

    <div class="fondo-xmoker">
      <h2 class="text-xl font-bold text-blue-800 mb-4 text-center">
        Tu Comunidad Xmoker 💙
      </h2>

      <ng-container *ngIf="!cargando">
        <ng-container *ngIf="grupos.length === 0; else tieneGrupo">
          <p class="text-gray-600 mb-4 text-center">
            Aún no formas parte de ningún grupo privado.
          </p>
          <div class="max-w-md mx-auto space-y-3">
            <button class="btn btn-primary w-full" (click)="irACrear()">
              ➕ Crear grupo privado
            </button>
            <button class="btn btn-outline-primary w-full" (click)="irAUnirse()">
              🔑 Unirme por código
            </button>
          </div>
        </ng-container>

        <ng-template #tieneGrupo>
          <div class="bg-white rounded shadow p-4 max-w-xl mx-auto mb-6">
            <h4 class="text-lg font-semibold text-blue-700">
              👥 Grupo: {{ grupos[0]?.grupo?.nombre || grupos[0]?.nombre }}
            </h4>
            <p class="text-sm text-gray-500">
              {{ grupos[0]?.grupo?.descripcion || grupos[0]?.descripcion }}
            </p>

            <!-- Código de acceso con copiar e indicador -->
            <div class="mt-2 flex items-center gap-2 relative">
              <code class="text-xs font-mono bg-gray-100 px-2 py-1 rounded">
                {{ codigoAcceso }}
              </code>
              <button
                class="btn btn-outline-primary btn-sm"
                (click)="copiarCodigo()"
              >
                📋 Copiar código
              </button>
              <div
                *ngIf="copiado"
                class="copiado-tooltip"
              >
                ¡Copiado!
              </div>
            </div>
          </div>

          <!-- Botones de acción -->
          <div class="max-w-xl mx-auto mb-4 flex justify-end gap-2">
            <button class="btn btn-outline-info btn-sm rounded-4" (click)="openInfoModal()">
              ℹ️ Info
            </button>
            <button class="btn btn-outline-danger btn-sm rounded-4" (click)="openAbandonDialog()">
              🚪 Abandonar grupo
            </button>
          </div>

          <!-- (modales de info y confirmación aquí...) -->

          <!-- Tabs -->
          <div class="flex justify-around border-b mb-4 text-sm font-medium">
            <button class="px-4 py-2" [class.border-b-2]="tab==='muro'" (click)="cambiarTab('muro')">📟 Muro</button>
            <button class="px-4 py-2" [class.border-b-2]="tab==='retos'" (click)="cambiarTab('retos')">🏆 Retos</button>
            <button class="px-4 py-2" [class.border-b-2]="tab==='stats'" (click)="cambiarTab('stats')">📊 Estadísticas</button>
          </div>

          <!-- Contenido según pestaña -->
          <div class="max-w-xl mx-auto">
            <ng-container *ngIf="tab==='muro'">
              <app-post-form (onPost)="crearPublicacion($event)"></app-post-form>
              <div class="my-4 space-y-4">
                <app-post-card
                  *ngFor="let post of posts"
                  [post]="post"
                  [currentUserId]="currentUserId"
                  (onEliminar)="eliminarPost(post.id)"
                ></app-post-card>
              </div>
            </ng-container>

            <ng-container *ngIf="tab==='retos'">
              <div class="text-center mb-6">
                <h3 class="text-lg font-semibold text-blue-700 mb-2">🏆 Retos del grupo</h3>
                <button
                  class="bg-blue-600 text-white text-sm px-4 py-2 rounded-xl shadow hover:bg-blue-700 transition"
                  (click)="irACrearReto()"
                >
                  ➕ Crear nuevo reto
                </button>
              </div>
              <app-lista-retos
                [grupoId]="grupos[0]?.grupo?.id || grupos[0]?.id"
                [currentUserName]="currentUserName"
              ></app-lista-retos>
            </ng-container>

            <ng-container *ngIf="tab==='stats'">
              <app-estadisticas-grupo
                [grupoId]="grupos[0]?.grupo?.id || grupos[0]?.id"
              ></app-estadisticas-grupo>
            </ng-container>
          </div>
        </ng-template>
      </ng-container>
    </div>

    <app-navbar-inferior></app-navbar-inferior>
  `,
  styleUrls: ['./comunidad.component.css']
})
export class ComunidadComponent implements OnInit {
  private comunidadService = inject(ComunidadService);
  private clipboard = inject(Clipboard);
  private router = inject(Router);
  private route = inject(ActivatedRoute);

  grupos: any[] = [];
  posts: any[] = [];
  cargando = true;
  tab: 'muro'|'retos'|'stats' = (localStorage.getItem('comunidad_tab') as any) || 'muro';
  currentUserId = 0;
  currentUserName = '';

  /** Código de acceso para copiar */
  codigoAcceso = '';

  /** Tooltip de “¡Copiado!” */
  copiado = false;

  showConfirmModal = false;
  showInfoModal = false;

  ngOnInit() {
    // Leer usuario
    const su = localStorage.getItem('usuario');
    if (su) {
      try {
        const u = JSON.parse(su);
        this.currentUserId = u.id; this.currentUserName = u.nombre;
      } catch {}
    }

    // Cargar grupos
    this.comunidadService.getMisGrupos().subscribe({
      next: res => {
        this.grupos = res; this.cargando = false;
        if (this.grupos.length) {
          const g = this.grupos[0].grupo || this.grupos[0];
          this.codigoAcceso = g.codigoAcceso;
          // nuevo reto?
          const nuevo = this.route.snapshot.queryParamMap.get('nuevoRetoId');
          if (nuevo) {
            this.tab = 'retos';
            localStorage.setItem('comunidad_tab','retos');
          }
          this.cargarPosts(g.id);
        }
      },
      error: () => { this.cargando = false; console.error('❌ Error al cargar grupos'); }
    });
  }

  copiarCodigo() {
    if (!this.codigoAcceso) return;
    this.clipboard.copy(this.codigoAcceso);
    this.copiado = true;
    setTimeout(() => this.copiado = false, 2000);
  }

  openAbandonDialog() { this.showConfirmModal = true; }
  openInfoModal()    { this.showInfoModal = true; }
  closeInfoModal()   { this.showInfoModal = false; }

  onConfirmAbandon() {
    const g = this.grupos[0].grupo||this.grupos[0];
    this.comunidadService.abandonarGrupo(g.id).subscribe({
      next: msg => { this.grupos=[]; this.showConfirmModal=false; },
      error: () => { this.showConfirmModal=false; }
    });
  }

  cambiarTab(n: 'muro'|'retos'|'stats') {
    this.tab = n; localStorage.setItem('comunidad_tab', n);
  }

  cargarPosts(id: number) {
    this.comunidadService.getPostsGrupo(id).subscribe({
      next: ps => this.posts = ps,
      error: () => {}
    });
  }

  crearPublicacion(data: { contenido:string; imagenUrl?:string }) {
    const g = this.grupos[0].grupo||this.grupos[0];
    this.comunidadService.crearPostConImagenUrl(g.id, data.contenido, data.imagenUrl)
      .subscribe(()=> this.cargarPosts(g.id));
  }

  eliminarPost(id: number) {
    if (!confirm('¿Eliminar post?')) return;
    this.comunidadService.eliminarPost(id).subscribe(()=>{
      const g = this.grupos[0].grupo||this.grupos[0];
      this.cargarPosts(g.id);
    });
  }

  irACrear()      { this.router.navigate(['/comunidad/crear']); }
  irAUnirse()     { this.router.navigate(['/comunidad/unirse']); }
  irACrearReto()  {
    const g = this.grupos[0].grupo||this.grupos[0];
    this.router.navigate(['/comunidad/crear-reto', g.id]);
  }
}
