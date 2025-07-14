package com.xmoker.comunidad.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RetoListadoDTO {
    private Long id;
    private String titulo;
    private String descripcion;
    private String fechaInicio;
    private String fechaFin;
}
