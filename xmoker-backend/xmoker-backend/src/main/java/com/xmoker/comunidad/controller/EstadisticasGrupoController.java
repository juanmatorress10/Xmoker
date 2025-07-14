// src/main/java/com/xmoker/comunidad/controller/EstadisticasGrupoController.java

package com.xmoker.comunidad.controller;

import com.xmoker.comunidad.dto.EstadisticasGrupoDTO;
import com.xmoker.comunidad.dto.ComparativaGrupoDTO;
import com.xmoker.comunidad.entity.GrupoApoyo;
import com.xmoker.comunidad.service.EstadisticasGrupoService;
import com.xmoker.comunidad.service.GrupoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/estadisticas")
@CrossOrigin(origins = "http://localhost:4200")
public class EstadisticasGrupoController {
    private static final Logger log = LoggerFactory.getLogger(EstadisticasGrupoController.class);

    @Autowired
    private EstadisticasGrupoService estadisticasService;

    @Autowired
    private GrupoService grupoService;

    /** Devuelve totales y medias del grupo */
    @GetMapping("/{grupoId}")
    public EstadisticasGrupoDTO obtenerEstadisticas(@PathVariable Long grupoId) {
        GrupoApoyo grupo = grupoService.buscarPorId(grupoId);
        return estadisticasService.calcularEstadisticas(grupo);
    }

    /** Devuelve la diferencia respecto a la media global */
    @GetMapping("/{grupoId}/comparativa")
    public ResponseEntity<ComparativaGrupoDTO> obtenerComparativa(@PathVariable Long grupoId) {
        try {
            GrupoApoyo grupo = grupoService.buscarPorId(grupoId);
            ComparativaGrupoDTO dto = estadisticasService.obtenerComparativa(grupo);
            return ResponseEntity.ok(dto);
        } catch (Exception ex) {
            log.error("Error al calcular comparativa para grupo " + grupoId, ex);
            // Si hay fallo, devolvemos diferencia cero en lugar de 500 sin cuerpo válido
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ComparativaGrupoDTO(0.0, 0.0));
        }
    }
}
