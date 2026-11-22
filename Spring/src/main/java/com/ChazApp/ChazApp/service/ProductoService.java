package com.ChazApp.ChazApp.service;

import com.ChazApp.ChazApp.exception.ChazaNotFoundException;
import com.ChazApp.ChazApp.exception.MenuNoPublicadoException;
import com.ChazApp.ChazApp.exception.ProductoNotFoundException;
import com.ChazApp.ChazApp.model.Chaza;
import com.ChazApp.ChazApp.model.Producto;
import com.ChazApp.ChazApp.repository.ChazaRepository;
import com.ChazApp.ChazApp.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final ChazaRepository chazaRepository;

    public ProductoService(ProductoRepository productoRepository, ChazaRepository chazaRepository) {
        this.productoRepository = productoRepository;
        this.chazaRepository = chazaRepository;
    }

    /**
     * Obtiene todos los productos asociados a una chaza (para el vendedor).
     * @throws ChazaNotFoundException si la chaza no existe
     */
    public List<Producto> obtenerProductosPorChaza(Long chazaId) {
        if (!chazaRepository.existsById(chazaId)) {
            throw new ChazaNotFoundException(chazaId);
        }
        return productoRepository.findByChazaId(chazaId);
    }

    /**
     * Obtiene los productos de una chaza para el estudiante (CU-05).
     * Valida la existencia de la chaza y que su menú esté publicado.
     * @throws ChazaNotFoundException si la chaza no existe
     * @throws MenuNoPublicadoException si la chaza no tiene el menú publicado
     */
    public List<Producto> obtenerProductosParaEstudiante(Long chazaId) {
        Chaza chaza = chazaRepository.findById(chazaId)
                .orElseThrow(() -> new ChazaNotFoundException(chazaId));

        if (!chaza.isMenuPublicado()) {
            throw new MenuNoPublicadoException("No hay un menú disponible para esta chaza");
        }

        return productoRepository.findByChazaId(chazaId);
    }

    /**
     * Obtiene un producto por su ID.
     * @throws ProductoNotFoundException si el producto no existe
     */
    public Producto obtenerPorId(Long productoId) {
        return productoRepository.findById(productoId)
                .orElseThrow(() -> new ProductoNotFoundException(productoId));
    }

    /**
     * Cambia el estado de disponibilidad del producto invirtiéndolo (!disponible) (CU-04).
     * @throws ProductoNotFoundException si el producto no existe
     */
    @Transactional
    public Producto cambiarDisponibilidad(Long productoId) {
        Producto producto = obtenerPorId(productoId);
        producto.setDisponible(!producto.isDisponible());
        return productoRepository.save(producto);
    }

    /**
     * Establece explícitamente el estado de disponibilidad del producto (CU-04).
     * @throws ProductoNotFoundException si el producto no existe
     */
    @Transactional
    public Producto cambiarDisponibilidad(Long productoId, boolean disponible) {
        Producto producto = obtenerPorId(productoId);
        producto.setDisponible(disponible);
        return productoRepository.save(producto);
    }
}
