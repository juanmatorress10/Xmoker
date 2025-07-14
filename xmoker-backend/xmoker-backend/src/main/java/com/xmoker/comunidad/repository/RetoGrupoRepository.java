package com.xmoker.comunidad.repository;

import com.xmoker.comunidad.entity.GrupoApoyo;
import com.xmoker.comunidad.entity.RetoGrupo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface RetoGrupoRepository extends JpaRepository<RetoGrupo, Long> {
    List<RetoGrupo> findByGrupo(GrupoApoyo grupo);
    List<RetoGrupo> findByGrupoAndFechaFinAfter(GrupoApoyo grupo, LocalDate hoy);
    List<RetoGrupo> findByGrupoAndFechaFinGreaterThanEqual(GrupoApoyo grupo, LocalDate fecha);
}
