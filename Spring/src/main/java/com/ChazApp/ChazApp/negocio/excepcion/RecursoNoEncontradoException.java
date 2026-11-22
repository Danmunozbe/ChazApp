package com.ChazApp.ChazApp.negocio.excepcion;

/** La chaza o el pedido solicitado no existe (o la chaza está cerrada). */
public class RecursoNoEncontradoException extends RuntimeException {

    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
