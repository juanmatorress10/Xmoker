// src/test/java/com/xmoker/logro/service/LogroEvaluatorTest.java
package com.xmoker.logro.service;

import com.xmoker.logro.entity.Logro;
import com.xmoker.logro.entity.TipoLogro;
import com.xmoker.user.entity.Progreso;
import com.xmoker.user.entity.Usuario;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LogroEvaluatorTest {

    /**
     * Si el usuario no tiene Progreso (null), debe devolver false.
     */
    @Test
    void evaluar_sinProgreso_devuelveFalse() {
        Logro logro = new Logro();
        logro.setTipo(TipoLogro.DIAS_SIN_FUMAR);
        logro.setValorObjetivo(5);

        Usuario usuario = new Usuario();
        usuario.setProgreso(null);

        assertFalse(LogroEvaluator.evaluar(logro, usuario));
    }

    /**
     * Para DIAS_SIN_FUMAR: true si diasSinFumar >= objetivo, false en caso contrario.
     */
    @Test
    void evaluar_diasSinFumar() {
        Logro logro = new Logro();
        logro.setTipo(TipoLogro.DIAS_SIN_FUMAR);
        logro.setValorObjetivo(10);

        Progreso p = new Progreso();
        p.setDiasSinFumar( 10 );
        Usuario usuario = new Usuario(); usuario.setProgreso(p);

        assertTrue(LogroEvaluator.evaluar(logro, usuario));

        p.setDiasSinFumar(5);
        assertFalse(LogroEvaluator.evaluar(logro, usuario));
    }

    /**
     * Para CHECKIN_SEGUIDO: true si rachaActual >= objetivo.
     */
    @Test
    void evaluar_checkinSeguido() {
        Logro logro = new Logro();
        logro.setTipo(TipoLogro.CHECKIN_SEGUIDO);
        logro.setValorObjetivo(3);

        Progreso p = new Progreso();
        p.setRachaActual(4);
        Usuario usuario = new Usuario(); usuario.setProgreso(p);

        assertTrue(LogroEvaluator.evaluar(logro, usuario));

        p.setRachaActual(1);
        assertFalse(LogroEvaluator.evaluar(logro, usuario));
    }

    /**
     * Para PARTICIPAR_FORO: true si participacionForo.length() >= objetivo, false
     * si es null o más corta.
     */
    @Test
    void evaluar_participarForo() {
        Logro logro = new Logro();
        logro.setTipo(TipoLogro.PARTICIPAR_FORO);
        logro.setValorObjetivo(5);

        Progreso p = new Progreso();
        p.setParticipacionForo("foro!");  // longitud 5
        Usuario usuario = new Usuario(); usuario.setProgreso(p);

        assertTrue(LogroEvaluator.evaluar(logro, usuario));

        p.setParticipacionForo("hey");
        assertFalse(LogroEvaluator.evaluar(logro, usuario));

        p.setParticipacionForo(null);
        assertFalse(LogroEvaluator.evaluar(logro, usuario));
    }

    /**
     * Para DINERO_AHORRADO y VIDA_RECUPERADA: compara números directos.
     */
    @Test
    void evaluar_dineroYVidaRecuperada() {
        Logro l1 = new Logro();
        l1.setTipo(TipoLogro.DINERO_AHORRADO);
        l1.setValorObjetivo(50);

        Logro l2 = new Logro();
        l2.setTipo(TipoLogro.VIDA_RECUPERADA);
        l2.setValorObjetivo(2);

        Progreso p = new Progreso();
        p.setDineroAhorrado(60f);
        p.setHorasVidaRecuperadas(1);

        Usuario usuario = new Usuario(); usuario.setProgreso(p);

        assertTrue(LogroEvaluator.evaluar(l1, usuario));
        assertFalse(LogroEvaluator.evaluar(l2, usuario));
    }

    /**
     * Para los casos que usan parseEntero: CO2_EVITADO, CAPACIDAD_PULMONAR y RIESGO_CV.
     */
    @Test
    void evaluar_parseEnteroCasos() {
        Logro co2 = new Logro();
        co2.setTipo(TipoLogro.CO2_EVITADO);
        co2.setValorObjetivo(10);

        Logro cap = new Logro();
        cap.setTipo(TipoLogro.CAPACIDAD_PULMONAR);
        cap.setValorObjetivo(80);

        Logro riesgo = new Logro();
        riesgo.setTipo(TipoLogro.RIESGO_CV);
        riesgo.setValorObjetivo(30);

        Progreso p = new Progreso();
        p.setCO2NoEmitido("12.5 g");
        p.setCapacidadPulmonarRecuperada("85 %");
        p.setReduccionRiesgoEnfermedades("20 %");

        Usuario usuario = new Usuario(); usuario.setProgreso(p);

        assertTrue(LogroEvaluator.evaluar(co2, usuario));    // 12 >= 10
        assertTrue(LogroEvaluator.evaluar(cap, usuario));    // 85 >= 80
        assertFalse(LogroEvaluator.evaluar(riesgo, usuario)); // 20 < 30
    }

    /**
     * Para COMPLETAR_RETO_ANSIEDAD: compara retosAnsiedadCompletados >= objetivo.
     */
    @Test
    void evaluar_retoAnsiedad() {
        Logro logro = new Logro();
        logro.setTipo(TipoLogro.COMPLETAR_RETO_ANSIEDAD);
        logro.setValorObjetivo(2);

        Progreso p = new Progreso();
        p.setRetosAnsiedadCompletados(3);

        Usuario usuario = new Usuario(); usuario.setProgreso(p);

        assertTrue(LogroEvaluator.evaluar(logro, usuario));

        p.setRetosAnsiedadCompletados(1);
        assertFalse(LogroEvaluator.evaluar(logro, usuario));
    }

    /**
     * Caso default: tipos no cubiertos deben devolver false.
     */
    @Test
    void evaluar_tipoDesconocido_devuelveFalse() {
        Logro logro = new Logro();
        // Asumamos un enum no contemplado, por ejemplo un nuevo tipo
        logro.setTipo(null);
        logro.setValorObjetivo(0);

        Progreso p = new Progreso();
        p.setDiasSinFumar(100);
        Usuario usuario = new Usuario(); usuario.setProgreso(p);

        assertFalse(LogroEvaluator.evaluar(logro, usuario));
    }
}
