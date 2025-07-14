package com.xmoker.logro.service;

import com.xmoker.logro.entity.Logro;
import com.xmoker.logro.entity.TipoLogro;
import com.xmoker.user.entity.Progreso;
import com.xmoker.user.entity.Usuario;

public class LogroEvaluator {

    /**
     * Evalúa si un usuario ha cumplido un logro según su progreso.
     * Devuelve false si cualquiera de los parámetros necesarios es null.
     */
    public static boolean evaluar(Logro logro, Usuario usuario) {
        if (logro == null || usuario == null) {
            return false;
        }
        Progreso progreso = usuario.getProgreso();
        if (progreso == null) {
            return false;
        }
        TipoLogro tipo = logro.getTipo();
        if (tipo == null) {
            return false;
        }

        int objetivo = logro.getValorObjetivo();

        return switch (tipo) {
            case DIAS_SIN_FUMAR ->
                    progreso.getDiasSinFumar() >= objetivo;
            case CHECKIN_SEGUIDO ->
                    progreso.getRachaActual() >= objetivo;
            case PARTICIPAR_FORO ->
                    progreso.getParticipacionForo() != null &&
                            progreso.getParticipacionForo().length() >= objetivo;
            case DINERO_AHORRADO ->
                    (int) progreso.getDineroAhorrado() >= objetivo;
            case VIDA_RECUPERADA ->
                    progreso.getHorasVidaRecuperadas() >= objetivo;
            case CO2_EVITADO ->
                    parseEntero(progreso.getCO2NoEmitido()) >= objetivo;
            case CAPACIDAD_PULMONAR ->
                    parseEntero(progreso.getCapacidadPulmonarRecuperada()) >= objetivo;
            case RIESGO_CV ->
                    parseEntero(progreso.getReduccionRiesgoEnfermedades()) >= objetivo;
            case COMPLETAR_RETO_ANSIEDAD ->
                    progreso.getRetosAnsiedadCompletados() >= objetivo;
            default ->
                    false;
        };
    }

    /**
     * Extrae el número entero de cadenas como "12.5 g" o "80 %".
     * Si no encuentra un número válido, devuelve 0.
     */
    private static int parseEntero(String texto) {
        if (texto == null || texto.isBlank()) {
            return 0;
        }
        try {
            String limpio = texto.replaceAll("[^\\d.]", "");
            if (limpio.isBlank()) return 0;
            double val = Double.parseDouble(limpio);
            return (int) Math.floor(val);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
