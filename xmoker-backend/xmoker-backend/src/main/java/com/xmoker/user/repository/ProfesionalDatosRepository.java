package com.xmoker.user.repository;

import com.xmoker.user.entity.ProfesionalDatos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfesionalDatosRepository extends JpaRepository<ProfesionalDatos, Long> {
}
