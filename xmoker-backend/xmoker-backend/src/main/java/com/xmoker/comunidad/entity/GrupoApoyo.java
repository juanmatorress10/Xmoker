package com.xmoker.comunidad.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.xmoker.user.entity.Usuario;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
public class GrupoApoyo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String descripcion;

    private boolean esPrivado;
    @Column(unique = true)
    private String codigoAcceso;

    private LocalDateTime fechaCreacion = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creador_id")
    @JsonIgnoreProperties({"progreso", "logros", "adminDatos", "profesionalDatos"})
    private Usuario creador;

    public GrupoApoyo(Long id) {
        this.id = id;
    }
}
