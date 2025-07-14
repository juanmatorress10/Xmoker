package com.xmoker.logro.repository;

import com.xmoker.logro.entity.Logro;
import com.xmoker.logro.entity.UsuarioLogro;
import com.xmoker.user.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioLogroRepository extends JpaRepository<UsuarioLogro, Long> {
    List<UsuarioLogro> findByUsuarioId(Long usuarioId);
    Optional<UsuarioLogro> findByUsuarioAndLogro(Usuario usuario, Logro logro);
}
