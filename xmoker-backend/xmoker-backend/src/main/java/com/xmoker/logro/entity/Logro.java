package com.xmoker.logro.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Logro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String descripcion;
    private String iconoUrl;


    @Column(nullable = false)
    private String categoria;

    @Enumerated(EnumType.STRING)
    private TipoLogro tipo;

    private int valorObjetivo;
    private int experienciaOtorgada;

    public Logro() {}

    public Logro(String nombre, String descripcion, TipoLogro tipo, int valorObjetivo, int experienciaOtorgada, String iconoUrl, String categoria) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.tipo = tipo;
        this.valorObjetivo = valorObjetivo;
        this.experienciaOtorgada = experienciaOtorgada;
        this.iconoUrl = iconoUrl;
        this.categoria = categoria;
    }
}

