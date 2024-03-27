package com.slamracing.proyecto.controller;

import com.slamracing.proyecto.service.ProductoService;
import com.slamracing.proyecto.service.VentasService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private VentasService ventasService;

    public String obtenerFechaHoraActual() {
        // Obtener la fecha y hora actual
        LocalDateTime fechaHoraActual = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy | hh:mm a 'GMT-5'");
        return fechaHoraActual.format(formatter);
    }

    @GetMapping("")
    public String panelAdmin(Model model) {
        // Agregar la fecha y hora formateada al modelo
        model.addAttribute("fechaHoraActual", obtenerFechaHoraActual());

        model.addAttribute("paginaActual", "home");

        return "admin/homeAdmin";
    }

    @GetMapping("/productos")
    public String productosAdmin(Model model) {
        // Agregar la fecha y hora formateada al modelo
        model.addAttribute("fechaHoraActual", obtenerFechaHoraActual());

        // Agregar el nombre de la página actual al modelo
        model.addAttribute("paginaActual", "productos");

        // Obtener la lista de productos y agregarla al modelo como lo estabas haciendo
        model.addAttribute("productos", productoService.listarProductos());

        return "admin/productosAdmin";
    }

    @GetMapping("/ventas")
    public String ventasAdmin(Model model) {
        // Agregar la fecha y hora formateada al modelo
        model.addAttribute("fechaHoraActual", obtenerFechaHoraActual());

        // Agregar el nombre de la página actual al modelo
        model.addAttribute("paginaActual", "ventas");

        model.addAttribute("ventas", ventasService.listarVentas());

        return "admin/ventasAdmin";
    }

    @GetMapping("/pagos")
    public String pagosAdmin(Model model) {
        // Agregar la fecha y hora formateada al modelo
        model.addAttribute("fechaHoraActual", obtenerFechaHoraActual());

        // Agregar el nombre de la página actual al modelo
        model.addAttribute("paginaActual", "pagos");

        return "admin/pagosAdmin";
    }

    @GetMapping("/pedidos")
    public String pedidosAdmin(Model model) {
        // Agregar la fecha y hora formateada al modelo
        model.addAttribute("fechaHoraActual", obtenerFechaHoraActual());

        // Agregar el nombre de la página actual al modelo
        model.addAttribute("paginaActual", "pedidos");

        return "admin/pedidosAdmin";
    }

    @GetMapping("/clientes")
    public String clientesAdmin(Model model) {
        // Agregar la fecha y hora formateada al modelo
        model.addAttribute("fechaHoraActual", obtenerFechaHoraActual());

        // Agregar el nombre de la página actual al modelo
        model.addAttribute("paginaActual", "clientes");

        return "admin/clientesAdmin";
    }

}
