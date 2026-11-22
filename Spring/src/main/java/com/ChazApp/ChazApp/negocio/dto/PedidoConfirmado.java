package com.ChazApp.ChazApp.negocio.dto;

import java.time.LocalDateTime;
import java.util.List;

/** Resultado de un pedido registrado en la cola de la chaza (CU-01). */
public record PedidoConfirmado(
        Long id,
        Long chazaId,
        String chazaNombre,
        String nombreEstudiante,
        LocalDateTime fechaHora,
        String estado,
        List<ItemConfirmado> items,
        int total) {

    public record ItemConfirmado(String producto, int cantidad, int precioUnitario, int subtotal) {
    }
}
