package com.xmoker.user.service;

import com.xmoker.logro.service.LogroService;
import com.xmoker.user.entity.Progreso;
import com.xmoker.user.entity.Usuario;
import com.xmoker.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class MetricasProgresoService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LogroService logroService;

    public Map<String, Object> calcularMetricas(Usuario usuario) {
        Map<String, Object> metricas = new HashMap<>();

        Date fecha = usuario.getFechaInicioProceso();
        LocalDate inicio;



        if (fecha instanceof java.sql.Date) {
            inicio = ((java.sql.Date) fecha).toLocalDate();
        } else {
            inicio = fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        }

        LocalDate hoy = LocalDate.now();
        long totalDias = ChronoUnit.DAYS.between(inicio, hoy);
        Period periodo = Period.between(inicio, hoy);
        long horasTotales = totalDias * 24;

        int cigDiarios = usuario.getNivelConsumo();
        int cigEvitados = (int) (totalDias * cigDiarios);
        double precioPack = 5.5;
        double dineroAhorrado = (cigEvitados / 20.0) * precioPack;
        int horasVida = (int) (totalDias * 6);
        double co2 = cigEvitados * 0.01377;
        double capacidadPulmonar = Math.min(100, 70 + totalDias * 0.3);
        double riesgoCV = totalDias < 365
                ? (totalDias / 365.0) * 44
                : totalDias < 1825 ? 44 + ((totalDias - 365) / 1460.0) * 17 : 61;
        double mejoraInmune = Math.min(100, 30 + (totalDias / 30.0) * 5);
        double sueno = Math.min(100, 20 + (totalDias / 7.0) * 5);
        double estres = Math.min(100, 10 + (totalDias / 7.0) * 7.5);

        Progreso progreso = usuario.getProgreso();
        progreso.setDiasSinFumar((int) totalDias);
        progreso.setCantidadCigarrillosEvitados(cigEvitados);
        progreso.setDineroAhorrado((float) dineroAhorrado);
        progreso.setHorasVidaRecuperadas(horasVida);
        progreso.setCO2NoEmitido(String.format("%.2f g", co2));
        progreso.setCapacidadPulmonarRecuperada(String.format("%.2f %%", capacidadPulmonar));
        progreso.setReduccionRiesgoEnfermedades(String.format("%.2f %%", riesgoCV));
        progreso.setMejoraSistemaInmune(String.format("%.2f %%", mejoraInmune));
        progreso.setMejoraCalidadSueño(String.format("%.2f %%", sueno));
        progreso.setNivelesEstresReducidos(String.format("%.2f %%", estres));

        userRepository.save(usuario);
        logroService.evaluarLogrosParaUsuario(usuario);


        metricas.put("diasSinFumar", totalDias);
        metricas.put("cigarrillosEvitados", cigEvitados);
        metricas.put("dineroAhorrado", String.format("%.2f €", dineroAhorrado));
        metricas.put("horasVidaGanadas", horasVida);
        metricas.put("CO2NoEmitido", String.format("%.2f kg", co2));
        metricas.put("capacidadPulmonarRecuperada", String.format("%.2f %%", capacidadPulmonar));
        metricas.put("reduccionRiesgoCardiovascular", String.format("%.2f %%", riesgoCV));
        metricas.put("mejoraSistemaInmune", String.format("%.2f %%", mejoraInmune));
        metricas.put("mejoraCalidadSueno", String.format("%.2f %%", sueno));
        metricas.put("reduccionEstres", String.format("%.2f %%", estres));

        // NUEVO: tiempo formateado
        String tiempoFormateado = String.format("%d años, %d meses, %d días, %d horas",
                periodo.getYears(), periodo.getMonths(), periodo.getDays(), horasTotales);
        metricas.put("progresoTiempo", tiempoFormateado);

        return metricas;
    }

}
