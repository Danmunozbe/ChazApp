package com.ChazApp.ChazApp.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "productos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chaza_id", nullable = false)
    private Chaza chaza;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(length = 500)
    private String descripcion;

    @Column(length = 100)
    private String categoria;

    @Column(nullable = false)
    @Builder.Default
    private Double precio = 0.0;

    @Column(name = "imagen_url", length = 255)
    private String imagenUrl;

    @Column(nullable = false)
    @Builder.Default
    private boolean disponible = true;
}
