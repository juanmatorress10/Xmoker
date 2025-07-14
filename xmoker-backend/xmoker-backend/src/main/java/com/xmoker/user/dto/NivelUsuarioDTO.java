package com.xmoker.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NivelUsuarioDTO {
    private int nivel;
    private int experiencia;
    private double progresoPorcentaje;
}
