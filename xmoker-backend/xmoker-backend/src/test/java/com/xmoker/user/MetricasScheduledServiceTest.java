// src/test/java/com/xmoker/user/service/MetricasScheduledServiceTest.java
package com.xmoker.user.service;

import com.xmoker.user.entity.Usuario;
import com.xmoker.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MetricasScheduledServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private MetricasProgresoService metricasProgresoService;
    @InjectMocks private MetricasScheduledService scheduledService;

    @Test
    void actualizarMetricas_iteracionSobreUsuarios_invocaCalculoPorCadaUsuario() {
        // Arrange: dos usuarios ficticios
        Usuario u1 = new Usuario(); u1.setEmail("a@x.com");
        Usuario u2 = new Usuario(); u2.setEmail("b@x.com");
        when(userRepository.findAll()).thenReturn(List.of(u1, u2));

        // Act
        scheduledService.actualizarMetricas();

        // Assert: debe llamarse a calcularMetricas dos veces
        verify(metricasProgresoService, times(2)).calcularMetricas(any(Usuario.class));
    }
}
