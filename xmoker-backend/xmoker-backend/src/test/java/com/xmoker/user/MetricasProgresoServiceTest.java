// src/test/java/com/xmoker/user/service/MetricasProgresoServiceTest.java
package com.xmoker.user.service;

import com.xmoker.user.entity.Progreso;
import com.xmoker.user.entity.Usuario;
import com.xmoker.user.repository.UserRepository;
import com.xmoker.logro.service.LogroService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MetricasProgresoServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private LogroService logroService;

    @InjectMocks
    private MetricasProgresoService service;

    @Test
    void calcularMetricas_fechaInicioHoy_metricasCeroExceptoPulmon() {
        // Creamos un usuario cuyo inicio de proceso es hoy
        Usuario usuario = new Usuario();
        usuario.setFechaInicioProceso(new Date());
        usuario.setNivelConsumo(0);
        usuario.setProgreso(new Progreso());

        // Stub para evitar efectos secundarios
        when(userRepository.save(usuario)).thenReturn(usuario);
        doNothing().when(logroService).evaluarLogrosParaUsuario(usuario);

        // Ejecutamos
        Map<String, Object> metricas = service.calcularMetricas(usuario);

        // diasSinFumar debe ser 0L (long)
        assertEquals(0L,
                ((Number) metricas.get("diasSinFumar")).longValue(),
                "diasSinFumar debería ser 0");

        // cigarrillosEvitados debe ser 0 (int)
        assertEquals(0,
                ((Number) metricas.get("cigarrillosEvitados")).intValue(),
                "cigarrillosEvitados debería ser 0");

        // dineroAhorrado formateado con coma decimal
        assertEquals("0,00 €",
                metricas.get("dineroAhorrado"),
                "dineroAhorrado debería ser \"0,00 €\"");

        // horasVidaGanadas debe ser 0L
        assertEquals(0L,
                ((Number) metricas.get("horasVidaGanadas")).longValue(),
                "horasVidaGanadas debería ser 0");

        // CO2NoEmitido formateado con coma decimal
        assertEquals("0,00 kg",
                metricas.get("CO2NoEmitido"),
                "CO2NoEmitido debería ser \"0,00 kg\"");

        // capacidadPulmonarRecuperada, con valor base 70% en el día 0 (coma decimal)
        assertEquals("70,00 %",
                metricas.get("capacidadPulmonarRecuperada"),
                "capacidadPulmonarRecuperada debería ser \"70,00 %\"");
    }
}
