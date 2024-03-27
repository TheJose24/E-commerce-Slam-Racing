package com.slamracing.proyecto.repository;

import com.slamracing.proyecto.model.Pago;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VentasRepository extends JpaRepository<Pago, Long> {
}
