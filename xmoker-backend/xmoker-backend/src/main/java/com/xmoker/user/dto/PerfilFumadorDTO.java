package com.xmoker.user.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class PerfilFumadorDTO {
    private Map<String, Integer> puntuaciones;
    private List<String> categoriasAltas;
    private String mensajeResumen;
}
