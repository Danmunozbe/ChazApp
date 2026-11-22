package com.ChazApp.ChazApp.negocio.excepcion;

/** El pedido no cumple una regla de negocio (producto de otra chaza, cantidad inválida...). */
public class PedidoInvalidoException extends RuntimeException {

    public PedidoInvalidoException(String mensaje) {
        super(mensaje);
    }
}
