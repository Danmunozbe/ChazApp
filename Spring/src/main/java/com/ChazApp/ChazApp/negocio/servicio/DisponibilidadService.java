package com.ChazApp.ChazApp.negocio.servicio;

import com.ChazApp.ChazApp.negocio.dto.DisponibilidadProducto;
import com.ChazApp.ChazApp.negocio.excepcion.PedidoInvalidoException;
import com.ChazApp.ChazApp.negocio.excepcion.RecursoNoEncontradoException;
import com.ChazApp.ChazApp.persistencia.entidad.Producto;
import com.ChazApp.ChazApp.persistencia.repositorio.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * CU-04: Actualizar la disponibilidad de un producto (vendedor).
 * CU-04-M01: solo el vendedor de la chaza del producto puede cambiarla; aquí se
 * valida que el producto pertenezca a la chaza indicada en la petición.
 */
@Service
public class DisponibilidadService {

    private final ProductoRepository productoRepository;

    public DisponibilidadService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Transactional
    public DisponibilidadProducto cambiar(Long chazaId, Long productoId, boolean disponible) {
        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "El producto " + productoId + " no existe."));
        if (!producto.getChaza().getId().equals(chazaId)) {
            throw new PedidoInvalidoException(
                    "No puedes cambiar productos de otra chaza.");
        }
        producto.cambiarDisponibilidad(disponible);
        productoRepository.save(producto);
        return new DisponibilidadProducto(producto.getId(), producto.isDisponible());
    }
}
