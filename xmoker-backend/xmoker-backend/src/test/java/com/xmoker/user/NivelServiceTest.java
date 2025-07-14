// src/test/java/com/xmoker/user/service/NivelServiceTest.java
package com.xmoker.user.service;

import com.xmoker.user.entity.Usuario;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NivelServiceTest {

    @Test
    void actualizarNivel_invocaCalculoYSetaNivel() {
        // Usamos spy para verificar la llamada interna a calcularNivel
        NivelService service = Mockito.spy(new NivelService());
        Usuario u = new Usuario();

        // Forzamos que calcularNivel devuelva 5
        doReturn(5).when(service).calcularNivel(anyInt());

        service.actualizarNivel(u);
        // El nivel del usuario debe ser 5
        assertEquals(5, u.getNivel());
    }

    @Test
    void porcentajeProgreso_conValoresConocidos_devuelvePorcentajeCorrecto() {
        NivelService service = new NivelService();
        Usuario u = new Usuario();
        // Nivel 1 requiere 0 XP, nivel 2 requiere 100 XP => con 50 XP estamos al 50%
        u.setNivel(1);
        u.setExperiencia(50);

        double pct = service.porcentajeProgreso(u);
        assertEquals(50.0, pct, 0.01);
    }
}
