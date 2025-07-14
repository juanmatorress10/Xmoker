package com.xmoker.user.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
public class AdminDatos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String rolDentroDelSistema;

    @Temporal(TemporalType.DATE)
    private Date fechaAsignacion;

    @Lob
    private String historialAcciones;
}

