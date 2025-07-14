package com.xmoker.user.service;

import com.xmoker.user.entity.CuestionarioRespuesta;
import com.xmoker.user.entity.Usuario;
import com.xmoker.user.repository.CuestionarioRespuestaRepository;
import com.xmoker.user.repository.UserRepository;
import com.xmoker.user.dto.PerfilFumadorDTO;
import com.xmoker.user.dto.RespuestaDTO;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CuestionarioService {

    private final CuestionarioRespuestaRepository respuestaRepo;
    private final UserRepository userRepo;

    public CuestionarioService(CuestionarioRespuestaRepository respuestaRepo, UserRepository userRepo) {
        this.respuestaRepo = respuestaRepo;
        this.userRepo = userRepo;
    }

    @Transactional
    public void guardarRespuestas(Long usuarioId, List<RespuestaDTO> respuestasDTO) {
        Usuario usuario = userRepo.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        List<CuestionarioRespuesta> respuestas = respuestasDTO.stream()
                .map(dto -> {
                    CuestionarioRespuesta r = new CuestionarioRespuesta();
                    r.setUsuario(usuario);
                    r.setCodigoPregunta(dto.getCodigo());
                    r.setPuntuacion(dto.getValor());
                    return r;
                })
                .collect(Collectors.toList());

        respuestaRepo.deleteByUsuario_Id(usuarioId);
        respuestaRepo.saveAll(respuestas);
    }

    public PerfilFumadorDTO analizarPerfil(Long usuarioId) {
        Map<String, Integer> puntuaciones = calcularCategorias(usuarioId);

        List<String> destacadas = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : puntuaciones.entrySet()) {
            if (entry.getValue() >= 8) {
                destacadas.add(entry.getKey());
            }
        }

        String mensaje = obtenerMensajeCombinado(destacadas);

        Usuario usuario = userRepo.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        String estilo = destacadas.isEmpty() ? "Moderado" : String.join(", ", destacadas);
        usuario.setEstiloFumador(estilo);
        usuario.setDescripcionEstiloFumador(mensaje);
        userRepo.save(usuario);

        PerfilFumadorDTO dto = new PerfilFumadorDTO();
        dto.setPuntuaciones(puntuaciones);
        dto.setCategoriasAltas(destacadas);
        dto.setMensajeResumen(mensaje);

        return dto;
    }

    public Map<String, Integer> calcularCategorias(Long usuarioId) {
        List<CuestionarioRespuesta> respuestas = respuestaRepo.findByUsuarioId(usuarioId);

        Map<String, List<Integer>> categorias = Map.of(
                "Estimulación", List.of(1, 7, 18),
                "Refuerzo gestual", List.of(2, 8, 14),
                "Placer-relajación", List.of(3, 9, 15),
                "Reducción estados negativos", List.of(4, 10, 16),
                "Adicción", List.of(5, 11, 17),
                "Automatismo", List.of(6, 12, 13)
        );

        Map<String, Integer> resultado = new LinkedHashMap<>();

        for (var entry : categorias.entrySet()) {
            int suma = respuestas.stream()
                    .filter(r -> entry.getValue().contains(Integer.parseInt(r.getCodigoPregunta())))
                    .mapToInt(CuestionarioRespuesta::getPuntuacion)
                    .sum();
            resultado.put(entry.getKey(), suma);
        }

        return resultado;
    }

    private String obtenerMensajeCombinado(List<String> categorias) {
        if (categorias.isEmpty()) {
            return "✅ Tus motivos para fumar son variados y no muy intensos. ¡Es un buen punto de partida para dejarlo!";
        }

        StringBuilder mensaje = new StringBuilder("🔎 Según tus respuestas, tus principales motivos para fumar son:\n\n");

        for (String categoria : categorias) {
            mensaje.append("🔹 ").append(categoria).append(": ")
                    .append(descripcionCategoria(categoria)).append("\n\n");
        }

        mensaje.append("🌟 Conociendo mejor tus motivos, podrás planificar estrategias más efectivas para dejar de fumar. ¡Cada paso cuenta!");

        return mensaje.toString();
    }

    private String descripcionCategoria(String categoria) {
        return switch (categoria) {
            case "Estimulación" -> "Usas el cigarrillo para activarte o mantener la concentración. Hay formas más sanas de recargar energía, como pausas activas o respiraciones profundas.";
            case "Refuerzo gestual" -> "La gestualidad de fumar te aporta seguridad o calma. Puedes reemplazarlo por sostener otro objeto o cambiar pequeños hábitos.";
            case "Placer-relajación" -> "Fumar te proporciona momentos placenteros o relajantes. Existen otras formas de relajarte como caminar, escuchar música o meditar.";
            case "Reducción estados negativos" -> "Tiendes a fumar para manejar el estrés o la tristeza. Aprender a regular estas emociones de forma sana será un gran avance para ti.";
            case "Adicción" -> "Tienes una fuerte dependencia física del tabaco. ¡Pero no te preocupes! Con apoyo y constancia puedes superarlo paso a paso.";
            case "Automatismo" -> "Fumas casi sin darte cuenta. Detectar estos hábitos automáticos y romper esas rutinas es clave para tu éxito.";
            default -> "Tu relación con el tabaco es compleja, pero eso también te da más puntos donde actuar para cambiar.";
        };
    }
}
