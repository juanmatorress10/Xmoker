package com.xmoker.comunidad.service;

import com.xmoker.comunidad.entity.PostGrupo;
import com.xmoker.comunidad.entity.GrupoApoyo;
import com.xmoker.comunidad.repository.*;
import com.xmoker.user.entity.Usuario;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostGrupoServiceTest {

    @Mock PostGrupoRepository postRepo;
    @Mock ComentarioGrupoRepository commentRepo;
    @InjectMocks PostGrupoService service;

    @Test
    void crearPost_sinImagen_guardaCorrecto() {
        // Arrange
        Usuario u = new Usuario(); GrupoApoyo g = new GrupoApoyo();
        when(postRepo.save(any())).thenAnswer(i -> i.getArgument(0));

        // Act
        PostGrupo p = service.crearPost(u, g, "Hola", null);

        // Assert
        assertEquals(u, p.getAutor());
        assertEquals(g, p.getGrupo());
        assertEquals("Hola", p.getContenido());
        assertNull(p.getImagenUrl());
    }

    @Test
    void obtenerPostsPorGrupo_delegaEnRepo() {
        GrupoApoyo g = new GrupoApoyo();
        List<PostGrupo> list = List.of(new PostGrupo());
        when(postRepo.findByGrupoOrderByFechaPublicacionDesc(g)).thenReturn(list);

        assertSame(list, service.obtenerPostsPorGrupo(g));
    }

    @Test
    void buscarPorId_noExiste_lanza() {
        when(postRepo.findById(4L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class,
                () -> service.buscarPorId(4L));
    }

    @Test
    void eliminarPost_autorCorrecto_eliminaComentariosYPost() {
        Usuario u = new Usuario(); u.setId(1L);
        PostGrupo p = new PostGrupo(); p.setAutor(u);
        when(postRepo.findById(2L)).thenReturn(Optional.of(p));

        // Act
        service.eliminarPost(2L, u);

        // Assert: primero comentarios, luego post
        InOrder ord = inOrder(commentRepo, postRepo);
        ord.verify(commentRepo).deleteByPost(p);
        ord.verify(postRepo).delete(p);
    }

    @Test
    void eliminarPost_otroAutor_lanzaSecurity() {
        PostGrupo p = new PostGrupo(); p.setAutor(new Usuario()); p.getAutor().setId(2L);
        when(postRepo.findById(3L)).thenReturn(Optional.of(p));
        assertThrows(SecurityException.class,
                () -> service.eliminarPost(3L, new Usuario()));
    }

    @Test
    void delete_delegaEnRepoDeleteById() {
        service.delete(99L);
        verify(postRepo).deleteById(99L);
    }
}
