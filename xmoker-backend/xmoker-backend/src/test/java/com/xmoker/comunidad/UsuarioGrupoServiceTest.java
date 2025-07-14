package com.xmoker.comunidad.service;

import com.xmoker.comunidad.entity.*;
import com.xmoker.comunidad.repository.*;
import com.xmoker.user.entity.Usuario;
import com.xmoker.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioGrupoServiceTest {

    @Mock GrupoApoyoRepository grupoRepo;
    @Mock UserRepository userRepo;
    @Mock UsuarioGrupoRepository urRepo;
    @InjectMocks UsuarioGrupoService service;

    @Test
    void unirseAGrupo_exito() {
        Usuario u = new Usuario(); GrupoApoyo g = new GrupoApoyo();
        when(urRepo.findByUsuarioAndGrupo(u, g)).thenReturn(Optional.empty());
        when(urRepo.save(any())).thenAnswer(i -> i.getArgument(0));

        UsuarioGrupo out = service.unirseAGrupo(u, g);
        assertFalse(out.isEsModerador());
        assertEquals(u, out.getUsuario());
    }

    @Test
    void unirseAGrupo_siYaMiembro_lanza() {
        Usuario u = new Usuario(); GrupoApoyo g = new GrupoApoyo();
        when(urRepo.findByUsuarioAndGrupo(u, g))
                .thenReturn(Optional.of(new UsuarioGrupo()));

        assertThrows(IllegalStateException.class,
                () -> service.unirseAGrupo(u, g));
    }

    @Test
    void gruposDeUsuario_delegaEnRepo() {
        Usuario u = new Usuario();
        List<UsuarioGrupo> l = List.of(new UsuarioGrupo());
        when(urRepo.findByUsuario(u)).thenReturn(l);
        assertSame(l, service.gruposDeUsuario(u));
    }

    @Test
    void esMiembro_devuelveTrueFalse() {
        Usuario u = new Usuario(); GrupoApoyo g = new GrupoApoyo();
        when(urRepo.findByUsuarioAndGrupo(u, g))
                .thenReturn(Optional.of(new UsuarioGrupo()));
        assertTrue(service.esMiembro(u, g));
        when(urRepo.findByUsuarioAndGrupo(u, g))
                .thenReturn(Optional.empty());
        assertFalse(service.esMiembro(u, g));
    }

    @Test
    void unirsePorCodigo_exito() {
        Usuario u = new Usuario(); GrupoApoyo g = new GrupoApoyo(); g.setCodigoAcceso("ABC");
        when(grupoRepo.findByCodigoAcceso("ABC")).thenReturn(Optional.of(g));
        when(urRepo.findByUsuarioAndGrupo(u, g)).thenReturn(Optional.empty());
        when(urRepo.save(any())).thenAnswer(i -> i.getArgument(0));

        UsuarioGrupo out = service.unirsePorCodigo("ABC", u);
        assertEquals(g, out.getGrupo());
    }

    @Test
    void eliminarUsuarioDelGrupo_exito() {
        GrupoApoyo g = new GrupoApoyo(); g.setId(9L);
        Usuario u = new Usuario(); u.setId(7L);
        UsuarioGrupo rel = new UsuarioGrupo(); rel.setUsuario(u); rel.setGrupo(g);

        when(grupoRepo.findById(9L)).thenReturn(Optional.of(g));
        when(userRepo.findById(7L)).thenReturn(Optional.of(u));
        when(urRepo.findByUsuarioAndGrupo(u, g)).thenReturn(Optional.of(rel));

        service.eliminarUsuarioDelGrupo(9L, 7L);
        verify(urRepo).delete(rel);
    }

    @Test
    void listarMiembros_devuelveSoloUsuarios() {
        GrupoApoyo g = new GrupoApoyo(); g.setId(5L);
        Usuario u = new Usuario(); u.setId(2L);
        UsuarioGrupo rel = new UsuarioGrupo(); rel.setUsuario(u);
        when(grupoRepo.findById(5L)).thenReturn(Optional.of(g));
        when(urRepo.findByGrupo(g)).thenReturn(List.of(rel));

        List<com.xmoker.user.entity.Usuario> members = service.listarMiembros(5L);
        assertEquals(1, members.size());
        assertEquals(2L, members.get(0).getId());
    }

    @Test
    void abandonarGrupo_siMiembro_elimina() {
        Usuario u = new Usuario(); u.setId(4L);
        GrupoApoyo g = new GrupoApoyo(); g.setId(6L);
        UsuarioGrupo rel = new UsuarioGrupo(); rel.setUsuario(u); rel.setGrupo(g);

        when(grupoRepo.findById(6L)).thenReturn(Optional.of(g));
        when(urRepo.findByUsuarioAndGrupo(u, g)).thenReturn(Optional.of(rel));

        service.abandonarGrupo(u, 6L);
        verify(urRepo).delete(rel);
    }
}
