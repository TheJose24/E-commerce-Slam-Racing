package com.slamracing.proyecto.service;

import com.slamracing.proyecto.model.Usuario;
import com.slamracing.proyecto.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    UsuarioRepository UsuarioRepository;

    public Usuario crearUsuario(Usuario usuario) {
        return UsuarioRepository.save(usuario);
    }

    public List<Usuario> listarUsuarios() {
        return UsuarioRepository.findAll();
    }

    public Usuario buscarUsuarioPorId(Long id) {
        return UsuarioRepository.findById(id).orElseThrow (() -> new RuntimeException("Usuario no encontrado"));
    }

    public void actualizarUsuario(Usuario usuario) {
        UsuarioRepository.save(usuario);
    }

    public void eliminarUsuario(Long id) {
        UsuarioRepository.deleteById(id);
    }

    public Usuario login(String username, String password) {
        Usuario usuario = UsuarioRepository.findByUsernameAndPassword(username, password);
        if (usuario == null) {
            throw new EmptyResultDataAccessException("username o password incorrectos", 1);
        }
        return usuario;
    }
}
