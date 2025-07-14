// src/main/java/com/xmoker/comunidad/dto/MiembroDTO.java
package com.xmoker.comunidad.dto;

public class MiembroDTO {
    private Long id;
    private String nombre;

    public MiembroDTO(Long id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }
    public Long getId()       { return id; }
    public String getNombre() { return nombre; }
}
