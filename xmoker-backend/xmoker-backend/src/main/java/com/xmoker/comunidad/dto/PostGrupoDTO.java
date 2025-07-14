package com.xmoker.comunidad.dto;

import com.xmoker.comunidad.entity.PostGrupo;
import com.xmoker.user.entity.Usuario;

import java.time.LocalDateTime;

public class PostGrupoDTO {

    private Long id;
    private String contenido;
    private String imagenUrl;
    private LocalDateTime fechaPublicacion;
    private AutorDTO autor;

    public PostGrupoDTO() {
        // Constructor vacío
    }

    public PostGrupoDTO(PostGrupo post) {
        this.id = post.getId();
        this.contenido = post.getContenido();
        this.imagenUrl = post.getImagenUrl();
        this.fechaPublicacion = post.getFechaPublicacion();

        Usuario autorEntity = post.getAutor();
        if (autorEntity != null) {
            this.autor = new AutorDTO(autorEntity); // Incluye id, nombre, foto
        }
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getContenido() {
        return contenido;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public LocalDateTime getFechaPublicacion() {
        return fechaPublicacion;
    }

    public AutorDTO getAutor() {
        return autor;
    }
}
