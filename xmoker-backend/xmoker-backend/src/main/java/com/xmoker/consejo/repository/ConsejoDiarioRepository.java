package com.xmoker.consejo.repository;

import com.xmoker.consejo.entity.ConsejoDiario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface ConsejoDiarioRepository extends JpaRepository<ConsejoDiario, Long> {
    Optional<ConsejoDiario> findByDiaSugerido(Integer dia);
    List<ConsejoDiario> findByCategoria(String categoria);
}
