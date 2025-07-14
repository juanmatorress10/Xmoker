package com.xmoker.diario.controller;

import com.xmoker.diario.entity.DiarioEntrada;
import com.xmoker.diario.service.DiarioService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/diario")
@CrossOrigin(origins = "*")
public class DiarioController {

    private final DiarioService diarioService;

    public DiarioController(DiarioService diarioService) {
        this.diarioService = diarioService;
    }

    @PostMapping("/{usuarioId}")
    public DiarioEntrada crearEntrada(@PathVariable Long usuarioId, @RequestBody DiarioEntrada entrada) {
        return diarioService.crearEntrada(
                usuarioId,
                entrada.getEmocionDelDia(),
                entrada.getEstrategiasUsadas(),
                entrada.getComplicacionesEncontradas()
        );
    }

    @GetMapping("/{usuarioId}")
    public List<DiarioEntrada> listarEntradas(@PathVariable Long usuarioId) {
        return diarioService.listarEntradas(usuarioId);
    }

    @DeleteMapping("/{idEntrada}")
    public void eliminarEntrada(@PathVariable Long idEntrada) {
        diarioService.eliminarEntrada(idEntrada);
    }
}
