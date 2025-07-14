package com.xmoker.comunidad.service;

import com.xmoker.comunidad.entity.PostGrupo;
import com.xmoker.comunidad.entity.ReaccionGrupoPost;
import com.xmoker.comunidad.entity.TipoReaccion;
import com.xmoker.comunidad.repository.ReaccionGrupoPostRepository;
import com.xmoker.user.entity.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReaccionGrupoPostService {

    @Autowired
    private ReaccionGrupoPostRepository reaccionRepo;

    public ReaccionGrupoPost reaccionar(Usuario usuario, PostGrupo post, TipoReaccion tipo) {
        return reaccionRepo.findByUsuarioAndPost(usuario, post).map(r -> {
            r.setTipo(tipo); // actualizar si ya reaccionó
            return reaccionRepo.save(r);
        }).orElseGet(() -> {
            ReaccionGrupoPost nueva = new ReaccionGrupoPost();
            nueva.setUsuario(usuario);
            nueva.setPost(post);
            nueva.setTipo(tipo);
            return reaccionRepo.save(nueva);
        });
    }

    public List<ReaccionGrupoPost> obtenerReacciones(PostGrupo post) {
        return reaccionRepo.findByPost(post);
    }
}
