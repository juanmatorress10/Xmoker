// src/test/java/com/xmoker/user/service/CuestionarioServiceTest.java
package com.xmoker.user.service;

import com.xmoker.user.dto.PerfilFumadorDTO;
import com.xmoker.user.dto.RespuestaDTO;
import com.xmoker.user.entity.CuestionarioRespuesta;
import com.xmoker.user.entity.Usuario;
import com.xmoker.user.repository.CuestionarioRespuestaRepository;
import com.xmoker.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CuestionarioServiceTest {

    @Mock
    private CuestionarioRespuestaRepository respuestaRepo;

    @Mock
    private UserRepository userRepo;

    @InjectMocks
    private CuestionarioService service;

    private final Long USUARIO_ID = 123L;

    @BeforeEach
    void setup() {
        // Siempre devolvemos un usuario válido
        Usuario usuario = new Usuario();
        usuario.setId(USUARIO_ID);
        when(userRepo.findById(USUARIO_ID)).thenReturn(Optional.of(usuario));
    }

    @Test
    void guardarRespuestas_eliminaPreviasYGuardaNuevas() {
        // Arrange
        RespuestaDTO dto1 = new RespuestaDTO();
        dto1.setCodigo("1");
        dto1.setValor(5);

        RespuestaDTO dto2 = new RespuestaDTO();
        dto2.setCodigo("7");
        dto2.setValor(3);

        List<RespuestaDTO> dtos = List.of(dto1, dto2);

        // Act
        service.guardarRespuestas(USUARIO_ID, dtos);

        // Assert: borra las previas y guarda las nuevas
        verify(respuestaRepo).deleteByUsuario_Id(USUARIO_ID);
        ArgumentCaptor<List<CuestionarioRespuesta>> captor =
                ArgumentCaptor.forClass(List.class);
        verify(respuestaRepo).saveAll(captor.capture());

        List<CuestionarioRespuesta> guardadas = captor.getValue();
        assertEquals(2, guardadas.size());
        assertEquals("1", guardadas.get(0).getCodigoPregunta());
        assertEquals(5, guardadas.get(0).getPuntuacion());
        assertEquals(USUARIO_ID, guardadas.get(0).getUsuario().getId());
    }

    @Test
    void analizarPerfil_sinRespuestas_retornaPerfilModerado() {
        // Arrange: no hay respuestas
        when(respuestaRepo.findByUsuarioId(USUARIO_ID)).thenReturn(List.of());

        // Act
        PerfilFumadorDTO dto = service.analizarPerfil(USUARIO_ID);

        // Assert: categorías vacías y usuario guardado con estilo "Moderado"
        assertTrue(dto.getCategoriasAltas().isEmpty());
        ArgumentCaptor<Usuario> userCaptor = ArgumentCaptor.forClass(Usuario.class);
        verify(userRepo).save(userCaptor.capture());
        assertEquals("Moderado", userCaptor.getValue().getEstiloFumador());
    }

    @Test
    void analizarPerfil_conRespuestas_retornaPerfilAdecuado() {
        // Arrange: simulamos respuestas para dos categorías
        CuestionarioRespuesta r1 = mkResp("1", 8);   // Estimulación (1)
        CuestionarioRespuesta r7 = mkResp("7", 5);   // Estimulación (7)
        CuestionarioRespuesta r5 = mkResp("5", 9);   // Adicción (5)
        CuestionarioRespuesta r11 = mkResp("11", 1); // Adicción (11)
        when(respuestaRepo.findByUsuarioId(USUARIO_ID))
                .thenReturn(List.of(r1, r7, r5, r11));

        // Act
        PerfilFumadorDTO dto = service.analizarPerfil(USUARIO_ID);

        // Assert: aparecen ambas categorías
        List<String> altas = dto.getCategoriasAltas();
        assertTrue(altas.contains("Estimulación"));
        assertTrue(altas.contains("Adicción"));

        Map<String,Integer> pts = dto.getPuntuaciones();
        assertEquals(13, pts.get("Estimulación"));
        assertEquals(10, pts.get("Adicción"));

        // Verificamos estilo sin depender del orden
        ArgumentCaptor<Usuario> uc = ArgumentCaptor.forClass(Usuario.class);
        verify(userRepo).save(uc.capture());
        String estilo = uc.getValue().getEstiloFumador();
        List<String> partes = Arrays.asList(estilo.split(",\\s*"));
        assertEquals(2, partes.size());
        assertTrue(partes.containsAll(List.of("Estimulación", "Adicción")));
    }

    /** Helper para crear instancias de CuestionarioRespuesta */
    private CuestionarioRespuesta mkResp(String codigo, int valor) {
        CuestionarioRespuesta r = new CuestionarioRespuesta();
        r.setCodigoPregunta(codigo);
        r.setPuntuacion(valor);
        return r;
    }
}
