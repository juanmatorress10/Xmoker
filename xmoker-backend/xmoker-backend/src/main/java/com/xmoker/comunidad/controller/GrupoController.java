package com.xmoker.comunidad.controller;

import com.xmoker.comunidad.dto.MiembroDTO;
import com.xmoker.comunidad.dto.PostGrupoDTO;
import com.xmoker.comunidad.entity.GrupoApoyo;
import com.xmoker.comunidad.entity.PostGrupo;
import com.xmoker.comunidad.repository.UsuarioGrupoRepository;
import com.xmoker.comunidad.service.GrupoService;
import com.xmoker.comunidad.service.PostGrupoService;
import com.xmoker.user.entity.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/grupos")
@CrossOrigin(origins = "http://localhost:4200")
public class GrupoController {

    @Autowired
    private GrupoService grupoService;

    @Autowired
    private UsuarioGrupoRepository usuarioGrupoRepo;

    @Autowired
    private PostGrupoService postService;
    @PostMapping
    public ResponseEntity<GrupoApoyo> crearGrupo(
            @RequestBody GrupoApoyo grupo,
            @AuthenticationPrincipal Usuario usuario) {
        return ResponseEntity.ok(grupoService.crearGrupo(grupo, usuario));
    }

    @GetMapping("/publicos")
    public List<GrupoApoyo> listarGruposPublicos() {
        return grupoService.listarGruposPublicos();
    }

    @GetMapping("/{grupoId}")
    public ResponseEntity<List<PostGrupoDTO>> obtenerPosts(@PathVariable Long grupoId) {
        GrupoApoyo grupo = grupoService.buscarPorId(grupoId);
        List<PostGrupo> posts = postService.obtenerPostsPorGrupo(grupo);

        List<PostGrupoDTO> postDTOs = posts.stream()
                .map(PostGrupoDTO::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(postDTOs);
    }


    @GetMapping("/{grupoId}/miembros")
    public List<MiembroDTO> listarMiembros(@PathVariable Long grupoId) {
        GrupoApoyo grupo = grupoService.buscarPorId(grupoId);
        return usuarioGrupoRepo.findByGrupo(grupo).stream()
                .map(ug -> {
                    var u = ug.getUsuario();
                    return new MiembroDTO(u.getId(), u.getNombre());
                })
                .toList();
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminarGrupo(@PathVariable Long id, @AuthenticationPrincipal Usuario usuario) {
        grupoService.eliminarGrupoSiEsCreador(id, usuario);
        return ResponseEntity.ok("Grupo eliminado correctamente");
    }


}
