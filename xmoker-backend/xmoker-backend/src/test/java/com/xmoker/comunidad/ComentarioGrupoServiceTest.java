package com.xmoker.comunidad.service;

import com.xmoker.comunidad.entity.ComentarioGrupo;
import com.xmoker.comunidad.entity.PostGrupo;
import com.xmoker.comunidad.repository.ComentarioGrupoRepository;
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
class ComentarioGrupoServiceTest {

    @Mock ComentarioGrupoRepository repo;
    @InjectMocks ComentarioGrupoService service;

    @Test
    void crearComentario_guardaYDevuelve() {
        // Arrange
        PostGrupo post = new PostGrupo();
        Usuario autor = new Usuario(); autor.setId(5L);
        String contenido = "¡Hola!";
        when(repo.save(any())).thenAnswer(inv -> inv.getArgument(0));

        // Act
        ComentarioGrupo c = service.crearComentario(post, autor, contenido);

        // Assert
        assertEquals(post,   c.getPost());
        assertEquals(autor,  c.getAutor());
        assertEquals(contenido, c.getContenido());
        verify(repo).save(c);
    }

    @Test
    void obtenerComentarios_delegaEnRepo() {
        // Arrange
        PostGrupo post = new PostGrupo();
        List<ComentarioGrupo> lista = List.of(new ComentarioGrupo(), new ComentarioGrupo());
        when(repo.findByPostOrderByFechaComentarioAsc(post)).thenReturn(lista);

        // Act
        List<ComentarioGrupo> out = service.obtenerComentarios(post);

        // Assert
        assertSame(lista, out);
        verify(repo).findByPostOrderByFechaComentarioAsc(post);
    }

    @Test
    void eliminarComentario_noExiste_lanzaIllegalArgument() {
        // Arrange
        when(repo.findById(1L)).thenReturn(Optional.empty());

        // Act-Assert
        assertThrows(IllegalArgumentException.class,
                () -> service.eliminarComentario(1L, new Usuario()));
    }

    @Test
    void eliminarComentario_otroAutor_lanzaSecurityException() {
        // Arrange
        ComentarioGrupo com = new ComentarioGrupo();
        com.setAutor(new Usuario()); com.getAutor().setId(2L);
        when(repo.findById(10L)).thenReturn(Optional.of(com));
        Usuario distinto = new Usuario(); distinto.setId(99L);

        // Act-Assert
        assertThrows(SecurityException.class,
                () -> service.eliminarComentario(10L, distinto));
    }

    @Test
    void eliminarComentario_mismoAutor_elimina() {
        // Arrange
        Usuario u = new Usuario(); u.setId(3L);
        ComentarioGrupo com = new ComentarioGrupo();
        com.setAutor(u);
        when(repo.findById(7L)).thenReturn(Optional.of(com));

        // Act
        service.eliminarComentario(7L, u);

        // Assert
        verify(repo).delete(com);
    }
}
