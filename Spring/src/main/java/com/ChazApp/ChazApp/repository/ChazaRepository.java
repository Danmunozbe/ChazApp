package com.ChazApp.ChazApp.repository;

import com.ChazApp.ChazApp.model.Chaza;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChazaRepository extends JpaRepository<Chaza, Long> {

    List<Chaza> findByActivaTrue();
}
