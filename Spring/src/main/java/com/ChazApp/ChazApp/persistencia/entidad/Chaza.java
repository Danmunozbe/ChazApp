package com.ChazApp.ChazApp.persistencia.entidad;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Puesto de comida dentro del campus.
 */
@Entity
@Table(name = "chazas")
public class Chaza {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(length = 500)
    private String descripcion;

    @Column(length = 200)
    private String ubicacion;

    @Column(name = "imagen_url", length = 200)
    private String imagenUrl;

    /** true = abierta, false = cerrada. */
    @Column(nullable = false)
    private boolean activa = true;

    protected Chaza() {
        // Requerido por JPA
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public boolean isActiva() {
        return activa;
    }
}
