package com.xmoker.user.dto;

import lombok.Data;

@Data
public class RespuestaDTO {
    private String codigo; // Código de la pregunta: "A", "B", ...
    private int valor;     // Valor de 1 a 5
}
