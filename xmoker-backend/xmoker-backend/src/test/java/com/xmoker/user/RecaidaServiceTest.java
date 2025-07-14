package com.xmoker.user.service;

import com.xmoker.comunidad.entity.EstadoRetoUsuario;
import com.xmoker.comunidad.entity.GrupoApoyo;
import com.xmoker.comunidad.entity.PostGrupo;
import com.xmoker.comunidad.entity.UsuarioGrupo;
import com.xmoker.comunidad.entity.UsuarioRetoGrupo;
import com.xmoker.logro.entity.Logro;
import com.xmoker.logro.entity.TipoLogro;
import com.xmoker.logro.entity.UsuarioLogro;
import com.xmoker.logro.repository.UsuarioLogroRepository;
import com.xmoker.user.entity.Progreso;
import com.xmoker.user.entity.Recaida;
import com.xmoker.user.entity.Usuario;
import com.xmoker.user.repository.RecaidaRepository;
import com.xmoker.user.repository.UserRepository;
import com.xmoker.user.service.RecaidaService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecaidaServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private RecaidaRepository recaidaRepository;
    @Mock private UsuarioLogroRepository usuarioLogroRepository;
    @Mock private com.xmoker.comunidad.repository.PostGrupoRepository postGrupoRepository;
    @Mock private com.xmoker.comunidad.repository.UsuarioGrupoRepository usuarioGrupoRepository;
    @Mock private com.xmoker.comunidad.repository.UsuarioRetoGrupoRepository usuarioRetoGrupoRepository;

    @InjectMocks private RecaidaService recaidaService;

    @Test
    void registrarRecaida_flujoCompleto_verificaGuardadosYReseteos() {
        // Arrange: creamos usuario con progreso inicial
        Progreso progreso = new Progreso();
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setProgreso(progreso);

        // Preparamos una recaída con motivo
        Recaida r = new Recaida();
        r.setMotivo("estrés");

        // Un logro que debe reiniciarse al recaer
        Logro logro = new Logro();
        logro.setTipo(TipoLogro.DIAS_SIN_FUMAR);
        UsuarioLogro ul = new UsuarioLogro();
        ul.setLogro(logro);
        ul.setUsuario(usuario);

        // Un grupo al que pertenece el usuario
        GrupoApoyo g = new GrupoApoyo();
        UsuarioGrupo ug = new UsuarioGrupo();
        ug.setUsuario(usuario);
        ug.setGrupo(g);

        // Un reto activo (fechaFin como LocalDate)
        UsuarioRetoGrupo urg = new UsuarioRetoGrupo();
        urg.setUsuario(usuario);
        urg.setEstado(EstadoRetoUsuario.NO_COMPLETADO);
        urg.setReto(new com.xmoker.comunidad.entity.RetoGrupo() {{
            setFechaFin(LocalDate.now().plusDays(1));
        }});

        // Simulamos las llamadas a repositorios
        when(userRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioLogroRepository.findByUsuarioId(1L)).thenReturn(List.of(ul));
        when(usuarioGrupoRepository.findByUsuario(usuario)).thenReturn(List.of(ug));
        when(usuarioRetoGrupoRepository.findAll()).thenReturn(List.of(urg));

        // Act: ejecutamos el método bajo prueba
        recaidaService.registrarRecaida(1L, r);

        // Assert: comprobamos que se guardaron entidades y se resetearon campos
        verify(recaidaRepository, times(1)).save(any(Recaida.class));
        verify(usuarioLogroRepository, times(1)).save(any(UsuarioLogro.class));
        verify(postGrupoRepository, times(1)).save(any(PostGrupo.class));
        verify(usuarioRetoGrupoRepository, times(1)).save(any(UsuarioRetoGrupo.class));
        verify(userRepository, times(1)).save(usuario);

        // Verificamos algunos resets en el progreso y fecha de inicio
        assertEquals(0, progreso.getDiasSinFumar());
        assertEquals("0 kg", progreso.getCO2NoEmitido());
        assertNotNull(usuario.getFechaInicioProceso());
    }
}
