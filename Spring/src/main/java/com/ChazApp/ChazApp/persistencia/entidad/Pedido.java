package com.ChazApp.ChazApp.persistencia.entidad;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Pedido anticipado de un estudiante. Al crearse queda en la cola de su chaza
 * con estado RECIBIDO; la cola se ordena por fechaHora (llegada).
 */
@Entity
@Table(name = "pedidos")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "chaza_id", nullable = false)
    private Chaza chaza;

    @Column(name = "nombre_estudiante", nullable = false, length = 80)
    private String nombreEstudiante;

    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoPedido estado;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id ASC")
    private List<ItemPedido> items = new ArrayList<>();

    protected Pedido() {
        // Requerido por JPA
    }

    public Pedido(Chaza chaza, String nombreEstudiante, LocalDateTime fechaHora) {
        this.chaza = chaza;
        this.nombreEstudiante = nombreEstudiante;
        this.fechaHora = fechaHora;
        this.estado = EstadoPedido.RECIBIDO;
    }

    /** Agrega un producto guardando el precio vigente al momento del pedido. */
    public void agregarItem(Producto producto, int cantidad) {
        items.add(new ItemPedido(this, producto, cantidad, producto.getPrecio()));
    }

    /** CU-02: cambia el estado al destino si la transición es válida. */
    public void cambiarEstado(EstadoPedido destino) {
        if (!estado.puedeAvanzarA(destino)) {
            throw new IllegalStateException("No se puede pasar de " + estado.getEtiqueta()
                    + " a " + destino.getEtiqueta() + ".");
        }
        this.estado = destino;
    }

    public int getTotal() {
        return items.stream().mapToInt(ItemPedido::getSubtotal).sum();
    }

    public Long getId() {
        return id;
    }

    public Chaza getChaza() {
        return chaza;
    }

    public String getNombreEstudiante() {
        return nombreEstudiante;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public List<ItemPedido> getItems() {
        return Collections.unmodifiableList(items);
    }
}
