// src/test/java/com/xmoker/consejo/service/ConsejoServiceTest.java
package com.xmoker.consejo.service;

import com.xmoker.consejo.entity.ConsejoDiario;
import com.xmoker.consejo.repository.ConsejoDiarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConsejoServiceTest {

    @Mock
    private ConsejoDiarioRepository consejoRepo;

    @InjectMocks
    private ConsejoService consejoService;

    @Test
    void obtenerPorDia_whenFound_returnsThatRecord() {
        // Arrange: simulamos un consejo para el día 5
        ConsejoDiario esperado = new ConsejoDiario();
        esperado.setDiaSugerido(5);
        when(consejoRepo.findByDiaSugerido(5)).thenReturn(Optional.of(esperado));

        // Act
        ConsejoDiario resultado = consejoService.obtenerPorDia(5);

        // Assert: debe devolver exactamente el objeto simulado y no llamar a obtenerAleatorio
        assertSame(esperado, resultado);
        verify(consejoRepo, times(1)).findByDiaSugerido(5);
    }

    @Test
    void obtenerPorDia_whenNotFound_delegatesToObtenerAleatorio() {
        // Arrange: simulamos sin consejo para el día y un consejo aleatorio
        when(consejoRepo.findByDiaSugerido(10)).thenReturn(Optional.empty());
        ConsejoService spyService = Mockito.spy(consejoService);
        ConsejoDiario aleatorio = new ConsejoDiario();
        doReturn(aleatorio).when(spyService).obtenerAleatorio();

        // Act
        ConsejoDiario resultado = spyService.obtenerPorDia(10);

        // Assert: al no encontrar, debe invocar obtenerAleatorio
        assertSame(aleatorio, resultado);
        verify(spyService, times(1)).obtenerAleatorio();
    }

    @Test
    void obtenerAleatorio_whenEmptyList_returnsNull() {
        // Arrange: repositorio sin registros
        when(consejoRepo.findAll()).thenReturn(Collections.emptyList());

        // Act
        ConsejoDiario resultado = consejoService.obtenerAleatorio();

        // Assert: con lista vacía, devuelve null
        assertNull(resultado);
        verify(consejoRepo, times(1)).findAll();
    }

    @Test
    void obtenerAleatorio_whenOneElement_returnsThatElement() {
        // Arrange: repositorio con un solo consejo
        ConsejoDiario unico = new ConsejoDiario();
        when(consejoRepo.findAll()).thenReturn(List.of(unico));

        // Act
        ConsejoDiario resultado = consejoService.obtenerAleatorio();

        // Assert: con un elemento, siempre lo devuelve
        assertSame(unico, resultado);
    }

    @Test
    void obtenerPorCategoria_inputLowercase_convertsToUppercaseAndReturnsList() {
        // Arrange: simulamos lista resultante
        List<ConsejoDiario> lista = List.of(new ConsejoDiario(), new ConsejoDiario());
        when(consejoRepo.findByCategoria("SALUD")).thenReturn(lista);

        // Act
        List<ConsejoDiario> resultado = consejoService.obtenerPorCategoria("salud");

        // Assert: debe llamar con la cadena en mayúsculas
        assertEquals(2, resultado.size());
        verify(consejoRepo, times(1)).findByCategoria("SALUD");
    }
}
