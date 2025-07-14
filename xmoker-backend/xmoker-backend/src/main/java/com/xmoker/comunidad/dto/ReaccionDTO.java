package com.xmoker.comunidad.dto;

import com.xmoker.comunidad.entity.ReaccionGrupoPost;
import com.xmoker.comunidad.entity.TipoReaccion;



public class ReaccionDTO {
    private Long id;
    private TipoReaccion tipo;
    private Long usuarioId;
    private Long postId;


    public ReaccionDTO() { }

    public ReaccionDTO(ReaccionGrupoPost r) {
        this.id        = r.getId();
        this.tipo      = r.getTipo();
        this.usuarioId = r.getUsuario().getId();
        this.postId    = r.getPost().getId();
    }

    // getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public TipoReaccion getTipo() { return tipo; }
    public void setTipo(TipoReaccion tipo) { this.tipo = tipo; }
    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
    public Long getPostId() { return postId; }
    public void setPostId(Long postId) { this.postId = postId; }
}
