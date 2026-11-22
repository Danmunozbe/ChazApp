package com.ChazApp.ChazApp.persistencia;

import com.ChazApp.ChazApp.persistencia.repositorio.ChazaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

/**
 * Carga los datos de prueba (los mismos del prototipo de escritorio) solo cuando
 * la base está vacía, para no duplicar datos ni borrar cambios entre reinicios.
 */
@Component
public class CargadorDatosIniciales implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(CargadorDatosIniciales.class);

    private final ChazaRepository chazaRepository;
    private final DataSource dataSource;
    private final Resource script;

    public CargadorDatosIniciales(ChazaRepository chazaRepository,
                                  DataSource dataSource,
                                  @Value("classpath:db/datos-iniciales.sql") Resource script) {
        this.chazaRepository = chazaRepository;
        this.dataSource = dataSource;
        this.script = script;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (chazaRepository.count() > 0) {
            log.info("La base de datos ya tiene chazas; no se cargan datos iniciales.");
            return;
        }
        new ResourceDatabasePopulator(false, false, "UTF-8", script).execute(dataSource);
        log.info("Datos iniciales cargados: {} chazas.", chazaRepository.count());
    }
}
