package com.ChazApp.ChazApp.negocio.servicio;

import com.ChazApp.ChazApp.negocio.dto.ColaChaza;
import com.ChazApp.ChazApp.negocio.dto.PedidoCola;
import com.ChazApp.ChazApp.negocio.dto.PedidoCola.ItemCola;
import com.ChazApp.ChazApp.negocio.excepcion.PedidoInvalidoException;
import com.ChazApp.ChazApp.negocio.excepcion.RecursoNoEncontradoException;
import com.ChazApp.ChazApp.persistencia.entidad.Chaza;
import com.ChazApp.ChazApp.persistencia.entidad.EstadoPedido;
import com.ChazApp.ChazApp.persistencia.entidad.Pedido;
import com.ChazApp.ChazApp.persistencia.repositorio.ChazaRepository;
import com.ChazApp.ChazApp.persistencia.repositorio.PedidoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * CU-02: Gestionar la cola de pedidos de una chaza (vendedor).
 * HU-01 consultar la cola, HU-02 avanzar el estado, HU-03 marcar listo.
 */
@Service
public class ColaService {

    private static final List<EstadoPedido> PENDIENTES =
            List.of(EstadoPedido.RECIBIDO, EstadoPedido.EN_PREPARACION);

    private final ChazaRepository chazaRepository;
    private final PedidoRepository pedidoRepository;

    public ColaService(ChazaRepository chazaRepository, PedidoRepository pedidoRepository) {
        this.chazaRepository = chazaRepository;
        this.pedidoRepository = pedidoRepository;
    }

    /** CU-02-HU-01: pendientes por hora de llegada + los ya listos. */
    @Transactional(readOnly = true)
    public ColaChaza consultarCola(Long chazaId) {
        Chaza chaza = buscarChaza(chazaId);
        List<PedidoCola> pendientes = pedidoRepository
                .findByChaza_IdAndEstadoInOrderByFechaHoraAsc(chazaId, PENDIENTES)
                .stream().map(this::aPedidoCola).toList();
        List<PedidoCola> listos = pedidoRepository
                .findByChaza_IdAndEstadoOrderByFechaHoraDesc(chazaId, EstadoPedido.LISTO)
                .stream().map(this::aPedidoCola).toList();
        return new ColaChaza(chaza.getId(), chaza.getNombre(), pendientes, listos);
    }

    /** CU-02-HU-02: avanza el pedido al siguiente estado válido (sin saltos). */
    @Transactional
    public PedidoCola avanzarEstado(Long chazaId, Long pedidoId) {
        Pedido pedido = buscarPedidoDeChaza(chazaId, pedidoId);
        EstadoPedido siguiente = pedido.getEstado().siguiente();
        if (siguiente == null) {
            throw new PedidoInvalidoException("El pedido ya está listo para recoger.");
        }
        return aplicarEstado(pedido, siguiente);
    }

    /** CU-02-HU-03: marca como listo un pedido que está en preparación. */
    @Transactional
    public PedidoCola marcarListo(Long chazaId, Long pedidoId) {
        Pedido pedido = buscarPedidoDeChaza(chazaId, pedidoId);
        if (pedido.getEstado() != EstadoPedido.EN_PREPARACION) {
            throw new PedidoInvalidoException(
                    "Solo se puede marcar como listo un pedido en preparación.");
        }
        return aplicarEstado(pedido, EstadoPedido.LISTO);
    }

    private PedidoCola aplicarEstado(Pedido pedido, EstadoPedido destino) {
        try {
            pedido.cambiarEstado(destino);
        } catch (IllegalStateException ex) {
            throw new PedidoInvalidoException(ex.getMessage());
        }
        return aPedidoCola(pedidoRepository.save(pedido));
    }

    private Chaza buscarChaza(Long chazaId) {
        return chazaRepository.findById(chazaId)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "La chaza " + chazaId + " no existe."));
    }

    private Pedido buscarPedidoDeChaza(Long chazaId, Long pedidoId) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "El pedido " + pedidoId + " no existe."));
        // El vendedor solo gestiona pedidos de su propia chaza.
        if (!pedido.getChaza().getId().equals(chazaId)) {
            throw new RecursoNoEncontradoException(
                    "El pedido " + pedidoId + " no pertenece a esta chaza.");
        }
        return pedido;
    }

    private PedidoCola aPedidoCola(Pedido pedido) {
        EstadoPedido estado = pedido.getEstado();
        EstadoPedido siguiente = estado.siguiente();
        List<ItemCola> items = pedido.getItems().stream()
                .map(i -> new ItemCola(i.getProducto().getNombre(), i.getCantidad()))
                .toList();
        return new PedidoCola(
                pedido.getId(),
                pedido.getNombreEstudiante(),
                pedido.getFechaHora(),
                estado.getEtiqueta(),
                estado.name(),
                siguiente == null ? null : siguiente.getEtiqueta(),
                siguiente == null ? null : siguiente.name(),
                siguiente != null,
                estado == EstadoPedido.EN_PREPARACION,
                items,
                pedido.getTotal());
    }
}
