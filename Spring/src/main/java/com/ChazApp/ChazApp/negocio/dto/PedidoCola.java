package com.ChazApp.ChazApp.negocio.dto;

import java.time.LocalDateTime;
import java.util.List;

/** Un pedido tal como lo ve el vendedor en su cola (CU-02). */
public record PedidoCola(
        Long id,
        String nombreEstudiante,
        LocalDateTime fechaHora,
        String estado,
        String estadoCodigo,
        String siguienteEstado,
        String siguienteEstadoCodigo,
        boolean puedeAvanzar,
        boolean puedeMarcarListo,
        List<ItemCola> items,
        int total) {

    public record ItemCola(String producto, int cantidad) {
    }
}
