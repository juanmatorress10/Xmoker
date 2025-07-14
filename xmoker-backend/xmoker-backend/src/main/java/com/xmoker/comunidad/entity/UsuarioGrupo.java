package com.xmoker.comunidad.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.xmoker.user.entity.Usuario;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class UsuarioGrupo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonBackReference // o @JsonIgnoreProperties("usuarioGrupos")
    private Usuario usuario;

    @ManyToOne
    private GrupoApoyo grupo;

    private boolean esModerador = false;

    private LocalDateTime fechaIngreso = LocalDateTime.now();
}
