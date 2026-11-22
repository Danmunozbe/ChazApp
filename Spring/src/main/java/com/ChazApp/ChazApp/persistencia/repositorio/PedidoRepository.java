package com.ChazApp.ChazApp.persistencia.repositorio;

import com.ChazApp.ChazApp.persistencia.entidad.EstadoPedido;
import com.ChazApp.ChazApp.persistencia.entidad.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    /** Cola de la chaza: pedidos pendientes ordenados por hora de llegada (CU-02-HU-01). */
    List<Pedido> findByChaza_IdAndEstadoInOrderByFechaHoraAsc(Long chazaId, List<EstadoPedido> estados);

    /** Pedidos ya listos para recoger, los más recientes primero. */
    List<Pedido> findByChaza_IdAndEstadoOrderByFechaHoraDesc(Long chazaId, EstadoPedido estado);
}
