package com.xmoker.comunidad.service;

import com.xmoker.comunidad.dto.EstadisticasGrupoDTO;
import com.xmoker.comunidad.entity.GrupoApoyo;
import com.xmoker.comunidad.entity.UsuarioGrupo;
import com.xmoker.comunidad.entity.RetoGrupo;
import com.xmoker.comunidad.entity.PostGrupo;
import com.xmoker.comunidad.repository.UsuarioGrupoRepository;
import com.xmoker.comunidad.repository.PostGrupoRepository;
import com.xmoker.comunidad.repository.RetoGrupoRepository;
import com.xmoker.user.entity.Progreso;
import com.xmoker.user.entity.Usuario;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class EstadisticasGrupoServiceTest {

    @Mock
    private UsuarioGrupoRepository usuarioGrupoRepo;

    @Mock
    private PostGrupoRepository postRepo;

    @Mock
    private RetoGrupoRepository retoRepo;

    @InjectMocks
    private EstadisticasGrupoService estadisticasGrupoService;

    @Test
    void calcularEstadisticas_grupoConDatos_retornaValoresEsperados() {
        // --- Arrange ---

        // 1. Configuramos un grupo con ID 1L
        GrupoApoyo grupo = new GrupoApoyo();
        grupo.setId(1L);

        // 2. Simulamos un único miembro con su progreso
        Usuario usuario = new Usuario();
        Progreso progreso = new Progreso();
        progreso.setDiasSinFumar(10);
        progreso.setRachaActual(3);
        progreso.setRachaMaxima(5);
        usuario.setProgreso(progreso);

        UsuarioGrupo usuarioGrupo = new UsuarioGrupo();
        usuarioGrupo.setUsuario(usuario);
        when(usuarioGrupoRepo.findByGrupo(grupo))
                .thenReturn(List.of(usuarioGrupo));

        // 3. Simulamos 2 publicaciones en el foro
        PostGrupo post = new PostGrupo();
        when(postRepo.findByGrupo(grupo))
                .thenReturn(List.of(post, post));

        // 4. Creamos 2 retos: uno con fechaFin futura (activo), otro pasada (inactivo)
        RetoGrupo activo = new RetoGrupo();
        activo.setFechaFin(LocalDate.now().plusDays(1)); // futuro = activo
        RetoGrupo inactivo = new RetoGrupo();
        inactivo.setFechaFin(LocalDate.now().minusDays(1)); // pasado = inactivo
        when(retoRepo.findByGrupo(grupo))
                .thenReturn(List.of(activo, inactivo));

        // --- Act ---
        EstadisticasGrupoDTO dto = estadisticasGrupoService.calcularEstadisticas(grupo);

        // --- Assert ---

        // ID del grupo
        assertEquals(1L, dto.getGrupoId());

        // Miembros y días totales
        assertEquals(1, dto.getTotalMiembros());
        assertEquals(10, dto.getTotalDiasSinFumar());

        // Publicaciones
        assertEquals(2, dto.getTotalPublicaciones());

        // Retos totales vs activos
        assertEquals(2, dto.getTotalRetos());
        assertEquals(1, dto.getRetosActivos()); // getter correcto

        // Media de rachas
        assertEquals(3.0, dto.getMediaRachaActual());
        assertEquals(5.0, dto.getMediaRachaMaxima());
    }
}
