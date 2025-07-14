package com.xmoker.comunidad.service;

import com.xmoker.comunidad.dto.RetoListadoDTO;
import com.xmoker.comunidad.dto.RetoResumenDTO;
import com.xmoker.comunidad.dto.UsuarioEnRetoDTO;
import com.xmoker.comunidad.entity.*;
import com.xmoker.comunidad.repository.RetoGrupoRepository;
import com.xmoker.comunidad.repository.UsuarioRetoGrupoRepository;
import com.xmoker.user.entity.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class RetoGrupoService {

    @Autowired
    private RetoGrupoRepository retoRepo;

    @Autowired
    private UsuarioRetoGrupoRepository usuarioRetoRepo;

    public RetoGrupo crearReto(RetoGrupo reto, GrupoApoyo grupo, Usuario creador) {
        reto.setGrupo(grupo);
        reto.setCreador(creador);
        return retoRepo.save(reto);
    }

    public RetoResumenDTO crearRetoYObtenerResumen(RetoGrupo reto, GrupoApoyo grupo, Usuario creador) {
        reto.setGrupo(grupo);
        reto.setCreador(creador);
        RetoGrupo guardado = retoRepo.save(reto);
        return obtenerResumenDelReto(guardado.getId());
    }

    public List<RetoListadoDTO> listarRetosPorGrupoDTO(GrupoApoyo grupo) {
        return retoRepo.findByGrupo(grupo).stream()
                .map(r -> new RetoListadoDTO(
                        r.getId(),
                        r.getTitulo(),
                        r.getDescripcion(),
                        r.getFechaInicio().toString(),
                        r.getFechaFin().toString()
                ))
                .toList();
    }

    public List<RetoListadoDTO> listarRetosActivosDTO(GrupoApoyo grupo) {
        return retoRepo.findByGrupoAndFechaFinAfter(grupo, LocalDate.now()).stream()
                .map(r -> new RetoListadoDTO(
                        r.getId(),
                        r.getTitulo(),
                        r.getDescripcion(),
                        r.getFechaInicio().toString(),
                        r.getFechaFin().toString()
                ))
                .toList();
    }

    public List<UsuarioRetoGrupo> verMisRetos(Usuario usuario) {
        return usuarioRetoRepo.findAll().stream()
                .filter(r -> r.getUsuario().getId().equals(usuario.getId()))
                .toList();
    }

    public UsuarioRetoGrupo unirseAReto(Usuario usuario, RetoGrupo reto) {
        RetoGrupo retoCompleto = retoRepo.findById(reto.getId())
                .orElseThrow(() -> new IllegalArgumentException("Reto no encontrado"));

        return usuarioRetoRepo.findByUsuarioAndReto(usuario, retoCompleto)
                .orElseGet(() -> {
                    UsuarioRetoGrupo nuevo = new UsuarioRetoGrupo();
                    nuevo.setUsuario(usuario);
                    nuevo.setReto(retoCompleto); // 👍 ahora completo
                    nuevo.setEstado(EstadoRetoUsuario.PENDIENTE);
                    return usuarioRetoRepo.save(nuevo);
                });
    }
    public void marcarCompletado(Usuario usuario, RetoGrupo reto) {
        UsuarioRetoGrupo ur = usuarioRetoRepo.findByUsuarioAndReto(usuario, reto)
                .orElseThrow(() -> new IllegalArgumentException("No estás unido a este reto"));

        ur.setEstado(EstadoRetoUsuario.COMPLETADO);
        usuarioRetoRepo.save(ur);
    }

    public void marcarNoCompletado(Usuario usuario, RetoGrupo reto) {
        UsuarioRetoGrupo ur = usuarioRetoRepo.findByUsuarioAndReto(usuario, reto)
                .orElseThrow(() -> new IllegalArgumentException("No estás unido a este reto"));

        ur.setEstado(EstadoRetoUsuario.NO_COMPLETADO);
        usuarioRetoRepo.save(ur);
    }

    public RetoResumenDTO obtenerResumenDelReto(Long retoId) {
        RetoGrupo reto = retoRepo.findById(retoId)
                .orElseThrow(() -> new IllegalArgumentException("Reto no encontrado"));

        // ① Auto-expirar los pendientes fuera de plazo
        autoExpirarPendientes(reto);

        // ② Ahora calculas conteos y participantes:
        List<UsuarioRetoGrupo> registros = usuarioRetoRepo.findByReto(reto);
        long totalCompletados = registros.stream()
                .filter(r -> r.getEstado() == EstadoRetoUsuario.COMPLETADO)
                .count();

        List<UsuarioEnRetoDTO> participantes = registros.stream()
                .map(r -> new UsuarioEnRetoDTO(
                        r.getUsuario().getId(),
                        r.getUsuario().getNombre(),
                        r.getEstado()
                ))
                .toList();

        double porcentaje = registros.isEmpty() ? 0 :
                ((double) totalCompletados / registros.size()) * 100.0;

        return new RetoResumenDTO(
                reto.getId(),
                reto.getTitulo(),
                reto.getFechaInicio().toString(),
                reto.getFechaFin().toString(),
                registros.size(),
                (int) totalCompletados,
                Math.round(porcentaje * 100.0) / 100.0,
                participantes,
                reto.getCreador() != null ? reto.getCreador().getNombre() : "Desconocido"
        );
    }

// src/main/java/com/xmoker/comunidad/service/RetoGrupoService.java

    private void autoExpirarPendientes(RetoGrupo reto) {
        LocalDate hoy = LocalDate.now();

        // obtener todos los registros de usuario-reto
        List<UsuarioRetoGrupo> regs = usuarioRetoRepo.findByReto(reto);

        for (UsuarioRetoGrupo ur : regs) {
            boolean pendiente  = ur.getEstado() == EstadoRetoUsuario.PENDIENTE;
            boolean vencidoHoy = !reto.getFechaFin().isAfter(hoy);
            // isAfter → false cuando fechaFin ≤ hoy → marca vencido hoy o antes

            if (pendiente && vencidoHoy) {
                ur.setEstado(EstadoRetoUsuario.NO_COMPLETADO);
                usuarioRetoRepo.save(ur);
            }
        }
    }

}
