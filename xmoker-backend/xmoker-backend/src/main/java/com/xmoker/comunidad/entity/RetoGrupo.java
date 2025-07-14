package com.xmoker.comunidad.entity;

import com.xmoker.user.entity.Usuario;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Entity
public class RetoGrupo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "creador_id")
    private Usuario creador;

    private String titulo;
    private String descripcion;

    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    @ManyToOne
    private GrupoApoyo grupo;

    @OneToMany(mappedBy = "reto", cascade = CascadeType.ALL)
    private List<UsuarioRetoGrupo> participantes;
}
