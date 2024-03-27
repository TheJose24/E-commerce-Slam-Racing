package com.slamracing.proyecto.repository;

import com.slamracing.proyecto.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
}
