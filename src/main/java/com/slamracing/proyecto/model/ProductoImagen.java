package com.slamracing.proyecto.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString(exclude = "producto")
//@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "producto_imagenes")
public class ProductoImagen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", referencedColumnName = "id_producto")
    private Producto producto;

    private String url;
}

