package com.ChazApp.ChazApp.persistencia.repositorio;

import com.ChazApp.ChazApp.persistencia.entidad.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    List<Categoria> findByChaza_IdOrderByOrdenAsc(Long chazaId);
}
