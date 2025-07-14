package com.xmoker.comunidad.service;

import com.xmoker.comunidad.entity.*;
import com.xmoker.comunidad.repository.*;
import com.xmoker.user.entity.Usuario;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GrupoServiceTest {

    @Mock GrupoApoyoRepository grupoRepo;
    @Mock UsuarioGrupoRepository usuarioGrupoRepo;
    @Mock PostGrupoRepository postRepo;
    @Mock ComentarioGrupoRepository comentarioRepo;
    @InjectMocks GrupoService service;

    @Test
    void crearGrupo_exito_guardadoYRelacion() {
        // Arrange: usuario sin grupos previos
        Usuario creador = new Usuario(); creador.setId(1L);
        when(usuarioGrupoRepo.findByUsuario(creador)).thenReturn(List.of());
        // save devuelve mismo objeto
        GrupoApoyo g = new GrupoApoyo();
        when(grupoRepo.save(any())).thenAnswer(i -> i.getArgument(0));

        // Act
        GrupoApoyo out = service.crearGrupo(g, creador);

        // Assert
        assertEquals(creador, out.getCreador());
        assertTrue(out.isEsPrivado());
        assertNotNull(out.getCodigoAcceso());
        verify(usuarioGrupoRepo).save(any(UsuarioGrupo.class));
    }

    @Test
    void crearGrupo_siYaMiembro_lanzaIllegalState() {
        // Arrange
        Usuario u = new Usuario();
        when(usuarioGrupoRepo.findByUsuario(u)).thenReturn(List.of(new UsuarioGrupo()));

        // Act-Assert
        assertThrows(IllegalStateException.class,
                () -> service.crearGrupo(new GrupoApoyo(), u));
    }

    @Test
    void buscarPorId_noExiste_lanza() {
        when(grupoRepo.findById(5L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class,
                () -> service.buscarPorId(5L));
    }

    @Test
    void listarGruposPublicos_delegaEnRepo() {
        List<GrupoApoyo> pubs = List.of(new GrupoApoyo());
        when(grupoRepo.findByEsPrivadoFalse()).thenReturn(pubs);
        assertSame(pubs, service.listarGruposPublicos());
    }

    @Test
    void eliminarGrupoSiEsCreador_exito_eliminaDependencias() {
        // Arrange
        Usuario u = new Usuario(); u.setId(3L);
        GrupoApoyo g = new GrupoApoyo(); g.setCreador(u);
        when(grupoRepo.findById(9L)).thenReturn(Optional.of(g));

        // Act
        service.eliminarGrupoSiEsCreador(9L, u);

        // Assert: orden de borrado
        InOrder ord = inOrder(usuarioGrupoRepo, postRepo, grupoRepo);
        ord.verify(usuarioGrupoRepo).deleteAllByGrupo(g);
        ord.verify(postRepo).deleteAllByGrupo(g);
        ord.verify(grupoRepo).delete(g);
    }

    @Test
    void eliminarGrupoSiEsCreador_noCreador_lanzaSecurity() {
        Usuario otro = new Usuario(); otro.setId(7L);
        GrupoApoyo g = new GrupoApoyo(); g.setCreador(new Usuario()); g.getCreador().setId(8L);
        when(grupoRepo.findById(2L)).thenReturn(Optional.of(g));

        assertThrows(SecurityException.class,
                () -> service.eliminarGrupoSiEsCreador(2L, otro));
    }
}
