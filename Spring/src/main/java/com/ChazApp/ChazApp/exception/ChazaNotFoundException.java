package com.ChazApp.ChazApp.exception;

public class ChazaNotFoundException extends RuntimeException {

    public ChazaNotFoundException(String message) {
        super(message);
    }

    public ChazaNotFoundException(Long id) {
        super("Chaza con ID " + id + " no encontrada.");
    }
}
