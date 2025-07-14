package com.xmoker.user.entity;


import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class ProfesionalDatos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String especialidad;
    private String centroSalud;
    private String numeroColegiado;
    private String descripcion;
}
