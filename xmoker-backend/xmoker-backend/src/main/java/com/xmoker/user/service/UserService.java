package com.xmoker.user.service;

import com.xmoker.user.entity.*;
import com.xmoker.user.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Usuario registrar(Usuario usuario) {
        // 1. Asigna un rol por defecto si es nulo para evitar el NullPointerException
        if (usuario.getRol() == null) {
            usuario.setRol(RolUsuario.USUARIO); // O el rol que prefieras por defecto
        }

        // 2. Codifica la contraseña (esto ya lo tenías bien)
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

        // 3. El switch ahora es seguro y nunca recibirá un valor nulo
        switch (usuario.getRol()) {
            case USUARIO -> {
                Progreso progreso = new Progreso();
                progreso.setDiasSinFumar(0);
                progreso.setCantidadCigarrillosEvitados(0);
                progreso.setDineroAhorrado(0);
                progreso.setRachaActual(0);
                progreso.setRachaMaxima(0);
                progreso.setHorasVidaRecuperadas(0);
                progreso.setRecaidas(new ArrayList<>());
                usuario.setProgreso(progreso);
            }
            case PROFESIONAL -> {
                // Lógica para el profesional si es necesaria
            }
            case ADMINISTRADOR -> {
                AdminDatos admin = new AdminDatos();
                admin.setRolDentroDelSistema("Moderador");
                admin.setFechaAsignacion(new Date());
                admin.setHistorialAcciones("[]");
                usuario.setAdminDatos(admin);
            }
        }

        // 4. Guarda el usuario en la base de datos
        return userRepository.save(usuario);
    }

    public List<Usuario> listarTodos() {
        return userRepository.findAll();
    }
}
