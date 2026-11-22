package com.ChazApp.ChazApp.exception;

public class ProductoNotFoundException extends RuntimeException {

    public ProductoNotFoundException(String message) {
        super(message);
    }

    public ProductoNotFoundException(Long id) {
        super("Producto con ID " + id + " no encontrado.");
    }
}
