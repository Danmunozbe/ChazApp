package com.ChazApp.ChazApp.presentacion.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Única página de la aplicación. Toda la interacción (menús, carrito, cola del
 * vendedor, disponibilidad) ocurre en esta pantalla usando la API REST; no hay
 * rutas sueltas por chaza ni por pedido.
 */
@Controller
public class PaginaController {

    private final long intervaloRefrescoMs;

    public PaginaController(
            @Value("${chazapp.menu.intervalo-refresco-ms:5000}") long intervaloRefrescoMs) {
        this.intervaloRefrescoMs = intervaloRefrescoMs;
    }

    @GetMapping("/")
    public String inicio(Model model) {
        model.addAttribute("intervaloRefresco", intervaloRefrescoMs);
        return "app";
    }
}
