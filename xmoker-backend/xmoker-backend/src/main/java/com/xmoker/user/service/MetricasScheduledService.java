package com.xmoker.user.service;

import com.xmoker.user.entity.Usuario;
import com.xmoker.user.repository.UserRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MetricasScheduledService {

    private final UserRepository userRepository;
    private final MetricasProgresoService metricasProgresoService;

    public MetricasScheduledService(UserRepository userRepository, MetricasProgresoService metricasProgresoService) {
        this.userRepository = userRepository;
        this.metricasProgresoService = metricasProgresoService;
    }

    // 🔁 Cada 1 hora (para probar usa cada 1 minuto con fixedRate = 60000)
    @Scheduled(fixedRate = 3600000) // cada 3600000 ms = 1h
    public void actualizarMetricas() {
        List<Usuario> usuarios = userRepository.findAll();
        for (Usuario u : usuarios) {
            metricasProgresoService.calcularMetricas(u); // este método guarda las métricas
            System.out.println("✔️ Métricas actualizadas para: " + u.getEmail());
        }
    }
}
