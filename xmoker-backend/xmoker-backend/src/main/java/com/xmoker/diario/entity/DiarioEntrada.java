package com.xmoker.diario.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
public class DiarioEntrada {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long usuarioId;

    private LocalDateTime fechaCreacion;

    private String emocionDelDia;
    private String estrategiasUsadas;
    private String complicacionesEncontradas;
}
