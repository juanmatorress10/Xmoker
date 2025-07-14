package com.xmoker.comunidad.repository;

import com.xmoker.comunidad.entity.UsuarioGrupo;
import com.xmoker.user.entity.Usuario;
import com.xmoker.comunidad.entity.GrupoApoyo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioGrupoRepository extends JpaRepository<UsuarioGrupo, Long> {

    List<UsuarioGrupo> findByUsuario(Usuario usuario);

    List<UsuarioGrupo> findByGrupo(GrupoApoyo grupo);

    Optional<UsuarioGrupo> findByUsuarioAndGrupo(Usuario usuario, GrupoApoyo grupo);

    void deleteAllByGrupo(GrupoApoyo grupo);
}
