package com.xmoker.comunidad.repository;

import com.xmoker.comunidad.entity.ReaccionGrupoPost;
import com.xmoker.comunidad.entity.PostGrupo;
import com.xmoker.user.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReaccionGrupoPostRepository extends JpaRepository<ReaccionGrupoPost, Long> {
    List<ReaccionGrupoPost> findByPost(PostGrupo post);
    Optional<ReaccionGrupoPost> findByUsuarioAndPost(Usuario usuario, PostGrupo post);
}
