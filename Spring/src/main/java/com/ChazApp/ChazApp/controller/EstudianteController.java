package com.ChazApp.ChazApp.controller;

import com.ChazApp.ChazApp.exception.ChazaNotFoundException;
import com.ChazApp.ChazApp.exception.MenuNoPublicadoException;
import com.ChazApp.ChazApp.model.Chaza;
import com.ChazApp.ChazApp.model.Producto;
import com.ChazApp.ChazApp.service.ChazaService;
import com.ChazApp.ChazApp.service.ProductoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/estudiante")
public class EstudianteController {

    private final ChazaService chazaService;
    private final ProductoService productoService;

    public EstudianteController(ChazaService chazaService, ProductoService productoService) {
        this.chazaService = chazaService;
        this.productoService = productoService;
    }

    /**
     * CU-05: Lista de chazas disponibles para los estudiantes.
     */
    @GetMapping("/chazas")
    public String listarChazas(Model model) {
        List<Chaza> chazas = chazaService.obtenerChazasActivas();
        model.addAttribute("chazas", chazas);
        return "estudiante/chazas";
    }

    /**
     * CU-05: Consulta el menú y la disponibilidad de productos de una chaza.
     */
    @GetMapping("/chazas/{id}")
    public String verMenuChaza(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Chaza chaza = chazaService.obtenerChazaPorId(id);
            model.addAttribute("chaza", chaza);

            try {
                List<Producto> productos = productoService.obtenerProductosParaEstudiante(id);
                model.addAttribute("productos", productos);
                model.addAttribute("menuPublicado", true);
            } catch (MenuNoPublicadoException e) {
                model.addAttribute("productos", Collections.emptyList());
                model.addAttribute("menuPublicado", false);
                model.addAttribute("mensaje", e.getMessage());
            }

            return "estudiante/menu";

        } catch (ChazaNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/estudiante/chazas";
        }
    }
}
