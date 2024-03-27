package com.slamracing.proyecto.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Data
//@Builder
@ToString(exclude = "producto")
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="opiniones")
public class ReviewProducto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_producto", referencedColumnName = "id_producto")
    private Producto producto;

    private String nombres;

    // numero del 0 al 5
    private Double puntuacion;

    private String titulo;

    private String opinion;

    private String fecha;

}
