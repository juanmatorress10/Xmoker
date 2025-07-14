package com.xmoker.user.service;

import com.xmoker.user.entity.Progreso;
import com.xmoker.user.entity.Recaida;
import com.xmoker.user.entity.Usuario;
import com.xmoker.user.repository.RecaidaRepository;
import com.xmoker.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import com.xmoker.logro.entity.UsuarioLogro;
import com.xmoker.logro.entity.TipoLogro;
import com.xmoker.logro.repository.UsuarioLogroRepository;
import com.xmoker.comunidad.entity.*;
import com.xmoker.comunidad.repository.PostGrupoRepository;
import com.xmoker.comunidad.repository.UsuarioGrupoRepository;
import com.xmoker.comunidad.repository.UsuarioRetoGrupoRepository;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.stream.Collectors;


import java.util.Date;
import java.util.List;

@Service
public class RecaidaService {

    private final UserRepository userRepository;
    private final RecaidaRepository recaidaRepository;
    private final UsuarioLogroRepository usuarioLogroRepository;
    private final PostGrupoRepository postGrupoRepository;
    private final UsuarioGrupoRepository usuarioGrupoRepository;
    private final UsuarioRetoGrupoRepository usuarioRetoGrupoRepository;

    public RecaidaService(UserRepository userRepository,
                          RecaidaRepository recaidaRepository,
                          UsuarioLogroRepository usuarioLogroRepository,
                          PostGrupoRepository postGrupoRepository,
                          UsuarioGrupoRepository usuarioGrupoRepository,
                          UsuarioRetoGrupoRepository usuarioRetoGrupoRepository) {
        this.userRepository = userRepository;
        this.recaidaRepository = recaidaRepository;
        this.usuarioLogroRepository = usuarioLogroRepository;
        this.postGrupoRepository = postGrupoRepository;
        this.usuarioGrupoRepository = usuarioGrupoRepository;
        this.usuarioRetoGrupoRepository = usuarioRetoGrupoRepository;
    }


    public void registrarRecaida(Long userId, Recaida recaida) {
        Usuario usuario = userRepository.findById(userId).orElseThrow();
        Progreso progreso = usuario.getProgreso();

        // Guardar la recaída
        recaida.setFecha(new Date());
        recaida.setProgreso(progreso);
        recaidaRepository.save(recaida);

        // Resetear progreso
        progreso.setDiasSinFumar(0);
        progreso.setCantidadCigarrillosEvitados(0);
        progreso.setDineroAhorrado(0);
        progreso.setHorasVidaRecuperadas(0);
        progreso.setRachaActual(0);
        progreso.setCO2NoEmitido("0 kg");
        progreso.setCapacidadPulmonarRecuperada("0 %");
        progreso.setMejoraSistemaInmune("0 %");
        progreso.setReduccionRiesgoEnfermedades("0 %");
        progreso.setMejoraCalidadSueño("0 %");
        progreso.setNivelesEstresReducidos("0 %");

        // Reiniciar fecha del proceso
        usuario.setFechaInicioProceso(new Date());

        // Reiniciar progreso de logros en curso relacionados con la recaída
        List<UsuarioLogro> logros = usuarioLogroRepository.findByUsuarioId(userId);

        for (UsuarioLogro ul : logros) {
            TipoLogro tipo = ul.getLogro().getTipo();

            // Logros que deben reiniciarse por una recaída
            if (tipo == TipoLogro.DIAS_SIN_FUMAR ||
                    tipo == TipoLogro.CHECKIN_SEGUIDO ||
                    tipo == TipoLogro.RECAIDAS_EVITADAS ||
                    tipo == TipoLogro.COMPLETAR_RETO_ANSIEDAD) {

                ul.setProgresoActual(0);
                ul.setCompletado(false);
                ul.setReclamado(false);
                ul.setFechaAlcanzado(null);
                usuarioLogroRepository.save(ul);
            }
        }

        // Crear post automático en los grupos del usuario
        List<UsuarioGrupo> grupos = usuarioGrupoRepository.findByUsuario(usuario);

        String contenido = "🛑 He tenido una recaída";
        if (recaida.getMotivo() != null && !recaida.getMotivo().isBlank()) {
            contenido += ": " + recaida.getMotivo();
        }


        for (UsuarioGrupo ug : grupos) {
            PostGrupo post = new PostGrupo();
            post.setGrupo(ug.getGrupo());
            post.setAutor(usuario);
            post.setContenido(contenido);
            postGrupoRepository.save(post);
        }


        // Marcar retos activos como NO_COMPLETADO (en vez de eliminar)
        LocalDate hoy = LocalDate.now(ZoneId.systemDefault());

        List<UsuarioRetoGrupo> retosActivos = usuarioRetoGrupoRepository.findAll().stream()
                .filter(r -> r.getUsuario().equals(usuario)
                        && r.getEstado() != EstadoRetoUsuario.COMPLETADO
                        && r.getReto().getFechaFin().isAfter(hoy))
                .collect(Collectors.toList());

        for (UsuarioRetoGrupo r : retosActivos) {
            r.setEstado(EstadoRetoUsuario.NO_COMPLETADO); // ✅ trazabilidad
            usuarioRetoGrupoRepository.save(r);
        }


        // Guardar todo
        userRepository.save(usuario); // Guarda también progreso por cascada
    }
}
