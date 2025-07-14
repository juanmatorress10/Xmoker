package com.xmoker.comunidad.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EstadisticasGrupoDTO {
    private Long grupoId;
    private int totalMiembros;
    private int totalDiasSinFumar;
    private int totalPublicaciones;
    private int totalRetos;
    private int retosActivos;
    private double mediaRachaActual;
    private double mediaRachaMaxima;
}
