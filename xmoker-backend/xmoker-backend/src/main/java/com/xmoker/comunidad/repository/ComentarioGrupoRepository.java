package com.xmoker.comunidad.repository;

import com.xmoker.comunidad.entity.ComentarioGrupo;
import com.xmoker.comunidad.entity.PostGrupo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ComentarioGrupoRepository extends JpaRepository<ComentarioGrupo, Long> {

    List<ComentarioGrupo> findByPostOrderByFechaComentarioAsc(PostGrupo post);

    @Modifying
    @Transactional
    @Query("DELETE FROM ComentarioGrupo c WHERE c.post = :post")
    void deleteByPost(PostGrupo post);
}
