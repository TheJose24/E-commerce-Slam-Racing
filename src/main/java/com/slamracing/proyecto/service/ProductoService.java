package com.slamracing.proyecto.service;

import com.slamracing.proyecto.model.Producto;
import com.slamracing.proyecto.model.ProductoImagen;
import com.slamracing.proyecto.model.ReviewProducto;
import com.slamracing.proyecto.repository.ProductoImagenRepository;
import com.slamracing.proyecto.repository.ProductoRepository;
import com.slamracing.proyecto.repository.ReviewProductoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
@Slf4j
@Service
public class ProductoService {

    @Autowired
    ProductoRepository productoRepository;

    @Autowired
    ProductoImagenRepository productoImagenRepository;

    @Autowired
    ReviewProductoRepository reviewProductoRepository;

    public void agregarReviewProducto(ReviewProducto reviewProducto) {

        Long productoId = reviewProducto.getProducto().getId();
        Producto producto = buscarProductoPorId(productoId);
        int numOpiniones = producto.getNumOpiniones();
        double puntuacionPromedio = producto.getPuntuacionPromedio();

        // Actualizar el número de opiniones y la puntuación promedio
        if (numOpiniones != 0) {
            puntuacionPromedio = (puntuacionPromedio * numOpiniones + reviewProducto.getPuntuacion()) / (numOpiniones + 1);
            producto.setNumOpiniones(numOpiniones + 1);
        } else {
            puntuacionPromedio = reviewProducto.getPuntuacion();
            producto.setNumOpiniones(1);
        }

        // Actualizar el producto con la nueva información
        producto.setPuntuacionPromedio(puntuacionPromedio);


        reviewProducto.setProducto(producto);
        reviewProductoRepository.save(reviewProducto);
        System.out.println("Review guardado: " + reviewProducto);

    }


    public List<ReviewProducto> opinionesPorProducto(Long idProducto) {
        return reviewProductoRepository.findAllByProductoId(idProducto);
    }

    public String generarNombreUrl(String nombre, String color) {
        return nombre.toLowerCase().replace(" ", "-") + "-" + color.toLowerCase().replace(" ", "-");

    }
    public Producto agregarProducto(Producto producto) {
        producto.setSlug(generarNombreUrl(producto.getNombre(), producto.getColor()));

        for (ProductoImagen imagen : producto.getImagenes()) {
            imagen.setProducto(producto);
        }

        if (producto.getDescuento() == null) {
            producto.setDescuento(0);
        }
        if (producto.getStock() == null) {
            producto.setStock(0);
        }
        if (producto.getPuntuacionPromedio() == 0){
            producto.setPuntuacionPromedio(0.0);
        }

        return productoRepository.save(producto);
    }

    public List<Producto> listarProductos() {
        return productoRepository.findAll();
    }

    public List<Producto> listarProductosPorNombre(String nombre) {
        return productoRepository.findAllByNombre(nombre);
    }

    public Producto buscarProductoPorId(Long id) {
        return productoRepository.findById(id)
                .orElseThrow (() -> new RuntimeException("Producto no encontrado"));
    }

    public Producto buscarProductoPorSlug(String slug) {
        return productoRepository.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
    }

    public void actualizarProducto(Producto producto) {

        if (producto.getId() == null) {
            throw new IllegalArgumentException("El ID del producto no puede ser nulo");
        }

        System.out.println("Producto obtenido de el controlador: " + producto);

        Producto productoDb = buscarProductoPorId(producto.getId());

        // Actualizar las propiedades del producto
        producto.setNombre(producto.getNombre() != null && !producto.getNombre().isEmpty() ? producto.getNombre() : productoDb.getNombre());
        producto.setDescripcion(producto.getDescripcion() != null && !producto.getDescripcion().isEmpty() ? producto.getDescripcion() : productoDb.getDescripcion());
        producto.setMaterial(producto.getMaterial() != null && !producto.getMaterial().isEmpty() ? producto.getMaterial() : productoDb.getMaterial());
        producto.setPrecio_unitario(producto.getPrecio_unitario() != null ? producto.getPrecio_unitario() : productoDb.getPrecio_unitario());
        producto.setDescuento(producto.getDescuento() !=null  ? producto.getDescuento() : productoDb.getDescuento());
        producto.setStock(producto.getStock() != null ? producto.getStock() : productoDb.getStock());
        producto.setColor(producto.getColor() != null && !producto.getColor().isEmpty() ? producto.getColor() : productoDb.getColor());
        producto.setNumOpiniones(productoDb.getNumOpiniones());
        producto.setPuntuacionPromedio(productoDb.getPuntuacionPromedio());

        // Generar el slug si el nombre o el color han cambiado
        boolean nombreCambiado = !producto.getNombre().equals(productoDb.getNombre());
        boolean colorCambiado = !producto.getColor().equals(productoDb.getColor());

        if (nombreCambiado || colorCambiado ) {
            producto.setSlug(generarNombreUrl(producto.getNombre(), producto.getColor()));
        } else {
            producto.setSlug(productoDb.getSlug());
        }

        // Verificar si hay imágenes asociadas al producto
        if (producto.getImagenes() != null && !producto.getImagenes().isEmpty()) {
            System.out.println("Imagenes del producto desde el servicio: " + producto.getImagenes());
            for (ProductoImagen imagen : producto.getImagenes()) {
                System.out.println("Imagen: " + imagen);
                // Establecer la relación con el producto
                imagen.setProducto(productoDb);
                System.out.println("Imagen actualizada: " + imagen);

                if (imagen.getId() == null) {
                    productoImagenRepository.save(imagen);
                }
            }
        }

        try {
            System.out.println("Producto actualizado desde el servicio: " + producto);
            productoRepository.save(producto);
        } catch (Exception e) {
            log.error("Error al actualizar el producto con ID: " + producto.getId(), e);
            throw new RuntimeException("Error al actualizar el producto. Consulta los registros para más detalles.", e);
        }
    }

    public void eliminarProducto(Long id) {
        productoRepository.deleteById(id);
    }




}
