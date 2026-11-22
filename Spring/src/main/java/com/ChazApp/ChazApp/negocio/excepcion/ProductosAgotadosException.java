package com.ChazApp.ChazApp.negocio.excepcion;

import java.util.List;

/**
 * El pedido incluye productos agotados (CU-01-M01). Lleva la lista para que
 * la presentación le avise al estudiante cuáles debe quitar.
 */
public class ProductosAgotadosException extends RuntimeException {

    public record ProductoAgotado(Long productoId, String nombre) {
    }

    private final List<ProductoAgotado> productosAgotados;

    public ProductosAgotadosException(List<ProductoAgotado> productosAgotados) {
        super(construirMensaje(productosAgotados));
        this.productosAgotados = List.copyOf(productosAgotados);
    }

    public List<ProductoAgotado> getProductosAgotados() {
        return productosAgotados;
    }

    private static String construirMensaje(List<ProductoAgotado> agotados) {
        String nombres = String.join(", ", agotados.stream().map(ProductoAgotado::nombre).toList());
        return (agotados.size() == 1 ? "Este producto se agotó: " : "Estos productos se agotaron: ")
                + nombres + ". Quítalos del pedido para poder confirmarlo.";
    }
}
