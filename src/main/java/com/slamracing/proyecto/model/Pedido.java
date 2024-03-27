package com.slamracing.proyecto.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
//@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name="pedidos")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pedido")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
    private List<DetallePedido> detalles;

    private String fechaCreacion;

    private BigDecimal subTotal;

    private BigDecimal envio;

    private BigDecimal precioTotal;

}
