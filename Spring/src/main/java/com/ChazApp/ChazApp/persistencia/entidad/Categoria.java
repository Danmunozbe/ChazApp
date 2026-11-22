package com.ChazApp.ChazApp.persistencia.entidad;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Categoría de productos dentro del menú de una chaza (Burritos, Bebidas, ...).
 */
@Entity
@Table(name = "categorias")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "chaza_id", nullable = false)
    private Chaza chaza;

    @Column(nullable = false, length = 100)
    private String nombre;

    /** Posición de la categoría al mostrar el menú. */
    @Column(nullable = false)
    private int orden;

    protected Categoria() {
        // Requerido por JPA
    }

    public Long getId() {
        return id;
    }

    public Chaza getChaza() {
        return chaza;
    }

    public String getNombre() {
        return nombre;
    }

    public int getOrden() {
        return orden;
    }
}
