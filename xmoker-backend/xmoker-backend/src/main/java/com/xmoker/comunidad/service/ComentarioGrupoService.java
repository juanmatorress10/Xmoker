package com.xmoker.comunidad.service;

import com.xmoker.comunidad.entity.ComentarioGrupo;
import com.xmoker.comunidad.entity.PostGrupo;
import com.xmoker.comunidad.repository.ComentarioGrupoRepository;
import com.xmoker.user.entity.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ComentarioGrupoService {

    @Autowired
    private ComentarioGrupoRepository comentarioRepo;

    public ComentarioGrupo crearComentario(PostGrupo post, Usuario autor, String contenido) {
        ComentarioGrupo comentario = new ComentarioGrupo();
        comentario.setPost(post);
        comentario.setAutor(autor);
        comentario.setContenido(contenido);
        return comentarioRepo.save(comentario);
    }

    public List<ComentarioGrupo> obtenerComentarios(PostGrupo post) {
        return comentarioRepo.findByPostOrderByFechaComentarioAsc(post);
    }

    public void eliminarComentario(Long comentarioId, Usuario usuario) {
        ComentarioGrupo comentario = comentarioRepo.findById(comentarioId)
                .orElseThrow(() -> new IllegalArgumentException("Comentario no encontrado"));

        if (!comentario.getAutor().getId().equals(usuario.getId())) {
            throw new SecurityException("No tienes permiso para eliminar este comentario");
        }

        comentarioRepo.delete(comentario);
    }

}
