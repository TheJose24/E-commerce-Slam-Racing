package com.slamracing.proyecto.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Date;

@Data
//@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name="pagos")
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;

    private String medio_pago;
    private String tipo_pago;

    private BigDecimal montoNeto;
    private BigDecimal comision_mp;
    private BigDecimal monto_recibido;
    private String fecha;
    private String estado;
}
