package com.xmoker.user.dto;

public record UsuarioDTO(
        Long id,
        String nombre,
        String email,
        String estiloFumador,
        String descripcionEstiloFumador,
        String fotoPerfilUrl,
        int nivel,
        int experiencia,
        double porcentajeProgreso
) {}
