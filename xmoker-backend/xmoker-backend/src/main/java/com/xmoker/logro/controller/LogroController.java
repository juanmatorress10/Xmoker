package com.xmoker.logro.controller;

import com.xmoker.logro.entity.UsuarioLogro;
import com.xmoker.logro.service.LogroService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/logros")
@RequiredArgsConstructor
public class LogroController {

    private final LogroService logroService;

    @GetMapping("/{usuarioId}")
    public List<UsuarioLogro> obtenerLogrosUsuario(@PathVariable Long usuarioId) {
        return logroService.obtenerLogros(usuarioId);
    }

    @PostMapping("/evaluar/{usuarioId}")
    public void evaluarLogros(@PathVariable Long usuarioId) {
        logroService.evaluarLogrosParaUsuarioPorId(usuarioId); // puedes añadir este método en el servicio si prefieres
    }

    @PostMapping("/ansiedad/{usuarioId}")
    public ResponseEntity<Void> registrarRetoAnsiedad(@PathVariable Long usuarioId) {
        logroService.registrarActividadAnsiedad(usuarioId);
        return ResponseEntity.ok().build();
    }


    @PutMapping("/reclamar/{usuarioLogroId}")
    public ResponseEntity<Void> reclamarLogro(@PathVariable Long usuarioLogroId) {
        logroService.marcarComoReclamado(usuarioLogroId);
        return ResponseEntity.ok().build();
    }
}
