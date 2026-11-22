package com.ChazApp.ChazApp.controller;

import com.ChazApp.ChazApp.exception.ChazaNotFoundException;
import com.ChazApp.ChazApp.exception.ProductoNotFoundException;
import com.ChazApp.ChazApp.model.Chaza;
import com.ChazApp.ChazApp.model.Producto;
import com.ChazApp.ChazApp.service.ChazaService;
import com.ChazApp.ChazApp.service.ProductoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/vendedor")
public class VendedorController {

    private final ChazaService chazaService;
    private final ProductoService productoService;

    public VendedorController(ChazaService chazaService, ProductoService productoService) {
        this.chazaService = chazaService;
        this.productoService = productoService;
    }

    /**
     * CU-04: Panel de administración para el vendedor de una chaza específica.
     */
    @GetMapping("/chaza/{chazaId}")
    public String panelVendedor(@PathVariable Long chazaId, Model model, RedirectAttributes redirectAttributes) {
        try {
            Chaza chaza = chazaService.obtenerChazaPorId(chazaId);
            List<Producto> productos = productoService.obtenerProductosPorChaza(chazaId);

            model.addAttribute("chaza", chaza);
            model.addAttribute("productos", productos);
            return "vendedor/dashboard";

        } catch (ChazaNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/";
        }
    }

    /**
     * CU-04: Actualiza la disponibilidad del producto (!disponible) y redirige de vuelta.
     */
    @PostMapping("/producto/{productoId}/toggle")
    public String toggleDisponibilidad(@PathVariable Long productoId, RedirectAttributes redirectAttributes) {
        try {
            Producto productoActualizado = productoService.cambiarDisponibilidad(productoId);
            Long chazaId = productoActualizado.getChaza().getId();

            String estado = productoActualizado.isDisponible() ? "Disponible" : "Agotado";
            redirectAttributes.addFlashAttribute("mensajeExito",
                    "Disponibilidad actualizada: '" + productoActualizado.getNombre() + "' ahora está " + estado + ".");

            return "redirect:/vendedor/chaza/" + chazaId;

        } catch (ProductoNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/";
        }
    }
}
