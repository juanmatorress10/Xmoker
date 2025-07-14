package com.xmoker.comunidad.entity;

import com.xmoker.user.entity.Usuario;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class UsuarioRetoGrupo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Usuario usuario;

    @ManyToOne
    private RetoGrupo reto;

    @Enumerated(EnumType.STRING)
    private EstadoRetoUsuario estado = EstadoRetoUsuario.PENDIENTE;
}
