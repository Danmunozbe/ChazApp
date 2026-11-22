package com.ChazApp.ChazApp.presentacion.api;

import com.ChazApp.ChazApp.negocio.dto.ChazaResumen;
import com.ChazApp.ChazApp.negocio.dto.ColaChaza;
import com.ChazApp.ChazApp.negocio.dto.DisponibilidadProducto;
import com.ChazApp.ChazApp.negocio.dto.MenuChaza;
import com.ChazApp.ChazApp.negocio.dto.PedidoCola;
import com.ChazApp.ChazApp.negocio.servicio.ColaService;
import com.ChazApp.ChazApp.negocio.servicio.DisponibilidadService;
import com.ChazApp.ChazApp.negocio.servicio.MenuService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * API de chazas por HTTP (navegador o Postman).
 * Comprador: listar, menú y disponibilidad (CU-05).
 * Vendedor: cola de pedidos (CU-02) y disponibilidad de productos (CU-04).
 */
@RestController
@RequestMapping("/api/chazas")
public class ChazaApiController {

    private final MenuService menuService;
    private final ColaService colaService;
    private final DisponibilidadService disponibilidadService;

    public ChazaApiController(MenuService menuService,
                              ColaService colaService,
                              DisponibilidadService disponibilidadService) {
        this.menuService = menuService;
        this.colaService = colaService;
        this.disponibilidadService = disponibilidadService;
    }

    // ---- CU-05: comprador ----------------------------------------------------

    @GetMapping
    public List<ChazaResumen> listar() {
        return menuService.listarChazas();
    }

    @GetMapping("/{id}/menu")
    public MenuChaza menu(@PathVariable Long id) {
        return menuService.obtenerMenu(id);
    }

    /** Refresco periódico de disponibilidad (CU-05-M01), también usado por el vendedor. */
    @GetMapping("/{id}/disponibilidad")
    public List<DisponibilidadProducto> disponibilidad(@PathVariable Long id) {
        return menuService.consultarDisponibilidad(id);
    }

    // ---- CU-04: el vendedor cambia la disponibilidad de un producto ----------

    @PutMapping("/{id}/productos/{productoId}/disponibilidad")
    public DisponibilidadProducto cambiarDisponibilidad(
            @PathVariable Long id,
            @PathVariable Long productoId,
            @Valid @RequestBody CambioDisponibilidadRequest cuerpo) {
        return disponibilidadService.cambiar(id, productoId, cuerpo.disponible());
    }

    // ---- CU-02: cola de pedidos del vendedor ---------------------------------

    @GetMapping("/{id}/cola")
    public ColaChaza cola(@PathVariable Long id) {
        return colaService.consultarCola(id);
    }

    /** CU-02-HU-02: avanza el pedido al siguiente estado válido. */
    @PostMapping("/{id}/pedidos/{pedidoId}/avanzar")
    public PedidoCola avanzar(@PathVariable Long id, @PathVariable Long pedidoId) {
        return colaService.avanzarEstado(id, pedidoId);
    }

    /** CU-02-HU-03: marca el pedido como listo para recoger. */
    @PostMapping("/{id}/pedidos/{pedidoId}/listo")
    public PedidoCola listo(@PathVariable Long id, @PathVariable Long pedidoId) {
        return colaService.marcarListo(id, pedidoId);
    }
}
