import { Routes } from '@angular/router';
import { HomeComponent } from './components/home/home.component';
import { RegisterComponent } from './components/register/register.component';
import { LoginComponent } from './components/login/login.component';
import { CuestionarioComponent } from './components/cuestionario/cuestionario.component';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { PerfilFumadorComponent } from './components/perfil-fumador/perfil-fumador.component';
import { MetricasAvanzadasComponent } from './components/dashboard/metricas-avanzadas/metricas-avanzadas.component';
import { DiarioComponent } from './components/dashboard/diario/diario.component';
import { EstadisticasDiarioComponent } from './components/dashboard/estadisticas-diario/estadisticas-diario.component';
import { ConfiguracionCuentaComponent } from './components/configuracion-cuenta/configuracion-cuenta/configuracion-cuenta.component';
import { LogrosComponent } from './components/logros/logros.component';

import { ComunidadComponent } from './components/comunidad/comunidad.component';
import { GrupoCrearComponent } from './components/comunidad/grupo-crear.component';
// Añadirás también estos si los generas:
import { GrupoUnirseComponent } from './components/comunidad/grupo-unirse.component';
import { CrearRetoComponent } from './components/comunidad/crear-reto.component';


export const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'login', component: LoginComponent },
  { path: 'cuestionario', component: CuestionarioComponent },
  { path: 'dashboard', component: DashboardComponent },
  { path: 'perfil-fumador', component: PerfilFumadorComponent },
  { path: 'dashboard/metricas-avanzadas', component: MetricasAvanzadasComponent },
  { path: 'dashboard/diario', component: DiarioComponent },
  { path: 'dashboard/estadisticas-diario', component: EstadisticasDiarioComponent },
  { path: 'cuenta/configuracion', component: ConfiguracionCuentaComponent },
  { path: 'logros', component: LogrosComponent },

  // 🧑‍🤝‍🧑 Comunidad
  { path: 'comunidad', component: ComunidadComponent },
  { path: 'comunidad/crear', component: GrupoCrearComponent },
  { path: 'comunidad/unirse', component: GrupoUnirseComponent }, // pendiente de generar
  { path: 'comunidad/crear-reto/:grupoId', component: CrearRetoComponent },
    // grupo completo
];
