package com.ChazApp.ChazApp.persistencia.repositorio;

import com.ChazApp.ChazApp.persistencia.entidad.Chaza;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChazaRepository extends JpaRepository<Chaza, Long> {

    List<Chaza> findByActivaTrueOrderByIdAsc();
}
