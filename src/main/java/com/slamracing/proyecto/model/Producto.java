package com.slamracing.proyecto.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Data
//@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="productos")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    private Long id;
    private String nombre;
    @Column(length = 300)
    private String descripcion;


    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL)
    private List<ProductoImagen> imagenes;

    private BigDecimal precio_unitario;
    // El stock no puede ser un numero negativo
    @PositiveOrZero
    private Integer stock;
    private Integer descuento;
    private String color;
    private String material;
    // Ruta amigable para el producto
    private String slug;

    @JsonIgnore
    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL)
    private List<ReviewProducto> puntuaciones;

    private int numOpiniones;

    private double puntuacionPromedio;

}
