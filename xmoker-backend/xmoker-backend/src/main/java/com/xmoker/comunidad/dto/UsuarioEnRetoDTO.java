package com.xmoker.comunidad.dto;

import com.xmoker.comunidad.entity.EstadoRetoUsuario;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UsuarioEnRetoDTO {
    private Long usuarioId;
    private String nombre;
    private EstadoRetoUsuario estado;
}
