package com.xmoker.user.repository;

import com.xmoker.user.entity.CuestionarioRespuesta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CuestionarioRespuestaRepository extends JpaRepository<CuestionarioRespuesta, Long> {
    void deleteByUsuario_Id(Long usuarioId);
    List<CuestionarioRespuesta> findByUsuarioId(Long usuarioId);
}
