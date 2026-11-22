package com.ChazApp.ChazApp.persistencia.entidad;

/**
 * Estados de un pedido y sus transiciones válidas (diagrama simple del sprint):
 * RECIBIDO -> EN_PREPARACION -> LISTO.
 * CU-01 crea el pedido en RECIBIDO; CU-02 lo avanza sin saltos.
 */
public enum EstadoPedido {
    RECIBIDO("Recibido"),
    EN_PREPARACION("En preparación"),
    LISTO("Listo para recoger");

    private final String etiqueta;

    EstadoPedido(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    /** true si el pedido todavía está en la cola de pendientes (no se ha recogido). */
    public boolean esPendiente() {
        return this != LISTO;
    }

    /** Siguiente estado en el flujo, o null si ya es el final (LISTO). */
    public EstadoPedido siguiente() {
        return switch (this) {
            case RECIBIDO -> EN_PREPARACION;
            case EN_PREPARACION -> LISTO;
            case LISTO -> null;
        };
    }

    /** true si se puede pasar de este estado al destino sin saltos inválidos. */
    public boolean puedeAvanzarA(EstadoPedido destino) {
        return destino == siguiente();
    }
}
