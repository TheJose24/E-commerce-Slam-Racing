package com.slamracing.proyecto.controller;

import com.slamracing.proyecto.model.DetallePedido;
import com.slamracing.proyecto.model.Pedido;
import com.slamracing.proyecto.model.Producto;
import com.slamracing.proyecto.service.ProductoService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/v1/carrito")
public class CarritoController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private HomeController homeController;

    @Getter
    @Setter
    List<DetallePedido> detalles = new ArrayList<>();

    @Getter
    @Setter
    Pedido pedido = new Pedido();

    @PostMapping("/agregarProducto")
    public void carrito(@RequestBody Long id) {
        log.info("ID del producto recibido: {}", id);
        DetallePedido detalle = new DetallePedido();

        log.info("Buscando producto en la base de datos con ID: {}", id);
        Producto productoDb = productoService.buscarProductoPorId(id);
        log.info("Producto encontrado: {}", productoDb);

        double sumaTotalProductos = 0;
        int cantidad = 1;


        detalle.setProducto(productoDb);
        detalle.setCantidad(cantidad);

        BigDecimal precioUnitario = productoDb.getPrecio_unitario();
        BigDecimal descuento = BigDecimal.valueOf(productoDb.getDescuento() / 100.0);
        BigDecimal totalDescuento = precioUnitario.multiply(descuento);
        BigDecimal precioConDescuento = precioUnitario.subtract(totalDescuento);
        BigDecimal total = precioConDescuento.multiply(BigDecimal.valueOf(cantidad));

        detalle.setTotal(total);

        // Validar que el se añada 2 veces
        Long idProducto = productoDb.getId();
        boolean ingresado = detalles.stream().anyMatch(dt -> dt.getProducto().getId().equals(idProducto));

        if (!ingresado){
            detalles.add(detalle);
        }

        sumaTotalProductos = detalles.stream().mapToDouble(dt -> dt.getTotal().doubleValue()).sum();
        log.info("Suma total de los productos en el carrito: {}", sumaTotalProductos);

        pedido.setSubTotal(BigDecimal.valueOf(sumaTotalProductos));
        pedido.setEnvio(BigDecimal.valueOf(200));
        pedido.setPrecioTotal(pedido.getSubTotal().add(pedido.getEnvio()));
        pedido.setDetalles(detalles);
        log.info("Precio total del pedido actualizado: {}", pedido.getPrecioTotal());
        log.info("pedido: {}", pedido);
    }


    @DeleteMapping("/eliminarProducto/{id}")
    public ResponseEntity<String> eliminarProducto(@PathVariable Long id) {
        log.info("ID del producto recibido: {}", id);

        // Buscar el producto en la lista de detalles del pedido
        DetallePedido detalleAEliminar = null;
        for (DetallePedido detalle : detalles) {
            if (detalle.getProducto().getId().equals(id)) {
                detalleAEliminar = detalle;
                System.out.println(detalleAEliminar);
                break;
            }
        }

        if (detalleAEliminar == null) {
            log.warn("No se encontró el producto con ID: {}", id);
            return ResponseEntity.notFound().build();
        }

        // Eliminar el detalle del pedido
        detalles.remove(detalleAEliminar);
        System.out.println("Detalle eliminado");
        System.out.println(detalles);

        if (detalles.isEmpty()) {
            pedido.setSubTotal(BigDecimal.valueOf(0));
            pedido.setPrecioTotal(BigDecimal.valueOf(0));
            pedido.setEnvio(BigDecimal.valueOf(0));
            pedido.setDetalles(detalles);
            return ResponseEntity.ok("Carrito vacío");
        }

        // Calcular el nuevo subtotal
        BigDecimal subtotal = detalles.stream()
                .map(DetallePedido::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Calcular el total sumando el subtotal y el precio de envío
        BigDecimal total = subtotal.add(pedido.getEnvio());

        pedido.setSubTotal(subtotal);
        pedido.setPrecioTotal(total);
        pedido.setDetalles(detalles);

        log.info("Producto eliminado del carrito: {}", detalleAEliminar);
        log.info("Precio total del pedido actualizado: {}", pedido.getPrecioTotal());
        log.info("pedido eliminado: {}", pedido);

        return ResponseEntity.ok("Producto eliminado del carrito");
    }


    @GetMapping("/verCarrito")
    public Pedido verCarrito() {
        return pedido;
    }

    @PostMapping("/pagar")
    public ResponseEntity<Void> pagar(@RequestBody Pedido pedidoRequest) {
        log.info("Entrando al método pagar");
        log.info("Pedido recibido: {}", pedido);
        pedido = pedidoRequest;
        homeController.setPedido(pedido);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
