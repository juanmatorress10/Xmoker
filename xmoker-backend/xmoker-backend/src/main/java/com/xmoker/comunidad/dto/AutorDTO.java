package com.xmoker.comunidad.dto;

import com.xmoker.user.entity.Usuario;

public class AutorDTO {
    private Long id;
    private String nombre;
    private String fotoPerfilUrl;

    public AutorDTO(Usuario usuario) {
        this.id = usuario.getId();
        this.nombre = usuario.getNombre();
        this.fotoPerfilUrl = usuario.getFotoPerfilUrl(); // ✅ Importante
    }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getFotoPerfilUrl() { return fotoPerfilUrl; }
}
