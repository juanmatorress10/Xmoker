package com.xmoker.user.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;

@Data
public class RecaidaRequest {

    @NotNull(message = "El ID de usuario es obligatorio")
    private Long usuarioId;

    @NotNull(message = "La fecha de recaída es obligatoria")
    private Date fecha;

    @Min(value = 1, message = "Debe indicar al menos 1 cigarrillo fumado")
    private int cantidadFumada;

    @NotBlank(message = "Debe indicar el motivo de la recaída")
    private String motivo;
}
