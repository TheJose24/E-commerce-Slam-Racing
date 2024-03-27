package com.slamracing.proyecto.controller;

import com.slamracing.proyecto.model.ReviewProducto;
import com.slamracing.proyecto.model.Usuario;
import com.slamracing.proyecto.repository.UsuarioRepository;
import com.slamracing.proyecto.service.ProductoService;
import com.slamracing.proyecto.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@RestController
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/crearUsuario")
    public Usuario crearUsuario(@Valid @RequestBody Usuario usuario) {
        return usuarioService.crearUsuario(usuario);
    }

    @GetMapping("/listarUsuarios")
    public List<Usuario> listarUsuarios() {
        return usuarioService.listarUsuarios();
    }

    @GetMapping("/listarUsuario/{id}")
    public Usuario buscarUsuarioPorId(@PathVariable Long id) {
        return usuarioService.buscarUsuarioPorId(id);
    }

    @PutMapping("/actualizarUsuario")
    public void actualizarUsuario(@RequestBody Usuario usuario) {
        usuarioService.actualizarUsuario(usuario);
    }

    @DeleteMapping("/eliminarUsuario/{id}")
    public void eliminarCliente(@PathVariable Long id) {
        usuarioService.eliminarUsuario(id);
    }

    @PostMapping("/login")
    public Usuario login(@RequestBody Usuario usuario) {
        return usuarioService.login(usuario.getUsername(), usuario.getPassword());
    }

    @Autowired
    private ProductoService productoService;

    @PostMapping("/agregarReview")
    public ReviewProducto agregarReview(@RequestBody ReviewProducto reviewProducto) {
        // Formatear la fecha en el formato deseado "día/mes/año"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String fechaFormateada = LocalDate.now().format(formatter);

        // Establecer la fecha formateada en el objeto ReviewProducto
        reviewProducto.setFecha(fechaFormateada);
        reviewProducto.setPuntuacion(reviewProducto.getPuntuacion() != null ? reviewProducto.getPuntuacion() : 0.0);
        productoService.agregarReviewProducto(reviewProducto);
        return reviewProducto;
    }
}
