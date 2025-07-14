package com.xmoker.comunidad.controller;

import com.xmoker.comunidad.dto.UsuarioGrupoDTO;
import com.xmoker.comunidad.entity.GrupoApoyo;
import com.xmoker.comunidad.entity.UsuarioGrupo;
import com.xmoker.comunidad.service.GrupoService;
import com.xmoker.comunidad.service.UsuarioGrupoService;
import com.xmoker.user.entity.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/miembros")
public class UsuarioGrupoController {

    @Autowired
    private UsuarioGrupoService usuarioGrupoService;

    @Autowired
    private GrupoService grupoService;

    @PostMapping("/unirse/{grupoId}")
    public ResponseEntity<UsuarioGrupo> unirseAGrupo(
            @PathVariable Long grupoId,
            @AuthenticationPrincipal Usuario usuario) {
        GrupoApoyo grupo = grupoService.buscarPorId(grupoId);
        UsuarioGrupo miembro = usuarioGrupoService.unirseAGrupo(usuario, grupo);
        return ResponseEntity.ok(miembro);
    }

    @GetMapping("/mis-grupos")
    public ResponseEntity<List<UsuarioGrupoDTO>> obtenerGruposDelUsuario(
            @AuthenticationPrincipal Usuario usuario) {

        List<UsuarioGrupo> grupos = usuarioGrupoService.gruposDeUsuario(usuario);
        List<UsuarioGrupoDTO> dto = grupos.stream()
                .map(UsuarioGrupoDTO::new)
                .toList();

        return ResponseEntity.ok(dto);
    }

    @PostMapping("/unirse-codigo")
    public ResponseEntity<?> unirsePorCodigo(
            @RequestBody Map<String, String> body,
            @AuthenticationPrincipal Usuario usuario) {
        String codigo = body.get("codigo");
        usuarioGrupoService.unirsePorCodigo(codigo, usuario);
        return ResponseEntity.ok("Unido correctamente al grupo.");
    }

    @DeleteMapping("/eliminar/{grupoId}/{usuarioId}")
    public ResponseEntity<?> eliminarUsuarioDelGrupo(@PathVariable Long grupoId, @PathVariable Long usuarioId) {
        usuarioGrupoService.eliminarUsuarioDelGrupo(grupoId, usuarioId);
        return ResponseEntity.ok("Usuario eliminado del grupo");
    }

    /**
     * El usuario autenticado abandona el grupo
     */
    @DeleteMapping("/abandonar/{grupoId}")
    public ResponseEntity<String> abandonarGrupo(
            @PathVariable Long grupoId,
            @AuthenticationPrincipal Usuario usuario) {
        usuarioGrupoService.abandonarGrupo(usuario, grupoId);
        return ResponseEntity.ok("Has abandonado el grupo correctamente.");
    }

    @GetMapping("/miembros/{grupoId}")
    public ResponseEntity<List<Usuario>> listarMiembros(@PathVariable Long grupoId) {
        return ResponseEntity.ok(usuarioGrupoService.listarMiembros(grupoId));
    }
}
