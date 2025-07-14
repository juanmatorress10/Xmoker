package com.xmoker.comunidad.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class RetoResumenDTO {
    private Long retoId;
    private String titulo;
    private String fechaInicio;
    private String fechaFin;
    private int totalParticipantes;
    private int totalCompletados;
    private double porcentajeCompletado;
    private List<UsuarioEnRetoDTO> participantes;
    private String creadorNombre;
}
