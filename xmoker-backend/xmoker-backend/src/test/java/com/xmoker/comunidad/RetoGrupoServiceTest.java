package com.xmoker.comunidad.service;

import com.xmoker.comunidad.dto.RetoResumenDTO;
import com.xmoker.comunidad.dto.UsuarioEnRetoDTO;
import com.xmoker.comunidad.entity.GrupoApoyo;
import com.xmoker.comunidad.entity.RetoGrupo;
import com.xmoker.comunidad.entity.UsuarioRetoGrupo;
import com.xmoker.comunidad.entity.EstadoRetoUsuario;
import com.xmoker.comunidad.repository.RetoGrupoRepository;
import com.xmoker.comunidad.repository.UsuarioRetoGrupoRepository;
import com.xmoker.user.entity.Usuario;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RetoGrupoServiceTest {

    @Mock private RetoGrupoRepository retoRepo;
    @Mock private UsuarioRetoGrupoRepository usuarioRetoRepo;
    @InjectMocks private RetoGrupoService service;

    @Test
    void crearReto_exito_guardado() {
        // Arrange
        RetoGrupo reto = new RetoGrupo();
        when(retoRepo.save(any())).thenAnswer(inv -> inv.getArgument(0));

        // Act
        RetoGrupo out = service.crearReto(reto, new GrupoApoyo(), new Usuario());

        // Assert
        assertNotNull(out.getGrupo());
        assertNotNull(out.getCreador());
        verify(retoRepo).save(reto);
    }

    @Test
    void crearRetoYObtenerResumen_delegacion() {
        // Arrange
        RetoGrupo reto = new RetoGrupo();
        reto.setId(8L);
        when(retoRepo.save(any())).thenReturn(reto);
        // Creamos DTO con todos los parámetros del constructor
        RetoResumenDTO dto = new RetoResumenDTO(
                8L,
                "título",
                "descripción",
                "instrucciones",
                0,
                0,
                0.0,
                Collections.<UsuarioEnRetoDTO>emptyList(),
                ""
        );
        RetoGrupoService spy = spy(service);
        doReturn(dto).when(spy).obtenerResumenDelReto(8L);

        // Act
        RetoResumenDTO out = spy.crearRetoYObtenerResumen(reto, new GrupoApoyo(), new Usuario());

        // Assert
        assertSame(dto, out);
    }

    @Test
    void listarRetosPorGrupoDTO_mappeaCorrecto() {
        // Arrange
        GrupoApoyo g = new GrupoApoyo();
        RetoGrupo r = new RetoGrupo();
        r.setId(1L);
        r.setTitulo("T");
        r.setDescripcion("D");
        r.setFechaInicio(LocalDate.of(2025, 1, 1));
        r.setFechaFin(LocalDate.of(2025, 12, 1));
        when(retoRepo.findByGrupo(g)).thenReturn(List.of(r));

        // Act
        var list = service.listarRetosPorGrupoDTO(g);

        // Assert
        assertEquals(1, list.size());
        assertEquals("T", list.get(0).getTitulo());
    }

    @Test
    void listarRetosActivosDTO_filtraPorFecha() {
        // Arrange
        GrupoApoyo g = new GrupoApoyo();
        RetoGrupo past = new RetoGrupo();
        past.setFechaInicio(LocalDate.of(2025, 1, 1));
        past.setFechaFin(LocalDate.now().minusDays(1));
        RetoGrupo future = new RetoGrupo();
        future.setFechaInicio(LocalDate.of(2025, 1, 1));
        future.setFechaFin(LocalDate.now().plusDays(1));
        when(retoRepo.findByGrupoAndFechaFinAfter(g, LocalDate.now()))
                .thenReturn(List.of(future));

        // Act
        var list = service.listarRetosActivosDTO(g);

        // Assert
        assertEquals(1, list.size());
    }

    @Test
    void verMisRetos_filtraPorUsuario() {
        // Arrange
        Usuario u = new Usuario(); u.setId(20L);
        UsuarioRetoGrupo a = new UsuarioRetoGrupo();
        a.setUsuario(u);
        UsuarioRetoGrupo b = new UsuarioRetoGrupo();
        Usuario other = new Usuario(); other.setId(99L);
        b.setUsuario(other);
        when(usuarioRetoRepo.findAll()).thenReturn(List.of(a, b));

        // Act
        var out = service.verMisRetos(u);

        // Assert
        assertEquals(1, out.size());
        assertEquals(20L, out.get(0).getUsuario().getId());
    }

    @Test
    void unirseAReto_nuevo_guarda() {
        // Arrange
        Usuario u = new Usuario();
        RetoGrupo rg = new RetoGrupo(); rg.setId(5L);
        when(retoRepo.findById(5L)).thenReturn(Optional.of(rg));
        when(usuarioRetoRepo.findByUsuarioAndReto(u, rg)).thenReturn(Optional.empty());
        when(usuarioRetoRepo.save(any())).thenAnswer(inv -> inv.getArgument(0));

        // Act
        UsuarioRetoGrupo out = service.unirseAReto(u, rg);

        // Assert
        assertEquals(EstadoRetoUsuario.PENDIENTE, out.getEstado());
    }

    @Test
    void unirseAReto_existente_noDuplica() {
        // Arrange
        Usuario u = new Usuario();
        RetoGrupo rg = new RetoGrupo(); rg.setId(6L);
        UsuarioRetoGrupo existing = new UsuarioRetoGrupo();
        when(retoRepo.findById(6L)).thenReturn(Optional.of(rg));
        when(usuarioRetoRepo.findByUsuarioAndReto(u, rg))
                .thenReturn(Optional.of(existing));

        // Act
        UsuarioRetoGrupo out = service.unirseAReto(u, rg);

        // Assert
        assertSame(existing, out);
    }

    @Test
    void marcarCompletado_cambiaEstado() {
        // Arrange
        Usuario u = new Usuario();
        RetoGrupo rg = new RetoGrupo(); rg.setId(2L);
        UsuarioRetoGrupo ur = new UsuarioRetoGrupo();
        ur.setUsuario(u);
        ur.setEstado(EstadoRetoUsuario.PENDIENTE);
        when(usuarioRetoRepo.findByUsuarioAndReto(u, rg)).thenReturn(Optional.of(ur));

        // Act
        service.marcarCompletado(u, rg);

        // Assert
        assertEquals(EstadoRetoUsuario.COMPLETADO, ur.getEstado());
        verify(usuarioRetoRepo).save(ur);
    }

    @Test
    void obtenerResumenDelReto_calculaCorrecto() {
        // Arrange
        RetoGrupo rg = new RetoGrupo();
        rg.setId(3L);
        rg.setFechaInicio(LocalDate.of(2025, 1, 1));
        rg.setFechaFin(LocalDate.of(2025, 1, 2));
        when(retoRepo.findById(3L)).thenReturn(Optional.of(rg));

        UsuarioRetoGrupo a = new UsuarioRetoGrupo();
        Usuario ua = new Usuario(); ua.setId(10L);
        a.setUsuario(ua);
        a.setEstado(EstadoRetoUsuario.COMPLETADO);

        UsuarioRetoGrupo b = new UsuarioRetoGrupo();
        Usuario ub = new Usuario(); ub.setId(20L);
        b.setUsuario(ub);
        b.setEstado(EstadoRetoUsuario.PENDIENTE);

        when(usuarioRetoRepo.findByReto(rg)).thenReturn(List.of(a, b));

        // Act
        RetoResumenDTO dto = service.obtenerResumenDelReto(3L);

        // Assert
        assertEquals(3L, dto.getRetoId());
        assertEquals(1, dto.getTotalCompletados());
        assertEquals(2, dto.getTotalParticipantes());
        assertEquals(50.0, dto.getPorcentajeCompletado());
    }
}
