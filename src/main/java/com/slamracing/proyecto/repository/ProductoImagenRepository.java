package com.slamracing.proyecto.repository;

import com.slamracing.proyecto.model.Producto;
import com.slamracing.proyecto.model.ProductoImagen;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductoImagenRepository extends JpaRepository<ProductoImagen, Long> {


}
