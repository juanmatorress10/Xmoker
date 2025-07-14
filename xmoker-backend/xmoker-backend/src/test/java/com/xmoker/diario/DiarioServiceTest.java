package com.xmoker.diario.service;

import com.xmoker.diario.entity.DiarioEntrada;
import com.xmoker.diario.repository.DiarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DiarioServiceTest {

    @Mock
    private DiarioRepository diarioRepository;

    @InjectMocks
    private DiarioService diarioService;

    @Test
    void crearEntrada_datosValidos_guardaYDevuelveEntrada() {
        // Arrange: preparamos los datos de entrada
        Long usuarioId = 42L;
        String emocion = "Feliz";
        String estrategias = "Respiración";
        String complicaciones = "Ninguna";

        // Simulamos que el repositorio devuelve la misma entidad
        when(diarioRepository.save(any(DiarioEntrada.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        // Act: llamamos al método crearEntrada()
        DiarioEntrada resultado = diarioService.crearEntrada(usuarioId, emocion, estrategias, complicaciones);

        // Assert: verificamos los campos de la entrada
        assertNotNull(resultado, "Debe devolver un objeto no nulo");
        assertEquals(usuarioId, resultado.getUsuarioId(), "UsuarioId debe coincidir");
        assertEquals(emocion, resultado.getEmocionDelDia(), "Emoción debe coincidir");
        assertEquals(estrategias, resultado.getEstrategiasUsadas(), "Estrategias deben coincidir");
        assertEquals(complicaciones, resultado.getComplicacionesEncontradas(), "Complicaciones deben coincidir");
        assertNotNull(resultado.getFechaCreacion(), "Fecha de creación no debe ser nula");
        // Verificamos que se llamó a save() en el repositorio
        verify(diarioRepository, times(1)).save(any(DiarioEntrada.class));
    }

    @Test
    void listarEntradas_usuarioExistente_devuelveLista() {
        // Arrange: simulamos dos entradas en el repositorio
        DiarioEntrada e1 = new DiarioEntrada(); e1.setId(1L);
        DiarioEntrada e2 = new DiarioEntrada(); e2.setId(2L);
        when(diarioRepository.findByUsuarioId(7L)).thenReturn(List.of(e1, e2));

        // Act: llamamos a listarEntradas()
        List<DiarioEntrada> lista = diarioService.listarEntradas(7L);

        // Assert: debe devolver exactamente las dos entradas
        assertEquals(2, lista.size(), "Deben devolverse dos entradas");
        assertEquals(1L, lista.get(0).getId());
        assertEquals(2L, lista.get(1).getId());
    }

    @Test
    void eliminarEntrada_idValido_llamaDeleteById() {
        // Arrange: definimos un ID de entrada a eliminar
        Long idEntrada = 99L;
        doNothing().when(diarioRepository).deleteById(idEntrada);

        // Act: llamamos a eliminarEntrada()
        diarioService.eliminarEntrada(idEntrada);

        // Assert: verificamos que deleteById fue invocado una vez con el ID correcto
        verify(diarioRepository, times(1)).deleteById(idEntrada);
    }
}
