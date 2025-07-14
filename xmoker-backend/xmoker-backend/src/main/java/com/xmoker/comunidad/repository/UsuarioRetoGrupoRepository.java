package com.xmoker.comunidad.repository;

import com.xmoker.comunidad.entity.RetoGrupo;
import com.xmoker.comunidad.entity.UsuarioRetoGrupo;
import com.xmoker.user.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface UsuarioRetoGrupoRepository extends JpaRepository<UsuarioRetoGrupo, Long> {
    Optional<UsuarioRetoGrupo> findByUsuarioAndReto(Usuario usuario, RetoGrupo reto);
    List<UsuarioRetoGrupo> findByReto(RetoGrupo reto);
}
