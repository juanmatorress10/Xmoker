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


        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

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
            }

            case ADMINISTRADOR -> {
                AdminDatos admin = new AdminDatos();
                admin.setRolDentroDelSistema("Moderador");
                admin.setFechaAsignacion(new Date());
                admin.setHistorialAcciones("[]");
                usuario.setAdminDatos(admin);
            }
        }

        return userRepository.save(usuario);
    }

    public List<Usuario> listarTodos() {
        return userRepository.findAll();
    }
}
