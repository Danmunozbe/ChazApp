package com.ChazApp.ChazApp.negocio.dto;

/** Datos de una chaza para el listado inicial (CU-05). */
public record ChazaResumen(
        Long id,
        String nombre,
        String descripcion,
        String ubicacion,
        String imagenUrl,
        long productosDisponibles) {
}
