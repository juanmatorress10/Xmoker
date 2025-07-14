package com.xmoker.logro.service;

import com.xmoker.logro.entity.Logro;
import com.xmoker.logro.entity.UsuarioLogro;
import com.xmoker.logro.repository.LogroRepository;
import com.xmoker.logro.repository.UsuarioLogroRepository;
import com.xmoker.user.entity.Usuario;
import com.xmoker.user.repository.UserRepository;
import com.xmoker.user.service.NivelService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LogroService {

    private final LogroRepository logroRepository;
    private final UsuarioLogroRepository usuarioLogroRepository;
    private final UserRepository usuarioRepository;
    private final NivelService nivelService;

    public void evaluarLogrosParaUsuario(Usuario usuario) {
        List<Logro> logros = logroRepository.findAll();

        for (Logro logro : logros) {
            UsuarioLogro usuarioLogro = usuarioLogroRepository
                    .findByUsuarioAndLogro(usuario, logro)
                    .orElseGet(() -> new UsuarioLogro(usuario, logro));

            int progresoActual = calcularProgresoActual(logro, usuario);
            usuarioLogro.setProgresoActual(progresoActual);

            if (!usuarioLogro.isCompletado() && progresoActual >= logro.getValorObjetivo()) {
                usuarioLogro.setCompletado(true);
                usuarioLogro.setFechaAlcanzado(LocalDateTime.now());

                usuario.setExperiencia(usuario.getExperiencia() + logro.getExperienciaOtorgada());
                nivelService.actualizarNivel(usuario);
                usuarioRepository.save(usuario);
            }

            usuarioLogroRepository.save(usuarioLogro);
        }
    }

    public List<UsuarioLogro> obtenerLogros(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con id: " + usuarioId));

        List<Logro> todosLosLogros = logroRepository.findAll();

        return todosLosLogros.stream().map(logro ->
                usuarioLogroRepository.findByUsuarioAndLogro(usuario, logro)
                        .orElseGet(() -> {
                            UsuarioLogro ul = new UsuarioLogro(usuario, logro);
                            ul.setProgresoActual(calcularProgresoActual(logro, usuario));
                            return usuarioLogroRepository.save(ul); // 🔧 guardamos el nuevo logro para obtener ID
                        })
        ).toList();
    }


    public void evaluarLogrosParaUsuarioPorId(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con id: " + usuarioId));
        evaluarLogrosParaUsuario(usuario);
    }

    public void marcarComoReclamado(Long usuarioLogroId) {
        UsuarioLogro usuarioLogro = usuarioLogroRepository.findById(usuarioLogroId)
                .orElseThrow(() -> new EntityNotFoundException("UsuarioLogro no encontrado"));

        if (usuarioLogro.isReclamado()) return; // ✅ evitar doble recompensa

        usuarioLogro.setReclamado(true);

        Logro logro = usuarioLogro.getLogro();
        Usuario usuario = usuarioLogro.getUsuario();

        int progresoActual = calcularProgresoActual(logro, usuario);
        usuarioLogro.setProgresoActual(progresoActual);

        // Si no estaba completado, lo marcamos
        if (!usuarioLogro.isCompletado() && progresoActual >= logro.getValorObjetivo()) {
            usuarioLogro.setCompletado(true);
            usuarioLogro.setFechaAlcanzado(LocalDateTime.now());
        }

        // 🟢 Aquí es donde garantizamos que se sume la experiencia SOLO al reclamar
        usuario.setExperiencia(usuario.getExperiencia() + logro.getExperienciaOtorgada());
        nivelService.actualizarNivel(usuario);
        usuarioRepository.save(usuario);
        usuarioLogroRepository.save(usuarioLogro);
    }



    private int calcularProgresoActual(Logro logro, Usuario usuario) {
        var progreso = usuario.getProgreso();
        if (progreso == null) return 0;

        return switch (logro.getTipo()) {
            case DIAS_SIN_FUMAR -> progreso.getDiasSinFumar();
            case CHECKIN_SEGUIDO -> progreso.getRachaActual();
            case COMPLETAR_PERFIL -> 1; // Asume completado al asignarlo
            case PARTICIPAR_FORO -> progreso.getParticipacionForo() == null ? 0 : progreso.getParticipacionForo().length();
            case COMPARTIR_PROGRESO -> 1; // si se implementa algo específico, cámbialo
            case DINERO_AHORRADO -> (int) progreso.getDineroAhorrado();
            case VIDA_RECUPERADA -> progreso.getHorasVidaRecuperadas();
            case CO2_EVITADO -> parseIntFromString(progreso.getCO2NoEmitido());
            case CAPACIDAD_PULMONAR -> parseIntFromString(progreso.getCapacidadPulmonarRecuperada());
            case RIESGO_CV -> parseIntFromString(progreso.getReduccionRiesgoEnfermedades());
            case COMPLETAR_RETO_ANSIEDAD -> progreso.getRetosAnsiedadCompletados();

            default -> 0;
        };
    }

    private int parseIntFromString(String valor) {
        if (valor == null) return 0;
        try {
            return (int) Double.parseDouble(valor.replaceAll("[^\\d.]", ""));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public void registrarActividadAnsiedad(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        if (usuario.getProgreso() == null) {
            throw new IllegalStateException("El usuario no tiene datos de progreso inicializados.");
        }

        int actuales = usuario.getProgreso().getRetosAnsiedadCompletados();
        usuario.getProgreso().setRetosAnsiedadCompletados(actuales + 1);

        usuarioRepository.save(usuario);

        // Evaluar logros que puedan haberse desbloqueado por esta acción
        evaluarLogrosParaUsuario(usuario);
    }
}
