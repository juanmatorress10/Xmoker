package com.xmoker.user.controller;

import com.xmoker.user.dto.RecaidaRequest;
import com.xmoker.user.entity.Recaida;
import com.xmoker.user.service.RecaidaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recaidas")
public class RecaidaController {

    private final RecaidaService recaidaService;

    public RecaidaController(RecaidaService recaidaService) {
        this.recaidaService = recaidaService;
    }

    @PostMapping("/{userId}")
    public ResponseEntity<?> registrar(@PathVariable Long userId, @RequestBody Recaida recaida) {
        recaidaService.registrarRecaida(userId, recaida);
        return ResponseEntity.ok("Recaída registrada y progreso reiniciado");
    }
}
