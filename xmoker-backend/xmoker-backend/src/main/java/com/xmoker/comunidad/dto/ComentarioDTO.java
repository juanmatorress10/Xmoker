package com.xmoker.comunidad.dto;

import com.xmoker.comunidad.entity.ComentarioGrupo;

import java.time.LocalDateTime;

public class ComentarioDTO {
    private Long id;
    private String contenido;
    private String autorNombre;
    private String autorFotoUrl;
    private LocalDateTime fechaComentario;

    public ComentarioDTO(ComentarioGrupo comentario) {
        this.id = comentario.getId();
        this.contenido = comentario.getContenido();
        this.fechaComentario = comentario.getFechaComentario();
        this.autorNombre = comentario.getAutor().getNombre();
        this.autorFotoUrl = comentario.getAutor().getFotoPerfilUrl(); // ← CLAVE
    }

    public Long getId() { return id; }
    public String getContenido() { return contenido; }
    public String getAutorNombre() { return autorNombre; }
    public String getAutorFotoUrl() { return autorFotoUrl; }
    public LocalDateTime getFechaComentario() { return fechaComentario; }
}



