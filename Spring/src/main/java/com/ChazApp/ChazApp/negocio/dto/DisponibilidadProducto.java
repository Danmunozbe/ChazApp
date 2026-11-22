package com.ChazApp.ChazApp.negocio.dto;

/** Estado de disponibilidad de un producto, usado en el refresco periódico (CU-05-M01). */
public record DisponibilidadProducto(Long productoId, boolean disponible) {
}
