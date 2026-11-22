package com.ChazApp.ChazApp.service;

import com.ChazApp.ChazApp.exception.ChazaNotFoundException;
import com.ChazApp.ChazApp.exception.MenuNoPublicadoException;
import com.ChazApp.ChazApp.model.Chaza;
import com.ChazApp.ChazApp.repository.ChazaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ChazaService {

    private final ChazaRepository chazaRepository;

    public ChazaService(ChazaRepository chazaRepository) {
        this.chazaRepository = chazaRepository;
    }

    /**
     * Obtiene la lista de todas las chazas activas en la universidad.
     */
    public List<Chaza> obtenerChazasActivas() {
        return chazaRepository.findByActivaTrue();
    }

    /**
     * Obtiene una chaza por su ID.
     * @throws ChazaNotFoundException si la chaza no existe
     */
    public Chaza obtenerChazaPorId(Long id) {
        return chazaRepository.findById(id)
                .orElseThrow(() -> new ChazaNotFoundException(id));
    }

    /**
     * Obtiene una chaza validando las reglas para la vista del estudiante (CU-05).
     * @throws ChazaNotFoundException si la chaza no existe
     * @throws MenuNoPublicadoException si la chaza tiene menu_publicado = false
     */
    public Chaza obtenerChazaParaEstudiante(Long id) {
        Chaza chaza = obtenerChazaPorId(id);
        if (!chaza.isMenuPublicado()) {
            throw new MenuNoPublicadoException("No hay un menú disponible para esta chaza");
        }
        return chaza;
    }

    /**
     * Verifica si una chaza tiene su menú publicado.
     */
    public boolean tieneMenuPublicado(Long id) {
        Chaza chaza = obtenerChazaPorId(id);
        return chaza.isMenuPublicado();
    }
}
