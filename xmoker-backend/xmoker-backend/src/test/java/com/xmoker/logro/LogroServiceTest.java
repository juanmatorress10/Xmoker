// src/test/java/com/xmoker/logro/service/LogroServiceTest.java
package com.xmoker.logro.service;

import com.xmoker.logro.entity.Logro;
import com.xmoker.logro.entity.TipoLogro;
import com.xmoker.logro.entity.UsuarioLogro;
import com.xmoker.logro.repository.LogroRepository;
import com.xmoker.logro.repository.UsuarioLogroRepository;
import com.xmoker.user.entity.Progreso;
import com.xmoker.user.entity.Usuario;
import com.xmoker.user.repository.UserRepository;
import com.xmoker.user.service.NivelService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LogroServiceTest {

    @Mock       private LogroRepository logroRepository;
    @Mock       private UsuarioLogroRepository usuarioLogroRepository;
    @Mock       private UserRepository usuarioRepository;
    @Mock       private NivelService nivelService;
    @InjectMocks private LogroService service;  // :contentReference[oaicite:2]{index=2}

    @Test
    void evaluarLogrosParaUsuario_logroCumplido_aplicaExperienciaYGuardaEstado() {
        // Arrange
        Usuario usuario = new Usuario();
        usuario.setProgreso(new Progreso());
        usuario.getProgreso().setDiasSinFumar(7);
        usuario.setExperiencia(100);

        Logro logro = new Logro();
        logro.setTipo(TipoLogro.DIAS_SIN_FUMAR);
        logro.setValorObjetivo(5);
        logro.setExperienciaOtorgada(20);

        when(logroRepository.findAll()).thenReturn(List.of(logro));
        when(usuarioLogroRepository.findByUsuarioAndLogro(usuario, logro))
                .thenReturn(Optional.empty());

        // Act
        service.evaluarLogrosParaUsuario(usuario);

        // Assert: se creó nuevo UsuarioLogro y se guardó
        ArgumentCaptor<UsuarioLogro> cap = ArgumentCaptor.forClass(UsuarioLogro.class);
        verify(usuarioLogroRepository).save(cap.capture());
        UsuarioLogro saved = cap.getValue();

        assertTrue(saved.isCompletado(), "Debe marcar completado");
        assertNotNull(saved.getFechaAlcanzado(), "Fecha alcanzado debe estar seteada");

        // Verificamos recompensa
        assertEquals(120, usuario.getExperiencia());
        verify(nivelService).actualizarNivel(usuario);
        verify(usuarioRepository).save(usuario);
    }

    @Test
    void evaluarLogrosParaUsuario_logroNoCumplido_noAplicaExperiencia() {
        // Arrange
        Usuario usuario = new Usuario();
        usuario.setProgreso(new Progreso());
        usuario.getProgreso().setDiasSinFumar(2);
        usuario.setExperiencia(50);

        Logro logro = new Logro();
        logro.setTipo(TipoLogro.DIAS_SIN_FUMAR);
        logro.setValorObjetivo(5);
        logro.setExperienciaOtorgada(15);

        when(logroRepository.findAll()).thenReturn(List.of(logro));
        when(usuarioLogroRepository.findByUsuarioAndLogro(usuario, logro))
                .thenReturn(Optional.empty());

        // Act
        service.evaluarLogrosParaUsuario(usuario);

        // Assert: no se suma experiencia ni se invoca nivelService
        assertEquals(50, usuario.getExperiencia());
        verify(nivelService, never()).actualizarNivel(any());
        verify(usuarioRepository, never()).save(any());

        // Pero sí se guarda el progreso en UsuarioLogro
        verify(usuarioLogroRepository).save(any(UsuarioLogro.class));
    }

    @Test
    void obtenerLogros_usuarioNoExiste_lanzaEntityNotFound() {
        // Arrange
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException ex = assertThrows(
                EntityNotFoundException.class,
                () -> service.obtenerLogros(1L)
        );
        assertTrue(ex.getMessage().contains("Usuario no encontrado"));
    }

    @Test
    void obtenerLogros_creaNuevosLogrosYSoloGuardaLosInexistentes() {
        // Arrange
        Usuario usuario = new Usuario();
        usuario.setProgreso(new Progreso());
        when(usuarioRepository.findById(2L)).thenReturn(Optional.of(usuario));

        Logro l1 = new Logro(); l1.setTipo(TipoLogro.DIAS_SIN_FUMAR); l1.setValorObjetivo(0);
        Logro l2 = new Logro(); l2.setTipo(TipoLogro.CHECKIN_SEGUIDO); l2.setValorObjetivo(0);

        when(logroRepository.findAll()).thenReturn(List.of(l1, l2));
        // l1 ya existe
        UsuarioLogro existing = new UsuarioLogro(usuario, l1);
        when(usuarioLogroRepository.findByUsuarioAndLogro(usuario, l1))
                .thenReturn(Optional.of(existing));
        // l2 no existe => se crea y guarda
        when(usuarioLogroRepository.findByUsuarioAndLogro(usuario, l2))
                .thenReturn(Optional.empty());
        // al guardar devolvemos la instancia
        when(usuarioLogroRepository.save(any(UsuarioLogro.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        // Act
        List<UsuarioLogro> list = service.obtenerLogros(2L);

        // Assert
        assertEquals(2, list.size());
        verify(usuarioLogroRepository, times(1)).save(any(UsuarioLogro.class));
    }

    @Test
    void marcarComoReclamado_primeraVez_sumaExperienciaYGuarda() {
        // Arrange
        Usuario usuario = new Usuario();
        usuario.setProgreso(new Progreso());
        usuario.setExperiencia(30);

        Logro logro = new Logro();
        logro.setTipo(TipoLogro.DIAS_SIN_FUMAR);
        logro.setValorObjetivo(0);
        logro.setExperienciaOtorgada(5);

        UsuarioLogro ul = new UsuarioLogro(usuario, logro);
        ul.setCompletado(true);
        ul.setReclamado(false);

        when(usuarioLogroRepository.findById(10L)).thenReturn(Optional.of(ul));

        // Act
        service.marcarComoReclamado(10L);

        // Assert: se marcó reclamado y se guardó
        assertTrue(ul.isReclamado());
        assertEquals(35, usuario.getExperiencia());
        verify(nivelService).actualizarNivel(usuario);
        verify(usuarioRepository).save(usuario);
        verify(usuarioLogroRepository).save(ul);
    }

    @Test
    void marcarComoReclamado_segundaVez_noHaceNada() {
        // Arrange: ya reclamado
        UsuarioLogro ul = mock(UsuarioLogro.class);
        when(ul.isReclamado()).thenReturn(true);
        when(usuarioLogroRepository.findById(11L)).thenReturn(Optional.of(ul));

        // Act
        service.marcarComoReclamado(11L);

        // Assert: no invoca repos ni servicios
        verify(nivelService, never()).actualizarNivel(any());
        verify(usuarioRepository, never()).save(any());
        verify(usuarioLogroRepository, never()).save(any());
    }

    @Test
    void evaluarLogrosParaUsuarioPorId_delegacionCorrecta() {
        // Arrange
        Usuario usuario = new Usuario();
        when(usuarioRepository.findById(3L)).thenReturn(Optional.of(usuario));

        // Spy parcial para verificar llamada interna
        LogroService spySvc = Mockito.spy(service);

        // Act
        spySvc.evaluarLogrosParaUsuarioPorId(3L);

        // Assert
        verify(spySvc).evaluarLogrosParaUsuario(usuario);
    }
}
