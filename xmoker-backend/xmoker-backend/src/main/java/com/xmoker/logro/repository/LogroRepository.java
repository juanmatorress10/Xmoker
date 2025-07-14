package com.xmoker.logro.repository;

import com.xmoker.logro.entity.Logro;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogroRepository extends JpaRepository<Logro, Long> {
    // Puedes añadir búsquedas personalizadas aquí si lo necesitas
}
