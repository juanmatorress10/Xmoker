package com.xmoker.logro.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.xmoker.user.entity.Usuario;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class UsuarioLogro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonBackReference
    private Usuario usuario;



    @Column(nullable = false)
    private boolean reclamado = false;

    @ManyToOne
    private Logro logro;

    private boolean completado;
    private int progresoActual;
    private LocalDateTime fechaAlcanzado;

    // Constructor vacío para JPA
    public UsuarioLogro() {}

    // Constructor personalizado
    public UsuarioLogro(Usuario usuario, Logro logro) {
        this.usuario = usuario;
        this.logro = logro;
        this.completado = false;
        this.progresoActual = 0;
    }

    // Getters y setters (o usa Lombok con @Data)
}

