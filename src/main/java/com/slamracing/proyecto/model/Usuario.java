package com.slamracing.proyecto.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
//@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name="usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long id;
    private String nombre;
    private String apellido;
    private String username;
    @Email
    private String email;
    private String telefono;
    private String ciudad;
    private String direccion;
    private Boolean isAdmin;
    private String password;
}
