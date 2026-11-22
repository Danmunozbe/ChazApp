package com.ChazApp.ChazApp.presentacion.api;

import jakarta.validation.constraints.NotNull;

/** Cuerpo para CU-04: el vendedor marca un producto como disponible o agotado. */
public record CambioDisponibilidadRequest(
        @NotNull(message = "Indica si el producto queda disponible o no.")
        Boolean disponible) {
}
