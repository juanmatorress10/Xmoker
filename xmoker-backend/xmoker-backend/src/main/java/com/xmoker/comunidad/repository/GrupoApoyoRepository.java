package com.xmoker.comunidad.repository;

import com.xmoker.comunidad.entity.GrupoApoyo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GrupoApoyoRepository extends JpaRepository<GrupoApoyo, Long> {

    // Para obtener solo los grupos públicos
    List<GrupoApoyo> findByEsPrivadoFalse();
    boolean existsByCodigoAcceso(String codigoAcceso);
    Optional<GrupoApoyo> findByCodigoAcceso(String codigoAcceso);
}
