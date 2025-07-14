package com.xmoker.comunidad.controller;

import com.xmoker.comunidad.dto.RetoListadoDTO;
import com.xmoker.comunidad.dto.RetoResumenDTO;
import com.xmoker.comunidad.entity.GrupoApoyo;
import com.xmoker.comunidad.entity.RetoGrupo;
import com.xmoker.comunidad.entity.UsuarioRetoGrupo;

import com.xmoker.comunidad.service.GrupoService;
import com.xmoker.comunidad.service.RetoGrupoService;
import com.xmoker.user.entity.Usuario;
import com.xmoker.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/retos")
public class RetoGrupoController {

    @Autowired
    private RetoGrupoService retoService;

    @Autowired
    private GrupoService grupoService;

    @Autowired
    private com.xmoker.user.security.JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/{grupoId}")
    public ResponseEntity<RetoResumenDTO> crearReto(
            @PathVariable Long grupoId,
            @RequestBody RetoGrupo reto,
            @AuthenticationPrincipal Usuario usuario) {

        GrupoApoyo grupo = grupoService.buscarPorId(grupoId);
        RetoResumenDTO resumen = retoService.crearRetoYObtenerResumen(reto, grupo, usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(resumen);
    }

    @GetMapping("/{grupoId}")
    public List<RetoListadoDTO> listarRetosPorGrupo(@PathVariable Long grupoId) {
        GrupoApoyo grupo = grupoService.buscarPorId(grupoId);
        return retoService.listarRetosPorGrupoDTO(grupo);
    }

    @PostMapping("/unirse/{retoId}")
    public ResponseEntity<Void> unirseAReto(
            @PathVariable Long retoId,
            @RequestHeader("Authorization") String authHeader) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String token = authHeader.substring(7);
        String email = jwtService.extractUsername(token);

        Usuario usuario = userRepository.findByEmail(email).orElse(null);
        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        RetoGrupo reto = new RetoGrupo();
        reto.setId(retoId);

        retoService.unirseAReto(usuario, reto);
        return ResponseEntity.ok().build();  // ✅ No devuelve entidad compleja
    }



    @PostMapping("/completar/{retoId}")
    public ResponseEntity<Void> completarReto(
            @PathVariable Long retoId,
            @AuthenticationPrincipal Usuario usuario) {

        RetoGrupo reto = new RetoGrupo();
        reto.setId(retoId);
        retoService.marcarCompletado(usuario, reto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/no-completado/{retoId}")
    public ResponseEntity<Void> marcarNoCompletado(
            @PathVariable Long retoId,
            @AuthenticationPrincipal Usuario usuario) {

        RetoGrupo reto = new RetoGrupo();
        reto.setId(retoId);
        retoService.marcarNoCompletado(usuario, reto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/resumen/{retoId}")
    public ResponseEntity<RetoResumenDTO> verResumen(@PathVariable Long retoId) {
        return ResponseEntity.ok(retoService.obtenerResumenDelReto(retoId));
    }

    @GetMapping("/activos/{grupoId}")
    public List<RetoListadoDTO> retosActivos(@PathVariable Long grupoId) {
        GrupoApoyo grupo = grupoService.buscarPorId(grupoId);
        return retoService.listarRetosActivosDTO(grupo);
    }

    @GetMapping("/mis-retos")
    public List<UsuarioRetoGrupo> misRetos(@AuthenticationPrincipal Usuario usuario) {
        return retoService.verMisRetos(usuario);
    }
}
