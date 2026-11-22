package com.ChazApp.ChazApp.negocio.servicio;

import com.ChazApp.ChazApp.negocio.dto.ChazaResumen;
import com.ChazApp.ChazApp.negocio.dto.DisponibilidadProducto;
import com.ChazApp.ChazApp.negocio.dto.MenuChaza;
import com.ChazApp.ChazApp.negocio.dto.MenuChaza.CategoriaMenu;
import com.ChazApp.ChazApp.negocio.dto.MenuChaza.ProductoMenu;
import com.ChazApp.ChazApp.negocio.excepcion.RecursoNoEncontradoException;
import com.ChazApp.ChazApp.persistencia.entidad.Categoria;
import com.ChazApp.ChazApp.persistencia.entidad.Chaza;
import com.ChazApp.ChazApp.persistencia.entidad.Producto;
import com.ChazApp.ChazApp.persistencia.repositorio.CategoriaRepository;
import com.ChazApp.ChazApp.persistencia.repositorio.ChazaRepository;
import com.ChazApp.ChazApp.persistencia.repositorio.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * CU-05: Consultar menú y disponibilidad por chaza.
 */
@Service
@Transactional(readOnly = true)
public class MenuService {

    private static final String CATEGORIA_SIN_NOMBRE = "Otros";

    private final ChazaRepository chazaRepository;
    private final CategoriaRepository categoriaRepository;
    private final ProductoRepository productoRepository;

    public MenuService(ChazaRepository chazaRepository,
                       CategoriaRepository categoriaRepository,
                       ProductoRepository productoRepository) {
        this.chazaRepository = chazaRepository;
        this.categoriaRepository = categoriaRepository;
        this.productoRepository = productoRepository;
    }

    /** Chazas abiertas con su número de productos disponibles. */
    public List<ChazaResumen> listarChazas() {
        return chazaRepository.findByActivaTrueOrderByIdAsc().stream()
                .map(c -> new ChazaResumen(c.getId(), c.getNombre(), c.getDescripcion(),
                        c.getUbicacion(), c.getImagenUrl(),
                        productoRepository.countByChaza_IdAndDisponibleTrue(c.getId())))
                .toList();
    }

    /** Menú de la chaza agrupado por categoría, incluyendo productos agotados. */
    public MenuChaza obtenerMenu(Long chazaId) {
        Chaza chaza = buscarChaza(chazaId);

        Map<Long, List<ProductoMenu>> porCategoria = new HashMap<>();
        List<ProductoMenu> sinCategoria = new ArrayList<>();
        for (Producto p : productoRepository.findByChaza_IdOrderByIdAsc(chazaId)) {
            ProductoMenu item = new ProductoMenu(p.getId(), p.getNombre(), p.getDescripcion(),
                    p.getPrecio(), p.isDisponible());
            if (p.getCategoria() == null) {
                sinCategoria.add(item);
            } else {
                porCategoria.computeIfAbsent(p.getCategoria().getId(), k -> new ArrayList<>()).add(item);
            }
        }

        List<CategoriaMenu> categorias = new ArrayList<>();
        for (Categoria c : categoriaRepository.findByChaza_IdOrderByOrdenAsc(chazaId)) {
            List<ProductoMenu> productos = porCategoria.get(c.getId());
            if (productos != null) {
                categorias.add(new CategoriaMenu(c.getNombre(), List.copyOf(productos)));
            }
        }
        if (!sinCategoria.isEmpty()) {
            categorias.add(new CategoriaMenu(CATEGORIA_SIN_NOMBRE, List.copyOf(sinCategoria)));
        }

        return new MenuChaza(chaza.getId(), chaza.getNombre(), chaza.getDescripcion(),
                chaza.getUbicacion(), chaza.getImagenUrl(), categorias);
    }

    /** Disponibilidad actual de cada producto; la consultan el menú y la vista del vendedor (CU-05-M01). */
    public List<DisponibilidadProducto> consultarDisponibilidad(Long chazaId) {
        buscarChaza(chazaId);
        return productoRepository.findByChaza_IdOrderByIdAsc(chazaId).stream()
                .map(p -> new DisponibilidadProducto(p.getId(), p.isDisponible()))
                .toList();
    }

    private Chaza buscarChaza(Long chazaId) {
        return chazaRepository.findById(chazaId)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "La chaza " + chazaId + " no existe."));
    }
}
