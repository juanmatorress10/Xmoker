package com.xmoker.logro.config;

import com.xmoker.logro.entity.Logro;
import com.xmoker.logro.entity.TipoLogro;
import com.xmoker.logro.repository.LogroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class LogroInitializer implements CommandLineRunner {

    private final LogroRepository logroRepository;

    @Override
    public void run(String... args) {
        if (logroRepository.count() > 0) return;

        String rutaBase = "/images";
        List<Logro> logros = new ArrayList<>();

        // 1. Días sin fumar (hasta 2 años)
        int[] dias = {1, 3, 7, 14, 21, 30, 60, 90, 120, 150, 180, 210, 240, 270, 300, 330, 365, 400, 450, 500, 550, 600, 650, 700, 730};
        int[] exp =  {50, 70, 100, 120, 140, 180, 220, 280, 320, 360, 400, 450, 500, 550, 600, 700, 1000, 1100, 1200, 1300, 1400, 1500, 1600, 1800, 2000};

        for (int i = 0; i < dias.length; i++) {
            logros.add(new Logro(
                    "Día " + dias[i],
                    "Has alcanzado " + dias[i] + " días sin fumar.",
                    TipoLogro.DIAS_SIN_FUMAR,
                    dias[i],
                    exp[i],
                    rutaBase + "/dias_sin_fumar.png",
                    "Progreso"
            ));
        }

        // 2. Rachas
        int[] rachas = {3, 7, 15, 30, 60, 90, 180, 270, 365};
        int[] expRacha = {30, 70, 120, 180, 250, 350, 500, 700, 900};

        for (int i = 0; i < rachas.length; i++) {
            logros.add(new Logro(
                    "Racha de " + rachas[i] + " días",
                    "Has mantenido una racha de " + rachas[i] + " días sin fallar.",
                    TipoLogro.CHECKIN_SEGUIDO,
                    rachas[i],
                    expRacha[i],
                    rutaBase + "/racha.png",
                    "Progreso"
            ));
        }

        // 3. Sociales y motivacionales
        logros.add(new Logro("Perfil completo", "Has completado tu perfil personal.", TipoLogro.COMPLETAR_PERFIL, 1, 100, rutaBase + "/social.png", "Social"));
        logros.add(new Logro("Primer mensaje en el foro", "Participaste por primera vez en la comunidad.", TipoLogro.PARTICIPAR_FORO, 1, 80, rutaBase + "/social.png", "Social"));
        logros.add(new Logro("Participante activo", "Has compartido 10 mensajes en el foro.", TipoLogro.PARTICIPAR_FORO, 10, 200, rutaBase + "/social.png", "Social"));
        logros.add(new Logro("Compartiste tu progreso", "Has compartido públicamente tu avance.", TipoLogro.COMPARTIR_PROGRESO, 1, 120, rutaBase + "/social.png", "Social"));
        logros.add(new Logro("Motivación creciente", "Has subido tu nivel de motivación.", TipoLogro.NIVEL_MOTIVACION, 1, 150, rutaBase + "/social.png", "Social"));
        logros.add(new Logro("Salud en tus manos", "Usaste el apoyo de un profesional.", TipoLogro.RECUPERAR_SALUD, 1, 150, rutaBase + "/salud.png", "Salud"));
        logros.add(new Logro("Recaída evitada", "Has evitado recaídas durante 30 días.", TipoLogro.RECAIDAS_EVITADAS, 30, 300, rutaBase + "/salud.png", "Salud"));
        logros.add(new Logro("Sin recaídas por 6 meses", "Has evitado recaídas durante medio año.", TipoLogro.RECAIDAS_EVITADAS, 180, 700, rutaBase + "/salud.png", "Salud"));

        // 4. Logros por métricas progresivas
        int[] euros = {5, 10, 20, 50, 100, 200, 500, 1000};
        for (int i = 0; i < euros.length; i++) {
            logros.add(new Logro(
                    "Has ahorrado " + euros[i] + "€",
                    "Llevas ahorrados " + euros[i] + " euros sin fumar.",
                    TipoLogro.DINERO_AHORRADO,
                    euros[i],
                    100 + i * 50,
                    rutaBase + "/dinero.png",
                    "Estadísticas"
            ));
        }

        int[] pulmon = {20, 40, 60, 80, 100};
        for (int i = 0; i < pulmon.length; i++) {
            logros.add(new Logro(
                    "Pulmones al " + pulmon[i] + "%",
                    "Tu capacidad pulmonar ha llegado al " + pulmon[i] + "%.",
                    TipoLogro.CAPACIDAD_PULMONAR,
                    pulmon[i],
                    120 + i * 40,
                    rutaBase + "/pulmon.png",
                    "Salud"
            ));
        }

        double[] co2 = {0.5, 1, 2, 5, 10};
        for (int i = 0; i < co2.length; i++) {
            logros.add(new Logro(
                    "CO₂ evitado: " + co2[i] + " kg",
                    "Has evitado emitir " + co2[i] + " kg de CO₂.",
                    TipoLogro.CO2_EVITADO,
                    (int) (co2[i] * 100),
                    100 + i * 50,
                    rutaBase + "/co2.png",
                    "Estadísticas"
            ));
        }

        int[] vida = {6, 12, 24, 48, 72, 120};
        for (int i = 0; i < vida.length; i++) {
            logros.add(new Logro(
                    "Vida ganada: " + vida[i] + "h",
                    "Has recuperado " + vida[i] + " horas de vida.",
                    TipoLogro.VIDA_RECUPERADA,
                    vida[i],
                    100 + i * 40,
                    rutaBase + "/vida.png",
                    "Estadísticas"
            ));
        }

        int[] riesgo = {10, 25, 40, 50, 60};
        for (int i = 0; i < riesgo.length; i++) {
            logros.add(new Logro(
                    "Riesgo reducido: " + riesgo[i] + "%",
                    "Has reducido tu riesgo cardiovascular en un " + riesgo[i] + "%.",
                    TipoLogro.RIESGO_CV,
                    riesgo[i],
                    130 + i * 50,
                    rutaBase + "/corazon.png",
                    "Salud"
            ));
        }

        // 5. Logros por completar retos de ansiedad
        int[] retosAnsiedad = {1, 5, 10, 20, 50, 80, 100 };
        int[] expAnsiedad = {150, 300, 500, 800, 1200, 1600, 2000};

        for (int i = 0; i < retosAnsiedad.length; i++) {
            logros.add(new Logro(
                    "Retos de ansiedad ×" + retosAnsiedad[i],
                    "Has completado " + retosAnsiedad[i] + " retos de gestión emocional (respiración, juegos, sonidos...).",
                    TipoLogro.COMPLETAR_RETO_ANSIEDAD,
                    retosAnsiedad[i],
                    expAnsiedad[i],
                    rutaBase + "/ansiedad.png", // Asegúrate de tener esta imagen
                    "Bienestar"
            ));
        }

        logroRepository.saveAll(logros);
        System.out.println("\u2714\ufe0f Logros iniciales y progresivos cargados.");
    }
}
