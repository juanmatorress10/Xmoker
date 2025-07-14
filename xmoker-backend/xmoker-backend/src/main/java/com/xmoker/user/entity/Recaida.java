package com.xmoker.user.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
public class Recaida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.DATE)
    private Date fecha;


    private int cantidadFumada;

    private String motivo;

    @ManyToOne
    @JoinColumn(name = "progreso_id")
    @JsonBackReference
    private Progreso progreso;
}
