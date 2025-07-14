package com.xmoker.comunidad.dto;

import com.xmoker.comunidad.entity.GrupoApoyo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GrupoDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private String codigoAcceso;

    public GrupoDTO(GrupoApoyo g) {
        this.id = g.getId();
        this.nombre = g.getNombre();
        this.descripcion = g.getDescripcion();
        this.codigoAcceso = g.getCodigoAcceso();
    }
}


