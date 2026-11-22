package com.ChazApp.ChazApp.negocio.dto;

import java.util.List;

/** Menú completo de una chaza agrupado por categoría (CU-05). */
public record MenuChaza(
        Long chazaId,
        String nombre,
        String descripcion,
        String ubicacion,
        String imagenUrl,
        List<CategoriaMenu> categorias) {

    public record CategoriaMenu(String nombre, List<ProductoMenu> productos) {
    }

    public record ProductoMenu(Long id, String nombre, String descripcion, int precio, boolean disponible) {
    }
}
