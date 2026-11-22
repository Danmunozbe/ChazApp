package com.ChazApp.ChazApp.negocio.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * Carrito que envía el estudiante al confirmar (CU-01).
 * Puede mezclar productos de varias chazas; el servicio crea un pedido por chaza.
 */
public record SolicitudPedido(
        @NotBlank(message = "Escribe tu nombre para identificar el pedido.")
        @Size(max = 80, message = "El nombre no puede tener más de 80 caracteres.")
        String nombreEstudiante,

        @NotEmpty(message = "El carrito no tiene productos.")
        @Size(max = 60, message = "El carrito tiene demasiados productos distintos.")
        List<@Valid @NotNull ItemSolicitud> items) {

    public record ItemSolicitud(
            @NotNull(message = "Falta el producto en un ítem del carrito.")
            Long productoId,

            @NotNull(message = "Falta la cantidad en un ítem del carrito.")
            @Min(value = 1, message = "La cantidad mínima por producto es 1.")
            @Max(value = 20, message = "La cantidad máxima por producto es 20.")
            Integer cantidad) {
    }
}
