// src/main/java/com/xmoker/comunidad/service/EstadisticasGrupoService.java

package com.xmoker.comunidad.service;

import com.xmoker.comunidad.dto.EstadisticasGrupoDTO;
import com.xmoker.comunidad.dto.ComparativaGrupoDTO;
import com.xmoker.comunidad.entity.GrupoApoyo;
import com.xmoker.comunidad.entity.UsuarioGrupo;
import com.xmoker.comunidad.entity.RetoGrupo;
import com.xmoker.comunidad.repository.UsuarioGrupoRepository;
import com.xmoker.comunidad.repository.PostGrupoRepository;
import com.xmoker.comunidad.repository.RetoGrupoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.xmoker.user.entity.Usuario;
import com.xmoker.user.entity.Progreso;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class EstadisticasGrupoService {

    @Autowired
    private UsuarioGrupoRepository usuarioGrupoRepo;

    @Autowired
    private PostGrupoRepository postRepo;

    @Autowired
    private RetoGrupoRepository retoRepo;

    @Autowired
    private GrupoService grupoService;

    /** Coste medio diario evitado por no fumar */
    private static final double COSTO_POR_DIA = 5.0;

    public EstadisticasGrupoDTO calcularEstadisticas(GrupoApoyo grupo) {
        List<UsuarioGrupo> miembros = usuarioGrupoRepo.findByGrupo(grupo);

        int totalMiembros = 0;
        int totalDias = 0;
        int totalRachaActual = 0;
        int totalRachaMaxima = 0;

        for (UsuarioGrupo ug : miembros) {
            Usuario u = ug.getUsuario();
            if (u == null) {
                continue;
            }
            Progreso p = u.getProgreso();
            if (p != null) {
                totalDias += p.getDiasSinFumar();
                totalRachaActual += p.getRachaActual();
                totalRachaMaxima += p.getRachaMaxima();
                totalMiembros++;
            }
        }

        int totalPublicaciones = postRepo.findByGrupo(grupo).size();
        List<RetoGrupo> retos = retoRepo.findByGrupo(grupo);
        int retosActivos = (int) retos.stream()
                .filter(r -> r.getFechaFin().isAfter(LocalDate.now()))
                .count();

        return new EstadisticasGrupoDTO(
                grupo.getId(),
                totalMiembros,
                totalDias,
                totalPublicaciones,
                retos.size(),
                retosActivos,
                totalMiembros > 0 ? (double) totalRachaActual / totalMiembros : 0,
                totalMiembros > 0 ? (double) totalRachaMaxima / totalMiembros : 0
        );
    }

    public ComparativaGrupoDTO obtenerComparativa(GrupoApoyo grupo) {
        EstadisticasGrupoDTO tusStats = calcularEstadisticas(grupo);
        double tusMediaDias = tusStats.getTotalMiembros() > 0
                ? (double) tusStats.getTotalDiasSinFumar() / tusStats.getTotalMiembros()
                : 0;
        double tusMediaDinero = tusMediaDias * COSTO_POR_DIA;

        List<GrupoApoyo> todosGrupos = grupoService.buscarTodos();
        double sumaMediaDiasGlobal = 0;
        double sumaMediaDineroGlobal = 0;
        int gruposConMiembros = 0;

        for (GrupoApoyo g : todosGrupos) {
            EstadisticasGrupoDTO s = calcularEstadisticas(g);
            if (s.getTotalMiembros() > 0) {
                double mediaDias = (double) s.getTotalDiasSinFumar() / s.getTotalMiembros();
                sumaMediaDiasGlobal += mediaDias;
                sumaMediaDineroGlobal += mediaDias * COSTO_POR_DIA;
                gruposConMiembros++;
            }
        }

        double mediaGlobalDias = gruposConMiembros > 0
                ? sumaMediaDiasGlobal / gruposConMiembros
                : 0;
        double mediaGlobalDinero = gruposConMiembros > 0
                ? sumaMediaDineroGlobal / gruposConMiembros
                : 0;

        return new ComparativaGrupoDTO(
                tusMediaDias - mediaGlobalDias,
                tusMediaDinero - mediaGlobalDinero
        );
    }
}
