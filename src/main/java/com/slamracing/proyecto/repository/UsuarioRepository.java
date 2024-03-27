package com.slamracing.proyecto.repository;

import com.slamracing.proyecto.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    public Usuario findByUsernameAndPassword(String username, String password);
}
