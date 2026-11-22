package com.ChazApp.ChazApp.presentacion.api;

import com.ChazApp.ChazApp.negocio.excepcion.PedidoInvalidoException;
import com.ChazApp.ChazApp.negocio.excepcion.ProductosAgotadosException;
import com.ChazApp.ChazApp.negocio.excepcion.ProductosAgotadosException.ProductoAgotado;
import com.ChazApp.ChazApp.negocio.excepcion.RecursoNoEncontradoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

/** Traduce las excepciones de negocio a respuestas JSON con un mensaje legible. */
@RestControllerAdvice(basePackageClasses = ManejadorErroresApi.class)
public class ManejadorErroresApi {

    public record RespuestaError(String mensaje, List<ProductoAgotado> productosAgotados) {
        static RespuestaError de(String mensaje) {
            return new RespuestaError(mensaje, List.of());
        }
    }

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<RespuestaError> noEncontrado(RecursoNoEncontradoException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(RespuestaError.de(ex.getMessage()));
    }

    /** 409: el pedido tiene productos agotados; se devuelven para avisar al estudiante. */
    @ExceptionHandler(ProductosAgotadosException.class)
    public ResponseEntity<RespuestaError> agotados(ProductosAgotadosException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new RespuestaError(ex.getMessage(), ex.getProductosAgotados()));
    }

    @ExceptionHandler(PedidoInvalidoException.class)
    public ResponseEntity<RespuestaError> invalido(PedidoInvalidoException ex) {
        return ResponseEntity.badRequest().body(RespuestaError.de(ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RespuestaError> validacion(MethodArgumentNotValidException ex) {
        String mensaje = ex.getBindingResult().getAllErrors().stream()
                .map(e -> e.getDefaultMessage())
                .distinct()
                .reduce((a, b) -> a + " " + b)
                .orElse("Datos del pedido inválidos.");
        return ResponseEntity.badRequest().body(RespuestaError.de(mensaje));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<RespuestaError> cuerpoIlegible(HttpMessageNotReadableException ex) {
        return ResponseEntity.badRequest().body(RespuestaError.de("El cuerpo de la solicitud no es un JSON válido."));
    }
}
