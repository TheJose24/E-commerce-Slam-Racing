package com.slamracing.proyecto.payment.repository;

import com.slamracing.proyecto.model.Pago;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PagoRepository extends JpaRepository<Pago, Long> {
}
