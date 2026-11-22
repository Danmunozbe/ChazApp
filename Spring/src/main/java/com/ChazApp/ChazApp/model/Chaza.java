package com.ChazApp.ChazApp.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "chazas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Chaza {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(length = 500)
    private String descripcion;

    @Column(length = 200)
    private String ubicacion;

    @Column(name = "imagen_url", length = 255)
    private String imagenUrl;

    @Column(name = "menu_publicado", nullable = false)
    @Builder.Default
    private boolean menuPublicado = true;

    @Column(nullable = false)
    @Builder.Default
    private boolean activa = true;

    @OneToMany(mappedBy = "chaza", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Producto> productos = new ArrayList<>();
}
