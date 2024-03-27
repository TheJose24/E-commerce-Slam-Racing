package com.slamracing.proyecto.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Data
//@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "pedido")
@Entity
@Table(name="detalle_pedidos")
public class DetallePedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle_pedido")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id")
    private Pedido pedido;


    @ManyToOne
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    private int cantidad;

    private BigDecimal total;
}
