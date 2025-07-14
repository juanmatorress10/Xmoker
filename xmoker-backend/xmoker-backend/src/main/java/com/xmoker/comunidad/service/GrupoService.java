package com.xmoker.comunidad.service;

import com.xmoker.comunidad.entity.GrupoApoyo;
import com.xmoker.comunidad.entity.UsuarioGrupo;
import com.xmoker.comunidad.repository.ComentarioGrupoRepository;
import com.xmoker.comunidad.repository.GrupoApoyoRepository;
import com.xmoker.comunidad.repository.PostGrupoRepository;
import com.xmoker.comunidad.repository.UsuarioGrupoRepository;
import com.xmoker.user.entity.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class GrupoService {

    @Autowired
    private GrupoApoyoRepository grupoRepo;
    @Autowired
    private UsuarioGrupoRepository usuarioGrupoRepo;

    @Autowired
    private PostGrupoRepository postGrupoRepo;

    @Autowired
    private ComentarioGrupoRepository comentarioGrupoRepository;


    public GrupoApoyo crearGrupo(GrupoApoyo grupo, Usuario creador) {
        //Compruebo que no pertenezca a otro grupo ya.
        if (!usuarioGrupoRepo.findByUsuario(creador).isEmpty()) {
            throw new IllegalStateException("Ya perteneces a un grupo. No puedes crear otro.");
        }

        grupo.setCreador(creador);
        grupo.setEsPrivado(true);
        grupo.setCodigoAcceso(generarCodigoUnico());
        grupo = grupoRepo.save(grupo);

        UsuarioGrupo relacion = new UsuarioGrupo();
        relacion.setUsuario(creador);
        relacion.setGrupo(grupo);
        relacion.setEsModerador(true);
        usuarioGrupoRepo.save(relacion);

        return grupo;
    }


    public List<GrupoApoyo> listarGruposPublicos() {
        return grupoRepo.findByEsPrivadoFalse();
    }

    public GrupoApoyo buscarPorId(Long id) {
        return grupoRepo.findById(id).orElseThrow(() ->
                new IllegalArgumentException("Grupo no encontrado con id " + id));
    }

    private String generarCodigoUnico() {
        String codigo;
        do {
            codigo = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        } while (grupoRepo.existsByCodigoAcceso(codigo));
        return codigo;
    }

    public void eliminarGrupoSiEsCreador(Long grupoId, Usuario usuario) {
        GrupoApoyo grupo = grupoRepo.findById(grupoId)
                .orElseThrow(() -> new IllegalArgumentException("Grupo no encontrado"));

        if (!grupo.getCreador().getId().equals(usuario.getId())) {
            throw new SecurityException("Solo el creador del grupo puede eliminarlo.");
        }

        // Eliminar relaciones dependientes primero (orden importante)
        usuarioGrupoRepo.deleteAllByGrupo(grupo);
        postGrupoRepo.deleteAllByGrupo(grupo); // y en cascada, comentarios y reacciones

        grupoRepo.delete(grupo);
    }


    public List<GrupoApoyo> buscarTodos() {
        return grupoRepo.findAll();
    }


}
