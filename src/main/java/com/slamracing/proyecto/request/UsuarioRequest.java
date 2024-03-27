package com.slamracing.proyecto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioRequest {
    private String nombre;
    private String apellido;
    private String username;
    private String email;
    private String telefono;
    private String direccion;
    private Boolean isAdmin;
    private String password;
}
