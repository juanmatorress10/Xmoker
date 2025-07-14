package com.xmoker.user.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.xmoker.logro.entity.UsuarioLogro;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Data
@Entity
@ToString(exclude = {"progreso", "profesionalDatos", "adminDatos", "logros"})

public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String email;
    private String password;
    private String nombre;

    @Column(nullable = false)
    private int nivel = 1;

    @Column(nullable = false)
    private int experiencia = 0;

    @Column(length = 1000)
    private String fotoPerfilUrl;

    // Relación con logros
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<UsuarioLogro> logros;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date fechaInicioProceso;

    private int nivelConsumo;
    private String motivaciones;

    private String estiloFumador;
    @Column(columnDefinition = "TEXT")
    private String descripcionEstiloFumador;

    @Enumerated(EnumType.STRING)
    private RolUsuario rol;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "progreso_id")
    @JsonManagedReference
    private Progreso progreso;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "profesional_id")
    private ProfesionalDatos profesionalDatos;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "admin_id")
    private AdminDatos adminDatos;
}
