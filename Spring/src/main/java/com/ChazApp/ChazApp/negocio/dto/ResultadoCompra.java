package com.ChazApp.ChazApp.negocio.dto;

import java.util.List;

/**
 * Resultado de confirmar un carrito (CU-01, camino a): un pedido por cada chaza
 * involucrada. Cada pedido entra a la cola de su chaza.
 */
public record ResultadoCompra(List<PedidoConfirmado> pedidos, int totalGeneral) {
}
