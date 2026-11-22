package com.ChazApp.ChazApp.exception;

public class MenuNoPublicadoException extends RuntimeException {

    public MenuNoPublicadoException(String message) {
        super(message);
    }

    public MenuNoPublicadoException(Long chazaId) {
        super("No hay un menú disponible para esta chaza");
    }
}
