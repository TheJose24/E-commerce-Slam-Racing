package com.slamracing.proyecto.controller;

import com.slamracing.proyecto.model.*;
import com.slamracing.proyecto.service.DetallePedidoService;
import com.slamracing.proyecto.service.PedidoService;
import com.slamracing.proyecto.service.ProductoService;
import com.slamracing.proyecto.service.UsuarioService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Controller
@RequestMapping("/")
public class HomeController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    UsuarioService usuarioService;

    @Value("${mercado_pago_token}")
    private String mercadoPagoPublicKey;

    @Setter
    @Getter
    private Pedido pedido;

    @GetMapping("")
    public String homeUser() {
        return "user/home";
    }

    @GetMapping("/productos")
    public String productos(Model model) {
        List<Producto> productos = productoService.listarProductos();
        // Obtener la lista de productos y agregarla al modelo como lo estabas haciendo
        model.addAttribute("productos", productos);

        // Lista para nombres únicos
        Set<String> nombresUnicos = new HashSet<>();
        // Lista para materiales únicos
        Set<String> materialesUnicos = new HashSet<>();
        // Lista para colores únicos
        Set<String> coloresUnicos = new HashSet<>();

        // Iterar sobre la lista de productos para extraer los nombres únicos, materiales únicos y colores únicos
        for (Producto producto : productos) {
            nombresUnicos.add(producto.getNombre());
            materialesUnicos.add(producto.getMaterial());
            coloresUnicos.add(producto.getColor());
        }

        // Agregar los conjuntos de nombres únicos, materiales únicos y colores únicos al modelo
        model.addAttribute("nombresUnicos", nombresUnicos);
        model.addAttribute("materialesUnicos", materialesUnicos);
        model.addAttribute("coloresUnicos", coloresUnicos);

        List<Producto> productosFiltro = new ArrayList<>();

        for (Producto producto : productos) {
            Producto productoFiltrado = new Producto();
            productoFiltrado.setId(producto.getId());
            productoFiltrado.setNombre(producto.getNombre());
            productoFiltrado.setDescripcion(producto.getDescripcion());
            productoFiltrado.setPrecio_unitario(producto.getPrecio_unitario());
            productoFiltrado.setStock(producto.getStock());
            productoFiltrado.setDescuento(producto.getDescuento());
            productoFiltrado.setColor(producto.getColor());
            productoFiltrado.setMaterial(producto.getMaterial());
            productoFiltrado.setSlug(producto.getSlug());

            productosFiltro.add(productoFiltrado);
            System.out.println("Producto filtrado: " + productoFiltrado);
        }

        model.addAttribute("productosFiltro", productosFiltro);

        return "user/productos";
    }

    @GetMapping("/productos/{slug}")
    public String informacionProducto(@PathVariable String slug, Model model) {
        // Buscar el producto por su slug
        Producto productoDb = productoService.buscarProductoPorSlug(slug);
        log.info("Informacion del producto: {}", productoDb);
        model.addAttribute("producto", productoDb);

        // Buscar todos los productos con el mismo nombre
        List<Producto> productosPorNombre = productoService.listarProductosPorNombre(productoDb.getNombre());
        log.info("Productos con el mismo nombre: {}", productosPorNombre);
        model.addAttribute("productosPorNombre", productosPorNombre);

        return "user/informacionProducto";
    }



    @GetMapping("/procesoPago")
    public String procesoPago(Model model) {
        // Si el pedido no es nulo, agregarlo al modelo
        if (pedido != null) {
            log.info("Pedido recibido en procesoPago: {}", pedido);
            model.addAttribute("pedido", pedido);
            model.addAttribute("detalle", pedido.getDetalles());
            model.addAttribute("public_key", mercadoPagoPublicKey);
        }
        return "user/comprar";
    }

}
