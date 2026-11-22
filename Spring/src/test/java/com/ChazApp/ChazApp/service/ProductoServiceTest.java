package com.ChazApp.ChazApp.service;

import com.ChazApp.ChazApp.exception.ChazaNotFoundException;
import com.ChazApp.ChazApp.exception.MenuNoPublicadoException;
import com.ChazApp.ChazApp.exception.ProductoNotFoundException;
import com.ChazApp.ChazApp.model.Chaza;
import com.ChazApp.ChazApp.model.Producto;
import com.ChazApp.ChazApp.repository.ChazaRepository;
import com.ChazApp.ChazApp.repository.ProductoRepository;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas Unitarias - ProductoService")
class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private ChazaRepository chazaRepository;

    @InjectMocks
    private ProductoService productoService;

    private Chaza chazaPublicada;
    private Chaza chazaNoPublicada;
    private Producto productoDisponible;
    private Producto productoAgotado;

    @BeforeEach
    void setUp() {
        chazaPublicada = Chaza.builder()
                .id(1L)
                .nombre("Burritos UNAL")
                .menuPublicado(true)
                .activa(true)
                .build();

        chazaNoPublicada = Chaza.builder()
                .id(3L)
                .nombre("Salchipapas UNAL")
                .menuPublicado(false)
                .activa(true)
                .build();

        productoDisponible = Producto.builder()
                .id(101L)
                .chaza(chazaPublicada)
                .nombre("Burrito de Lomo")
                .descripcion("Lomo salteado con frijoles")
                .categoria("Burritos")
                .precio(11000.0)
                .disponible(true)
                .build();

        productoAgotado = Producto.builder()
                .id(102L)
                .chaza(chazaPublicada)
                .nombre("Burrito de Chorizo")
                .descripcion("Chorizo santarosano")
                .categoria("Burritos")
                .precio(11000.0)
                .disponible(false)
                .build();
    }

    @Test
    @DisplayName("obtenerProductosPorChaza() cuando chaza existe debe retornar sus productos")
    void obtenerProductosPorChaza_ChazaExiste_RetornaProductos() {
        when(chazaRepository.existsById(1L)).thenReturn(true);
        when(productoRepository.findByChazaId(1L)).thenReturn(Arrays.asList(productoDisponible, productoAgotado));

        List<Producto> productos = productoService.obtenerProductosPorChaza(1L);

        assertNotNull(productos);
        assertEquals(2, productos.size());
        verify(chazaRepository, times(1)).existsById(1L);
        verify(productoRepository, times(1)).findByChazaId(1L);
    }

    @Test
    @DisplayName("obtenerProductosPorChaza() cuando chaza no existe debe lanzar ChazaNotFoundException")
    void obtenerProductosPorChaza_ChazaNoExiste_LanzaChazaNotFoundException() {
        when(chazaRepository.existsById(99L)).thenReturn(false);

        assertThrows(ChazaNotFoundException.class, () -> productoService.obtenerProductosPorChaza(99L));
        verify(chazaRepository, times(1)).existsById(99L);
        verify(productoRepository, never()).findByChazaId(anyLong());
    }

    @Test
    @DisplayName("obtenerProductosParaEstudiante() con menú publicado debe retornar productos (CU-05)")
    void obtenerProductosParaEstudiante_MenuPublicado_Exito() {
        when(chazaRepository.findById(1L)).thenReturn(Optional.of(chazaPublicada));
        when(productoRepository.findByChazaId(1L)).thenReturn(Arrays.asList(productoDisponible, productoAgotado));

        List<Producto> productos = productoService.obtenerProductosParaEstudiante(1L);

        assertNotNull(productos);
        assertEquals(2, productos.size());
        verify(chazaRepository, times(1)).findById(1L);
        verify(productoRepository, times(1)).findByChazaId(1L);
    }

    @Test
    @DisplayName("obtenerProductosParaEstudiante() cuando menú NO está publicado debe lanzar MenuNoPublicadoException (CU-05)")
    void obtenerProductosParaEstudiante_MenuNoPublicado_LanzaMenuNoPublicadoException() {
        when(chazaRepository.findById(3L)).thenReturn(Optional.of(chazaNoPublicada));

        MenuNoPublicadoException ex = assertThrows(MenuNoPublicadoException.class,
                () -> productoService.obtenerProductosParaEstudiante(3L));

        assertTrue(ex.getMessage().contains("No hay un menú disponible para esta chaza"));
        verify(chazaRepository, times(1)).findById(3L);
        verify(productoRepository, never()).findByChazaId(anyLong());
    }

    @Test
    @DisplayName("obtenerProductosParaEstudiante() cuando chaza no existe debe lanzar ChazaNotFoundException")
    void obtenerProductosParaEstudiante_ChazaNoExiste_LanzaChazaNotFoundException() {
        when(chazaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ChazaNotFoundException.class, () -> productoService.obtenerProductosParaEstudiante(99L));
        verify(chazaRepository, times(1)).findById(99L);
        verify(productoRepository, never()).findByChazaId(anyLong());
    }

    @Test
    @DisplayName("obtenerPorId() cuando producto existe debe retornar el producto")
    void obtenerPorId_Existe_RetornaProducto() {
        when(productoRepository.findById(101L)).thenReturn(Optional.of(productoDisponible));

        Producto resultado = productoService.obtenerPorId(101L);

        assertNotNull(resultado);
        assertEquals(101L, resultado.getId());
        assertEquals("Burrito de Lomo", resultado.getNombre());
        verify(productoRepository, times(1)).findById(101L);
    }

    @Test
    @DisplayName("obtenerPorId() cuando producto no existe debe lanzar ProductoNotFoundException")
    void obtenerPorId_NoExiste_LanzaProductoNotFoundException() {
        when(productoRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ProductoNotFoundException.class, () -> productoService.obtenerPorId(999L));
        verify(productoRepository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("cambiarDisponibilidad() toggle de Disponible (true) a Agotado (false) (CU-04)")
    void cambiarDisponibilidad_ToggleDisponibleAAgotado_Exito() {
        when(productoRepository.findById(101L)).thenReturn(Optional.of(productoDisponible));
        when(productoRepository.save(any(Producto.class))).thenAnswer(invocation -> invocation.getArgument(0));

        assertTrue(productoDisponible.isDisponible());

        Producto resultado = productoService.cambiarDisponibilidad(101L);

        assertNotNull(resultado);
        assertFalse(resultado.isDisponible(), "El producto ahora debe estar no disponible (Agotado)");
        verify(productoRepository, times(1)).findById(101L);
        verify(productoRepository, times(1)).save(productoDisponible);
    }

    @Test
    @DisplayName("cambiarDisponibilidad() toggle de Agotado (false) a Disponible (true) (CU-04)")
    void cambiarDisponibilidad_ToggleAgotadoADisponible_Exito() {
        when(productoRepository.findById(102L)).thenReturn(Optional.of(productoAgotado));
        when(productoRepository.save(any(Producto.class))).thenAnswer(invocation -> invocation.getArgument(0));

        assertFalse(productoAgotado.isDisponible());

        Producto resultado = productoService.cambiarDisponibilidad(102L);

        assertNotNull(resultado);
        assertTrue(resultado.isDisponible(), "El producto ahora debe estar disponible");
        verify(productoRepository, times(1)).findById(102L);
        verify(productoRepository, times(1)).save(productoAgotado);
    }

    @Test
    @DisplayName("cambiarDisponibilidad() cuando producto no existe debe lanzar ProductoNotFoundException")
    void cambiarDisponibilidad_ProductoNoExiste_LanzaProductoNotFoundException() {
        when(productoRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ProductoNotFoundException.class, () -> productoService.cambiarDisponibilidad(999L));
        verify(productoRepository, times(1)).findById(999L);
        verify(productoRepository, never()).save(any(Producto.class));
    }

    @Test
    @DisplayName("cambiarDisponibilidad(id, boolean) fija explícitamente el estado de disponibilidad")
    void cambiarDisponibilidad_ParametroExplicito_Exito() {
        when(productoRepository.findById(101L)).thenReturn(Optional.of(productoDisponible));
        when(productoRepository.save(any(Producto.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Producto resultado = productoService.cambiarDisponibilidad(101L, false);

        assertNotNull(resultado);
        assertFalse(resultado.isDisponible());
        verify(productoRepository, times(1)).findById(101L);
        verify(productoRepository, times(1)).save(productoDisponible);
    }
}
