package com.xmoker.comunidad.service;

import com.xmoker.comunidad.entity.PostGrupo;
import com.xmoker.comunidad.entity.GrupoApoyo;
import com.xmoker.comunidad.repository.ComentarioGrupoRepository;
import com.xmoker.comunidad.repository.PostGrupoRepository;
import com.xmoker.user.entity.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.UUID;

@Service
public class PostGrupoService {

    private final String RUTA_LOCAL = "/images/uploads/";

    @Autowired
    private PostGrupoRepository postRepo;

    @Autowired
    private ComentarioGrupoRepository comentarioGrupoRepo;

    public PostGrupo crearPost(Usuario autor, GrupoApoyo grupo, String contenido, MultipartFile imagen) {
        PostGrupo post = new PostGrupo();
        post.setAutor(autor);
        post.setGrupo(grupo);
        post.setContenido(contenido);

        if (imagen != null && !imagen.isEmpty()) {
            try {
                String url = guardarImagenEnLocal(imagen);
                post.setImagenUrl(url);
            } catch (IOException e) {
                throw new RuntimeException("Error al guardar imagen", e);
            }
        }

        return postRepo.save(post);
    }

    public List<PostGrupo> obtenerPostsPorGrupo(GrupoApoyo grupo) {
        return postRepo.findByGrupoOrderByFechaPublicacionDesc(grupo);
    }

    private String guardarImagenEnLocal(MultipartFile imagen) throws IOException {
        String nombre = UUID.randomUUID() + "_" + imagen.getOriginalFilename();
        Path destino = Paths.get(RUTA_LOCAL).resolve(nombre);
        Files.createDirectories(destino.getParent());
        Files.copy(imagen.getInputStream(), destino, StandardCopyOption.REPLACE_EXISTING);
        return "/uploads/" + nombre; // esto se usará como imagenUrl
    }

    public PostGrupo guardar(PostGrupo post) {
        return postRepo.save(post);
    }

    public PostGrupo buscarPorId(Long id) {
        return postRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Post no encontrado con id " + id));
    }



    public void eliminarPost(Long postId, Usuario usuario) {
        PostGrupo post = postRepo.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post no encontrado"));

        if (!post.getAutor().getId().equals(usuario.getId())) {
            throw new SecurityException("No tienes permiso para eliminar este post");
        }

        // ✅ Eliminar primero los comentarios del post para evitar conflicto de FK
        comentarioGrupoRepo.deleteByPost(post);

        // ✅ Ahora puedes eliminar el post sin error
        postRepo.delete(post);
    }



    public void delete(Long id) {
        postRepo.deleteById(id);
    }


}
