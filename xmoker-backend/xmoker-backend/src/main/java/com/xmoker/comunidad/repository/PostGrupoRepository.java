package com.xmoker.comunidad.repository;

import com.xmoker.comunidad.entity.PostGrupo;
import com.xmoker.comunidad.entity.GrupoApoyo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostGrupoRepository extends JpaRepository<PostGrupo, Long> {
    List<PostGrupo> findByGrupo(GrupoApoyo grupo);
    List<PostGrupo> findByGrupoOrderByFechaPublicacionDesc(GrupoApoyo grupo);
    void deleteAllByGrupo(GrupoApoyo grupo);
}

