package com.ChazApp.ChazApp.negocio.servicio;

import com.ChazApp.ChazApp.negocio.dto.PedidoConfirmado;
import com.ChazApp.ChazApp.negocio.dto.PedidoConfirmado.ItemConfirmado;
import com.ChazApp.ChazApp.negocio.dto.ResultadoCompra;
import com.ChazApp.ChazApp.negocio.dto.SolicitudPedido;
import com.ChazApp.ChazApp.negocio.excepcion.PedidoInvalidoException;
import com.ChazApp.ChazApp.negocio.excepcion.ProductosAgotadosException;
import com.ChazApp.ChazApp.negocio.excepcion.ProductosAgotadosException.ProductoAgotado;
import com.ChazApp.ChazApp.negocio.excepcion.RecursoNoEncontradoException;
import com.ChazApp.ChazApp.persistencia.entidad.Chaza;
import com.ChazApp.ChazApp.persistencia.entidad.Pedido;
import com.ChazApp.ChazApp.persistencia.entidad.Producto;
import com.ChazApp.ChazApp.persistencia.repositorio.PedidoRepository;
import com.ChazApp.ChazApp.persistencia.repositorio.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * CU-01: Realizar pedido anticipado.
 * El carrito puede mezclar productos de varias chazas. Al confirmar se crea un
 * pedido por cada chaza, cada uno entra a la cola de su chaza en estado RECIBIDO.
 * Reglas: ningún producto puede estar agotado al confirmar (CU-01-M01).
 */
@Service
public class PedidoService {

    static final int CANTIDAD_MAXIMA_POR_PRODUCTO = 20;

    private final ProductoRepository productoRepository;
    private final PedidoRepository pedidoRepository;

    public PedidoService(ProductoRepository productoRepository,
                         PedidoRepository pedidoRepository) {
        this.productoRepository = productoRepository;
        this.pedidoRepository = pedidoRepository;
    }

    @Transactional
    public ResultadoCompra confirmarCarrito(SolicitudPedido solicitud) {
        String nombre = solicitud.nombreEstudiante().trim();

        // Sumar cantidades por producto (mismo producto repetido se acumula).
        Map<Long, Integer> cantidades = new LinkedHashMap<>();
        for (SolicitudPedido.ItemSolicitud item : solicitud.items()) {
            cantidades.merge(item.productoId(), item.cantidad(), Integer::sum);
        }

        Map<Long, Producto> productos = productoRepository.findAllById(cantidades.keySet()).stream()
                .collect(Collectors.toMap(Producto::getId, Function.identity()));

        List<ProductoAgotado> agotados = new ArrayList<>();
        for (Map.Entry<Long, Integer> entrada : cantidades.entrySet()) {
            Producto producto = productos.get(entrada.getKey());
            if (producto == null) {
                throw new PedidoInvalidoException("El producto " + entrada.getKey() + " ya no existe.");
            }
            if (!producto.getChaza().isActiva()) {
                throw new PedidoInvalidoException("La chaza de " + producto.getNombre() + " está cerrada.");
            }
            if (entrada.getValue() > CANTIDAD_MAXIMA_POR_PRODUCTO) {
                throw new PedidoInvalidoException("La cantidad máxima por producto es "
                        + CANTIDAD_MAXIMA_POR_PRODUCTO + " (" + producto.getNombre() + ").");
            }
            if (!producto.isDisponible()) {
                agotados.add(new ProductoAgotado(producto.getId(), producto.getNombre()));
            }
        }
        if (!agotados.isEmpty()) {
            throw new ProductosAgotadosException(agotados);
        }

        // Agrupar los ítems por chaza, conservando el orden en que aparecieron.
        Map<Chaza, Map<Producto, Integer>> porChaza = new LinkedHashMap<>();
        cantidades.forEach((productoId, cantidad) -> {
            Producto producto = productos.get(productoId);
            porChaza.computeIfAbsent(producto.getChaza(), c -> new LinkedHashMap<>())
                    .put(producto, cantidad);
        });

        List<PedidoConfirmado> confirmados = new ArrayList<>();
        int totalGeneral = 0;
        for (Map.Entry<Chaza, Map<Producto, Integer>> entrada : porChaza.entrySet()) {
            Pedido pedido = new Pedido(entrada.getKey(), nombre, LocalDateTime.now());
            entrada.getValue().forEach(pedido::agregarItem);
            PedidoConfirmado confirmado = aConfirmado(pedidoRepository.save(pedido));
            confirmados.add(confirmado);
            totalGeneral += confirmado.total();
        }

        return new ResultadoCompra(confirmados, totalGeneral);
    }

    @Transactional(readOnly = true)
    public PedidoConfirmado obtenerPedido(Long pedidoId) {
        return pedidoRepository.findById(pedidoId)
                .map(this::aConfirmado)
                .orElseThrow(() -> new RecursoNoEncontradoException("El pedido " + pedidoId + " no existe."));
    }

    private PedidoConfirmado aConfirmado(Pedido pedido) {
        List<ItemConfirmado> items = pedido.getItems().stream()
                .map(i -> new ItemConfirmado(i.getProducto().getNombre(), i.getCantidad(),
                        i.getPrecioUnitario(), i.getSubtotal()))
                .toList();
        return new PedidoConfirmado(pedido.getId(), pedido.getChaza().getId(), pedido.getChaza().getNombre(),
                pedido.getNombreEstudiante(), pedido.getFechaHora(), pedido.getEstado().getEtiqueta(),
                items, pedido.getTotal());
    }
}
