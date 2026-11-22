package com.ChazApp.ChazApp.controller;

import com.ChazApp.ChazApp.model.Chaza;
import com.ChazApp.ChazApp.service.ChazaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    private final ChazaService chazaService;

    public HomeController(ChazaService chazaService) {
        this.chazaService = chazaService;
    }

    /**
     * Pantalla de inicio con selector rápido de rol (Estudiante o Vendedor).
     */
    @GetMapping("/")
    public String index(Model model) {
        List<Chaza> chazas = chazaService.obtenerChazasActivas();
        model.addAttribute("chazas", chazas);
        return "index";
    }
}
