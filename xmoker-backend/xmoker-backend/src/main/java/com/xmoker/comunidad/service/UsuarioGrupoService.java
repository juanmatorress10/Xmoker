package com.xmoker.comunidad.service;

import com.xmoker.comunidad.entity.GrupoApoyo;
import com.xmoker.comunidad.entity.UsuarioGrupo;
import com.xmoker.comunidad.repository.GrupoApoyoRepository;
import com.xmoker.comunidad.repository.UsuarioGrupoRepository;
import com.xmoker.user.entity.Usuario;
import com.xmoker.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioGrupoService {

    @Autowired
    private GrupoApoyoRepository grupoRepo;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UsuarioGrupoRepository usuarioGrupoRepo;

    public UsuarioGrupo unirseAGrupo(Usuario usuario, GrupoApoyo grupo) {
        boolean yaMiembro = usuarioGrupoRepo.findByUsuarioAndGrupo(usuario, grupo).isPresent();
        if (yaMiembro) throw new IllegalStateException("Ya eres miembro del grupo");

        UsuarioGrupo nuevo = new UsuarioGrupo();
        nuevo.setUsuario(usuario);
        nuevo.setGrupo(grupo);
        nuevo.setEsModerador(false);

        return usuarioGrupoRepo.save(nuevo);
    }

    public List<UsuarioGrupo> gruposDeUsuario(Usuario usuario) {
        return usuarioGrupoRepo.findByUsuario(usuario);
    }

    public boolean esMiembro(Usuario usuario, GrupoApoyo grupo) {
        return usuarioGrupoRepo.findByUsuarioAndGrupo(usuario, grupo).isPresent();
    }

    public UsuarioGrupo unirsePorCodigo(String codigo, Usuario usuario) {
        // 1) Busca el grupo por código
        GrupoApoyo grupo = grupoRepo.findByCodigoAcceso(codigo)
                .orElseThrow(() -> new IllegalArgumentException("Código de grupo no válido."));

        // 2) Sólo impide re-unirse al MISMO grupo, no a cualquiera
        boolean yaEsMiembro = usuarioGrupoRepo.findByUsuarioAndGrupo(usuario, grupo).isPresent();
        if (yaEsMiembro) {
            throw new IllegalStateException("Ya formas parte de este grupo.");
        }

        // 3) Crea la relación
        UsuarioGrupo nuevo = new UsuarioGrupo();
        nuevo.setUsuario(usuario);
        nuevo.setGrupo(grupo);
        nuevo.setEsModerador(false);

        return usuarioGrupoRepo.save(nuevo);
    }

    public void eliminarUsuarioDelGrupo(Long grupoId, Long usuarioId) {
        GrupoApoyo grupo = grupoRepo.findById(grupoId)
                .orElseThrow(() -> new IllegalArgumentException("Grupo no encontrado"));

        Usuario usuario = userRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        UsuarioGrupo relacion = usuarioGrupoRepo.findByUsuarioAndGrupo(usuario, grupo)
                .orElseThrow(() -> new IllegalArgumentException("El usuario no pertenece al grupo"));

        usuarioGrupoRepo.delete(relacion);
    }

    public List<Usuario> listarMiembros(Long grupoId) {
        GrupoApoyo grupo = grupoRepo.findById(grupoId)
                .orElseThrow(() -> new IllegalArgumentException("Grupo no encontrado"));

        return usuarioGrupoRepo.findByGrupo(grupo).stream()
                .map(UsuarioGrupo::getUsuario)
                .toList();
    }

    @Transactional
    public void abandonarGrupo(Usuario usuario, Long grupoId) {
        // 1) recuperar el grupo
        GrupoApoyo grupo = grupoRepo.findById(grupoId)
                .orElseThrow(() -> new IllegalArgumentException("Grupo no encontrado"));

        // 2) buscar relación
        UsuarioGrupo rel = usuarioGrupoRepo
                .findByUsuarioAndGrupo(usuario, grupo)
                .orElseThrow(() -> new IllegalStateException("No eres miembro de ese grupo"));

        // 3) borrarla
        usuarioGrupoRepo.delete(rel);

    }
}
