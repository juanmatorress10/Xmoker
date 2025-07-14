package com.xmoker.consejo.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class ConsejoDiario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 1000)
    private String texto;

    private String categoria; // MOTIVACIONAL, SALUD, ESTRATEGIA, etc.

    private Integer diaSugerido; // Opcional: 1..365
}
