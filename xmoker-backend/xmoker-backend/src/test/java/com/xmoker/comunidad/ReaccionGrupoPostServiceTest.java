package com.xmoker.comunidad.service;

import com.xmoker.comunidad.entity.PostGrupo;
import com.xmoker.comunidad.entity.ReaccionGrupoPost;
import com.xmoker.comunidad.entity.TipoReaccion;
import com.xmoker.comunidad.repository.ReaccionGrupoPostRepository;
import com.xmoker.user.entity.Usuario;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReaccionGrupoPostServiceTest {

    @Mock
    private ReaccionGrupoPostRepository repo;

    @InjectMocks
    private ReaccionGrupoPostService service;

    @Test
    void reaccionar_yaExistente_actualizaTipo() {
        // Arrange: ya existe una reacción LIKE
        Usuario u = new Usuario();
        PostGrupo post = new PostGrupo();
        ReaccionGrupoPost existing = new ReaccionGrupoPost();
        existing.setTipo(TipoReaccion.LIKE);
        when(repo.findByUsuarioAndPost(u, post)).thenReturn(Optional.of(existing));
        when(repo.save(any())).thenAnswer(i -> i.getArgument(0));

        // Act: cambiamos a tipo APOYO
        ReaccionGrupoPost out = service.reaccionar(u, post, TipoReaccion.APOYO);

        // Assert: se actualiza el tipo y se guarda la misma entidad
        assertEquals(TipoReaccion.APOYO, out.getTipo());
        verify(repo).save(existing);
    }

    @Test
    void reaccionar_nueva_creaYGuarda() {
        // Arrange: no existe reacción previa
        Usuario u = new Usuario();
        PostGrupo post = new PostGrupo();
        when(repo.findByUsuarioAndPost(u, post)).thenReturn(Optional.empty());
        when(repo.save(any())).thenAnswer(i -> i.getArgument(0));

        // Act: creamos una nueva reacción FUEGUITO
        ReaccionGrupoPost out = service.reaccionar(u, post, TipoReaccion.FUEGUITO);

        // Assert: se inicializan campos correctamente
        assertEquals(u, out.getUsuario());
        assertEquals(post, out.getPost());
        assertEquals(TipoReaccion.FUEGUITO, out.getTipo());
    }

    @Test
    void obtenerReacciones_delegaEnRepo() {
        // Arrange
        PostGrupo p = new PostGrupo();
        List<ReaccionGrupoPost> list = List.of(new ReaccionGrupoPost());
        when(repo.findByPost(p)).thenReturn(list);

        // Act
        List<ReaccionGrupoPost> result = service.obtenerReacciones(p);

        // Assert: se delega directamente al repositorio
        assertSame(list, result);
        verify(repo).findByPost(p);
    }
}
