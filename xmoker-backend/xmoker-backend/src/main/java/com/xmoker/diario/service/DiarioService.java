package com.xmoker.diario.service;

import com.xmoker.diario.entity.DiarioEntrada;
import com.xmoker.diario.repository.DiarioRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DiarioService {

    private final DiarioRepository diarioRepository;

    public DiarioService(DiarioRepository diarioRepository) {
        this.diarioRepository = diarioRepository;
    }

    public DiarioEntrada crearEntrada(Long usuarioId, String emocion, String estrategias, String complicaciones) {
        DiarioEntrada entrada = new DiarioEntrada();
        entrada.setUsuarioId(usuarioId);
        entrada.setFechaCreacion(LocalDateTime.now());
        entrada.setEmocionDelDia(emocion);
        entrada.setEstrategiasUsadas(estrategias);
        entrada.setComplicacionesEncontradas(complicaciones);
        return diarioRepository.save(entrada);
    }

    public List<DiarioEntrada> listarEntradas(Long usuarioId) {
        return diarioRepository.findByUsuarioId(usuarioId);
    }

    public void eliminarEntrada(Long idEntrada) {
        diarioRepository.deleteById(idEntrada);
    }
}