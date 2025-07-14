package com.xmoker.comunidad.controller;

import com.xmoker.comunidad.dto.ReaccionDTO;
import com.xmoker.comunidad.entity.PostGrupo;
import com.xmoker.comunidad.entity.ReaccionGrupoPost;
import com.xmoker.comunidad.entity.TipoReaccion;
import com.xmoker.comunidad.service.PostGrupoService;
import com.xmoker.comunidad.service.ReaccionGrupoPostService;
import com.xmoker.user.entity.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// src/main/java/com/xmoker/comunidad/controller/ReaccionGrupoPostController.java
@RestController
@RequestMapping("/api/reacciones")
@CrossOrigin(origins = "http://localhost:4200")
public class ReaccionGrupoPostController {
    @Autowired private ReaccionGrupoPostService reaccionService;
    @Autowired private PostGrupoService postService;

    @PostMapping("/{postId}")
    public ResponseEntity<ReaccionDTO> reaccionar(
            @PathVariable Long postId,
            @RequestParam TipoReaccion tipo,
            @AuthenticationPrincipal Usuario usuario) {

        PostGrupo post = postService.buscarPorId(postId);
        ReaccionGrupoPost r = reaccionService.reaccionar(usuario, post, tipo);
        return ResponseEntity.ok(new ReaccionDTO(r));
    }

    @GetMapping("/{postId}")
    public List<ReaccionDTO> listarReacciones(@PathVariable Long postId) {
        PostGrupo post = postService.buscarPorId(postId);
        return reaccionService.obtenerReacciones(post)
                .stream().map(ReaccionDTO::new).toList();
    }
}

