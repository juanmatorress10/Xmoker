package com.xmoker.user.repository;

import com.xmoker.user.entity.Recaida;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecaidaRepository extends JpaRepository<Recaida, Long> {
    // Puedes añadir métodos personalizados si los necesitas
}
