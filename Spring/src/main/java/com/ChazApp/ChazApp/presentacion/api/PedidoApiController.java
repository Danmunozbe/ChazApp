package com.ChazApp.ChazApp.presentacion.api;

import com.ChazApp.ChazApp.negocio.dto.ResultadoCompra;
import com.ChazApp.ChazApp.negocio.dto.SolicitudPedido;
import com.ChazApp.ChazApp.negocio.servicio.PedidoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * CU-01 por HTTP (navegador o Postman): confirmar un carrito que puede mezclar
 * varias chazas. Devuelve un pedido por chaza, cada uno en la cola de su chaza.
 */
@RestController
@RequestMapping("/api/compras")
public class PedidoApiController {

    private final PedidoService pedidoService;

    public PedidoApiController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @PostMapping
    public ResponseEntity<ResultadoCompra> confirmar(@Valid @RequestBody SolicitudPedido solicitud) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoService.confirmarCarrito(solicitud));
    }
}
