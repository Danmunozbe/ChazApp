package com.ChazApp.ChazApp.negocio.dto;

import java.util.List;

/** Cola completa de una chaza para el vendedor (CU-02): pendientes y listos. */
public record ColaChaza(
        Long chazaId,
        String chazaNombre,
        List<PedidoCola> pendientes,
        List<PedidoCola> listos) {
}
