package com.xmoker.consejo.controller;

import com.xmoker.consejo.entity.ConsejoDiario;
import com.xmoker.consejo.service.ConsejoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/consejos")
public class ConsejoController {

    private final ConsejoService consejoService;

    public ConsejoController(ConsejoService consejoService) {
        this.consejoService = consejoService;
    }

    @GetMapping("/dia/{dia}")
    public ResponseEntity<ConsejoDiario> porDia(@PathVariable int dia) {
        ConsejoDiario consejo = consejoService.obtenerPorDia(dia);
        return consejo != null ? ResponseEntity.ok(consejo) : ResponseEntity.noContent().build();
    }

    @GetMapping("/categoria/{cat}")
    public List<ConsejoDiario> porCategoria(@PathVariable String cat) {
        return consejoService.obtenerPorCategoria(cat);
    }

    @GetMapping("/aleatorio")
    public ConsejoDiario aleatorio() {
        return consejoService.obtenerAleatorio();
    }
}
