export interface Logro {
    nombre: string;
    descripcion: string;
    tipo: string;
    valorObjetivo: number;
    experienciaOtorgada: number;
    iconoUrl: string;
    categoria: string;
  }
  
  export interface UsuarioLogro {
    id: number; // 👈 ID del UsuarioLogro
    completado: boolean;
    progresoActual: number;
    fechaAlcanzado?: string;
    reclamado: boolean; // 👈 Añade este campo
    logro: Logro;
  }
  