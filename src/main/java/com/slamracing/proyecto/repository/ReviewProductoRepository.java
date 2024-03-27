package com.slamracing.proyecto.repository;

import com.slamracing.proyecto.model.ReviewProducto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewProductoRepository extends JpaRepository<ReviewProducto, Long> {
    List<ReviewProducto> findAllByProductoId(Long idProducto);
}
