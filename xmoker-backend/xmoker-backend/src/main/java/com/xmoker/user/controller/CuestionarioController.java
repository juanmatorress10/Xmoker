package com.xmoker.user.controller;

import com.xmoker.user.dto.PerfilFumadorDTO;
import com.xmoker.user.dto.RespuestaDTO;
import com.xmoker.user.service.CuestionarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cuestionario")
public class CuestionarioController {

    private final CuestionarioService cuestionarioService;

    public CuestionarioController(CuestionarioService cuestionarioService) {
        this.cuestionarioService = cuestionarioService;
    }

    @PostMapping("/respuestas/{usuarioId}")
    public ResponseEntity<Void> guardar(@PathVariable Long usuarioId,
                                        @RequestBody List<RespuestaDTO> respuestas) {
        cuestionarioService.guardarRespuestas(usuarioId, respuestas);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/perfil/{usuarioId}")
    public ResponseEntity<PerfilFumadorDTO> perfil(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(cuestionarioService.analizarPerfil(usuarioId));
    }
}
