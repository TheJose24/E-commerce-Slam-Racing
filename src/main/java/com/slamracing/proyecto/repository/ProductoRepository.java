package com.slamracing.proyecto.repository;

import com.slamracing.proyecto.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    Optional<Producto> findBySlug(String slug);

    List<Producto> findAllByNombre(String nombre);

}
