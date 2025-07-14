package com.xmoker.comunidad.dto;

import com.xmoker.comunidad.entity.UsuarioGrupo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioGrupoDTO {
    private Long id;
    private boolean esModerador;
    private GrupoDTO grupo;

    public UsuarioGrupoDTO(UsuarioGrupo ug) {
        this.id = ug.getId();
        this.esModerador = ug.isEsModerador();
        this.grupo = new GrupoDTO(ug.getGrupo());
    }
}

