package com.xmoker.diario.repository;

import com.xmoker.diario.entity.DiarioEntrada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiarioRepository extends JpaRepository<DiarioEntrada, Long> {
    List<DiarioEntrada> findByUsuarioId(Long usuarioId);
}
