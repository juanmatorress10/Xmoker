package com.xmoker.user.dto;

import lombok.Data;

@Data
public class UsuarioUpdateDTO {
    private String nombre;
    private String email;
    private String password;
    private String fotoPerfilUrl;
}
