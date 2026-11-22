package com.ChazApp.ChazApp.persistencia.repositorio;

import com.ChazApp.ChazApp.persistencia.entidad.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

    List<Producto> findByChaza_IdOrderByIdAsc(Long chazaId);

    long countByChaza_IdAndDisponibleTrue(Long chazaId);
}
