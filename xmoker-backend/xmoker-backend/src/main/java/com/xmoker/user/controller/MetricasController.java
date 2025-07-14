package com.xmoker.user.controller;

import com.xmoker.user.entity.Usuario;
import com.xmoker.user.repository.UserRepository;
import com.xmoker.user.service.MetricasProgresoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/metricas")
public class MetricasController {

    private final MetricasProgresoService metricasService;
    private final UserRepository userRepository;

    public MetricasController(MetricasProgresoService metricasService, UserRepository userRepository) {
        this.metricasService = metricasService;
        this.userRepository = userRepository;
    }

    @GetMapping("/{idUsuario}")
    public ResponseEntity<Map<String, Object>> obtenerMetricas(@PathVariable Long idUsuario) {
        Usuario usuario = userRepository.findById(idUsuario).orElseThrow();
        Map<String, Object> metricas = metricasService.calcularMetricas(usuario);
        return ResponseEntity.ok(metricas);
    }
}
