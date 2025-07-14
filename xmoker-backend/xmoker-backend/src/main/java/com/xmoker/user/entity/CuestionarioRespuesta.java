package com.xmoker.user.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class CuestionarioRespuesta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String codigoPregunta; // A, B, C, ..., R

    private int puntuacion; // 1 a 5

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
}
