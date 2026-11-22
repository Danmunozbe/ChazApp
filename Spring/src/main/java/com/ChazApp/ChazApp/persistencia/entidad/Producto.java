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
 * Ítem del menú de una chaza. La disponibilidad es binaria (disponible / agotado),
 * como se acordó en la planeación del sprint.
 */
@Entity
@Table(name = "productos")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "chaza_id", nullable = false)
    private Chaza chaza;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(length = 500)
    private String descripcion;

    /** Precio en pesos colombianos (sin decimales). */
    @Column(nullable = false)
    private Integer precio;

    @Column(name = "imagen_url", length = 200)
    private String imagenUrl;

    /** true = disponible, false = agotado. */
    @Column(nullable = false)
    private boolean disponible = true;

    protected Producto() {
        // Requerido por JPA
    }

    /** CU-04: el vendedor marca el producto como disponible o agotado. */
    public void cambiarDisponibilidad(boolean disponible) {
        this.disponible = disponible;
    }

    public Long getId() {
        return id;
    }

    public Chaza getChaza() {
        return chaza;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Integer getPrecio() {
        return precio;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public boolean isDisponible() {
        return disponible;
    }
}
