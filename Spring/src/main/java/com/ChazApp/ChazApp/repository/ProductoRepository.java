package com.ChazApp.ChazApp.repository;

import com.ChazApp.ChazApp.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    List<Producto> findByChazaId(Long chazaId);

    List<Producto> findByChazaIdAndDisponibleTrue(Long chazaId);
}
