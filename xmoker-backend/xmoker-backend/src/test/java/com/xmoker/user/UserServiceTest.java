// src/test/java/com/xmoker/user/service/UserServiceTest.java
package com.xmoker.user.service;

import com.xmoker.user.entity.AdminDatos;
import com.xmoker.user.entity.Progreso;
import com.xmoker.user.entity.RolUsuario;
import com.xmoker.user.entity.Usuario;
import com.xmoker.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void listarTodos_devuelveListaUsuarios() {
        // Arrange: simulamos dos usuarios en el repositorio
        Usuario u1 = new Usuario(); u1.setEmail("a@a.com");
        Usuario u2 = new Usuario(); u2.setEmail("b@b.com");
        when(userRepository.findAll()).thenReturn(List.of(u1, u2));

        // Act
        List<Usuario> resultado = userService.listarTodos();

        // Assert: se devuelven los dos usuarios
        assertEquals(2, resultado.size());
        assertEquals("a@a.com", resultado.get(0).getEmail());
    }

    @Test
    void registrar_usuarioRolUsuario_creaProgresoInicial() {
        // Arrange
        Usuario usuario = new Usuario();
        usuario.setPassword("raw");
        usuario.setRol(RolUsuario.USUARIO);

        when(passwordEncoder.encode("raw")).thenReturn("hashed");
        // Al guardar, devolvemos el mismo objeto
        when(userRepository.save(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act
        Usuario registrado = userService.registrar(usuario);

        // Assert: contraseña encriptada y progreso inicializado
        assertEquals("hashed", registrado.getPassword());
        assertNotNull(registrado.getProgreso());
        assertEquals(0, registrado.getProgreso().getDiasSinFumar());
    }

    @Test
    void registrar_usuarioRolAdministrador_asignaAdminDatos() {
        // Arrange
        Usuario usuario = new Usuario();
        usuario.setPassword("raw");
        usuario.setRol(RolUsuario.ADMINISTRADOR);

        when(passwordEncoder.encode("raw")).thenReturn("hashed");
        when(userRepository.save(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act
        Usuario registrado = userService.registrar(usuario);

        // Assert: adminDatos no es null y tiene fecha asignación
        AdminDatos ad = registrado.getAdminDatos();
        assertNotNull(ad);
        assertNotNull(ad.getFechaAsignacion());
        assertEquals("hashed", registrado.getPassword());
    }
}
