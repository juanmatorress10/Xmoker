package com.xmoker.user.service;

import com.xmoker.user.entity.Usuario;
import org.springframework.stereotype.Service;

@Service
public class NivelService {

    /**
     * Tabla de experiencia necesaria por nivel:
     * Nivel 1: 0 XP
     * Nivel 2: 100 XP
     * Nivel 3: 250 XP
     * Nivel 4: 450 XP
     * ...
     * Nivel N: 50*N*(N-1)
     */
    public int calcularNivel(int experiencia) {
        int nivel = 1;
        while (experiencia >= experienciaParaNivel(nivel + 1)) {
            nivel++;
        }
        return nivel;
    }

    public int experienciaParaNivel(int nivel) {
        return 50 * nivel * (nivel - 1); // fórmula cuadrática simple
    }

    public void actualizarNivel(Usuario usuario) {
        int nuevoNivel = calcularNivel(usuario.getExperiencia());
        usuario.setNivel(nuevoNivel);
    }

    public double porcentajeProgreso(Usuario usuario) {
        int nivelActual = usuario.getNivel();
        int expActual = usuario.getExperiencia();
        int expNivelActual = experienciaParaNivel(nivelActual);
        int expSiguiente = experienciaParaNivel(nivelActual + 1);

        return (double)(expActual - expNivelActual) / (expSiguiente - expNivelActual) * 100;
    }
}
