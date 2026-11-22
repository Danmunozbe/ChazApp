package com.ChazApp.ChazApp.service;

import com.ChazApp.ChazApp.exception.ChazaNotFoundException;
import com.ChazApp.ChazApp.exception.MenuNoPublicadoException;
import com.ChazApp.ChazApp.model.Chaza;
import com.ChazApp.ChazApp.repository.ChazaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas Unitarias - ChazaService")
class ChazaServiceTest {

    @Mock
    private ChazaRepository chazaRepository;

    @InjectMocks
    private ChazaService chazaService;

    private Chaza chazaConMenu;
    private Chaza chazaSinMenu;

    @BeforeEach
    void setUp() {
        chazaConMenu = Chaza.builder()
                .id(1L)
                .nombre("Burritos UNAL")
                .descripcion("Burritos deliciosos")
                .ubicacion("Enfermería")
                .imagenUrl("/img/burritos_banner.jpg")
                .menuPublicado(true)
                .activa(true)
                .build();

        chazaSinMenu = Chaza.builder()
                .id(2L)
                .nombre("Salchipapas UNAL")
                .descripcion("Salchipapas de todo tipo")
                .ubicacion("Canchas")
                .imagenUrl("/img/salchipapas_banner.png")
                .menuPublicado(false)
                .activa(true)
                .build();
    }

    @Test
    @DisplayName("obtenerChazasActivas() debe retornar lista de chazas con activa=true")
    void obtenerChazasActivas_Exito() {
        when(chazaRepository.findByActivaTrue()).thenReturn(Arrays.asList(chazaConMenu, chazaSinMenu));

        List<Chaza> resultado = chazaService.obtenerChazasActivas();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(chazaRepository, times(1)).findByActivaTrue();
    }

    @Test
    @DisplayName("obtenerChazaPorId() cuando existe debe retornar la chaza")
    void obtenerChazaPorId_Existe_RetornaChaza() {
        when(chazaRepository.findById(1L)).thenReturn(Optional.of(chazaConMenu));

        Chaza resultado = chazaService.obtenerChazaPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Burritos UNAL", resultado.getNombre());
        verify(chazaRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("obtenerChazaPorId() cuando no existe debe lanzar ChazaNotFoundException")
    void obtenerChazaPorId_NoExiste_LanzaChazaNotFoundException() {
        when(chazaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ChazaNotFoundException.class, () -> chazaService.obtenerChazaPorId(99L));
        verify(chazaRepository, times(1)).findById(99L);
    }

    @Test
    @DisplayName("obtenerChazaParaEstudiante() cuando menú está publicado debe retornar la chaza exitosamente")
    void obtenerChazaParaEstudiante_MenuPublicado_Exito() {
        when(chazaRepository.findById(1L)).thenReturn(Optional.of(chazaConMenu));

        Chaza resultado = chazaService.obtenerChazaParaEstudiante(1L);

        assertNotNull(resultado);
        assertTrue(resultado.isMenuPublicado());
        assertEquals("Burritos UNAL", resultado.getNombre());
        verify(chazaRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("obtenerChazaParaEstudiante() cuando menú NO está publicado debe lanzar MenuNoPublicadoException")
    void obtenerChazaParaEstudiante_MenuNoPublicado_LanzaMenuNoPublicadoException() {
        when(chazaRepository.findById(2L)).thenReturn(Optional.of(chazaSinMenu));

        MenuNoPublicadoException ex = assertThrows(MenuNoPublicadoException.class,
                () -> chazaService.obtenerChazaParaEstudiante(2L));

        assertTrue(ex.getMessage().contains("No hay un menú disponible para esta chaza"));
        verify(chazaRepository, times(1)).findById(2L);
    }

    @Test
    @DisplayName("obtenerChazaParaEstudiante() cuando chaza no existe debe lanzar ChazaNotFoundException")
    void obtenerChazaParaEstudiante_ChazaNoExiste_LanzaChazaNotFoundException() {
        when(chazaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ChazaNotFoundException.class, () -> chazaService.obtenerChazaParaEstudiante(99L));
        verify(chazaRepository, times(1)).findById(99L);
    }

    @Test
    @DisplayName("tieneMenuPublicado() debe retornar true si menu_publicado es true")
    void tieneMenuPublicado_RetornaTrue() {
        when(chazaRepository.findById(1L)).thenReturn(Optional.of(chazaConMenu));

        boolean resultado = chazaService.tieneMenuPublicado(1L);

        assertTrue(resultado);
        verify(chazaRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("tieneMenuPublicado() debe retornar false si menu_publicado es false")
    void tieneMenuPublicado_RetornaFalse() {
        when(chazaRepository.findById(2L)).thenReturn(Optional.of(chazaSinMenu));

        boolean resultado = chazaService.tieneMenuPublicado(2L);

        assertFalse(resultado);
        verify(chazaRepository, times(1)).findById(2L);
    }
}
