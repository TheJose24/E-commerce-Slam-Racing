package com.slamracing.proyecto.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.preference.Preference;
import com.slamracing.proyecto.model.DetallePedido;
import com.slamracing.proyecto.model.Pedido;
import com.slamracing.proyecto.model.Producto;
import com.slamracing.proyecto.model.Usuario;
import com.slamracing.proyecto.payment.dto.ItemDTO;
import com.slamracing.proyecto.payment.dto.PagoTarjetaDTO;
import com.slamracing.proyecto.payment.dto.PreferenciaRequestDTO;
import com.slamracing.proyecto.payment.service.PagoTarjetaService;
import com.slamracing.proyecto.payment.service.PreferenciaService;
import com.slamracing.proyecto.service.PedidoService;
import com.slamracing.proyecto.service.ProductoService;
import com.slamracing.proyecto.service.UsuarioService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/v1/mercadoPago")
public class MercadoPagoController {

    @Autowired
    private final PagoTarjetaService pagoTarjetaService;

    @Autowired
    private final PreferenciaService preferenciaService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ProductoService productoService;

    @Autowired
    private CarritoController carritoController;

    @Autowired
    PedidoService pedidoService;

    public MercadoPagoController(PagoTarjetaService pagoTarjetaService, PreferenciaService preferenciaService) {
        this.pagoTarjetaService = pagoTarjetaService;
        this.preferenciaService = preferenciaService;
    }

    @PostMapping("/preferencias/crear")
    public ResponseEntity<String> crearPreferencia(@RequestBody PreferenciaRequestDTO preferenciaRequestDTO) {
        System.out.println("request de preferencia recibido: " + preferenciaRequestDTO);
        String preferenceId = preferenciaService.createPreference(preferenciaRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(preferenceId);
    }

    @PostMapping("/procesar_pago")
    public ResponseEntity<Long> procesarPago(@RequestBody @Validated PagoTarjetaDTO pagoTarjetaDTO){
        Long paymentId = pagoTarjetaService.processPayment(pagoTarjetaDTO);
        System.out.println(paymentId.toString());
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentId);
    }

    @PostMapping("/generarPedido")
    public ResponseEntity<Long> generarPedido(@RequestBody Pedido pedido) {
        System.out.println("Pedido: " + pedido);
        Usuario usuarioBd = usuarioService.buscarUsuarioPorId(Long.parseLong("1"));
        pedido.setUsuario(usuarioBd);
        // Iterar sobre los detalles del pedido
        for (DetallePedido detalle : pedido.getDetalles()) {
            // Establecer la referencia al pedido en cada detalle del pedido
            detalle.setPedido(pedido);
        }
        pedido.setDetalles(pedido.getDetalles());
        // Establecer la fecha de creación del pedido
        LocalDateTime fechaCreacion = LocalDateTime.now();
        // Definir el formato deseado (solo fecha y hora con segundos)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        // Formatear la fecha y hora actual utilizando el formateador
        String fechaHoraFormateada = fechaCreacion.format(formatter);

        pedido.setFechaCreacion(fechaHoraFormateada);

        pedidoService.guardarPedido(pedido);

        log.info("Pedido guardado: {}", pedido);

        return ResponseEntity.status(HttpStatus.CREATED).body(pedido.getId());
    }


    @PostMapping("/notificacion")
    public ResponseEntity<String> recibirNotificacion(@RequestBody String body) {
        try {
            System.out.println("Webhook: " + body);

            // Analizar el JSON del cuerpo de la notificación para extraer el ID del pago
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode bodyJson = objectMapper.readTree(body);

            String paymentId = bodyJson.get("data").get("id").asText(); // Suponiendo que el ID del pago está en un campo llamado "payment_id"

            String pagoInfo = pagoTarjetaService.buscarPagoPorId(paymentId);

            ObjectMapper pagoMapper = new ObjectMapper();
            JsonNode pagoJson  = pagoMapper.readTree(pagoInfo);

            String estadoPago = pagoJson.get("status").asText();

            if ("approved".equals(estadoPago)) {
                // El pago fue aprobado, actualizar el stock del producto y el estado del pedido
                Pedido pedido = pedidoService.buscarPedidoPorId(Long.parseLong(pagoJson.get("external_reference").asText()));

                List<DetallePedido> detallesPedido = pedido.getDetalles();
                for (DetallePedido detalle : detallesPedido) {
                    Producto producto = detalle.getProducto();
                    producto.setStock(producto.getStock() - detalle.getCantidad());
                    productoService.actualizarProducto(producto);
                    carritoController.setPedido(new Pedido());
                    carritoController.setDetalles(new ArrayList<>());
                }

            }

            return ResponseEntity.status(HttpStatus.OK).body("Notificación recibida");
        } catch (JsonProcessingException e) {
            log.error("Error al analizar el JSON de la notificación: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al analizar el JSON de la notificación");
        } catch (Exception e) {
            log.error("Error al recibir la notificación: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al recibir la notificación");
        }
    }
}
