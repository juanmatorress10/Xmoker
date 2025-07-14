package com.xmoker.comunidad.controller;

import com.xmoker.comunidad.dto.ComentarioDTO;
import com.xmoker.comunidad.entity.ComentarioGrupo;
import com.xmoker.comunidad.entity.PostGrupo;
import com.xmoker.comunidad.service.ComentarioGrupoService;
import com.xmoker.comunidad.service.PostGrupoService;
import com.xmoker.user.entity.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comentarios")
public class ComentarioGrupoController {

    @Autowired
    private ComentarioGrupoService comentarioService;

    @Autowired
    private PostGrupoService postService;

    @PostMapping("/{postId}")
    public ResponseEntity<ComentarioDTO> comentar(
            @PathVariable Long postId,
            @RequestParam String contenido,
            @AuthenticationPrincipal Usuario usuario) {

        PostGrupo post = postService.buscarPorId(postId);
        ComentarioGrupo comentario = comentarioService.crearComentario(post, usuario, contenido);
        return ResponseEntity.ok(new ComentarioDTO(comentario));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<List<ComentarioDTO>> obtenerComentarios(@PathVariable Long postId) {
        PostGrupo post = postService.buscarPorId(postId);
        List<ComentarioDTO> dtos = comentarioService.obtenerComentarios(post).stream()
                .map(ComentarioDTO::new)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @DeleteMapping("/eliminar/{comentarioId}")
    public ResponseEntity<?> eliminarComentario(
            @PathVariable Long comentarioId,
            @AuthenticationPrincipal Usuario usuario) {

        comentarioService.eliminarComentario(comentarioId, usuario);
        return ResponseEntity.ok("Comentario eliminado");
    }
}
