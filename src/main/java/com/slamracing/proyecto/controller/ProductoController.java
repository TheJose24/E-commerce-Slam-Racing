package com.slamracing.proyecto.controller;

import com.slamracing.proyecto.model.Producto;
import com.slamracing.proyecto.model.ProductoImagen;
import com.slamracing.proyecto.service.ProductoService;
import com.slamracing.proyecto.service.SubirArchivoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/producto")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private SubirArchivoService subirArchivoService;


    @PostMapping("/agregarProducto")
    public String agregarProducto(Producto producto, @RequestParam("imagen") MultipartFile[] files) {
        if (producto.getId() == null) {
            List<ProductoImagen> imagenes = new ArrayList<>();
            for (MultipartFile file : files) {
                String nombreImagen = subirArchivoService.guardarImagen(file);
                ProductoImagen imagen = new ProductoImagen();
                imagen.setUrl(nombreImagen);
                imagen.setProducto(producto);
                imagenes.add(imagen);
            }
            producto.setImagenes(imagenes);
        }
        productoService.agregarProducto(producto);
        log.info("Producto agregado {}", producto);
        return "redirect:/admin/productos";
    }


    @PostMapping("/actualizarProducto")
    public String actualizarProducto(Producto producto, @RequestParam("imagen") MultipartFile[] files) {
        // Busca el producto en la base de datos
        System.out.println("=============================================");
        Producto productodb = productoService.buscarProductoPorId(producto.getId());
        System.out.println("Producto base de datos: " + productodb);
        System.out.println("Producto recibido para actualizar: " + producto);
        System.out.println("=============================================");

        // Verifica si se proporcionaron archivos y si la matriz de archivos no está vacía
        if (files != null && files.length > 0) {
            System.out.println("=============================================");
            System.out.println("Archivos proporcionados: " + files.length);

            // Lista para almacenar las nuevas imágenes
            List<ProductoImagen> nuevasImagenes = new ArrayList<>();
            List<ProductoImagen> imagenesDb = productodb.getImagenes();
            System.out.println("Imagenes de bd: " + imagenesDb.size());
            System.out.println("=============================================");

            // Itera sobre los archivos proporcionados
            for (int i = 0; i < files.length; i++) {
                System.out.println("=============================================");
                MultipartFile file = files[i];
                System.out.println("Archivo: " + file.getOriginalFilename());
                // Verifica si el archivo actual no está vacío
                System.out.println("=============================================");
                    if (!file.isEmpty()) {
                        System.out.println("Archivo no vacío");

                        // Guarda la nueva imagen y obtiene su URL
                        String nombreImagen = subirArchivoService.guardarImagen(file);
                        System.out.println("Imagen guardada: " + nombreImagen);

                        // Crea una nueva instancia de ProductoImagen y la agrega a la lista de nuevas imágenes
                        ProductoImagen nuevaImagen = new ProductoImagen();
                        System.out.println("numero de iteracion: "+i);
                        System.out.println("numero de imagenes registradas: "+imagenesDb.size());
                        if (i < imagenesDb.size()) {
                            System.out.println("Se cumple condicion");
                            nuevaImagen.setId(imagenesDb.get(i).getId());
                            if (!imagenesDb.get(i).getUrl().equals("default.png")) {
                                subirArchivoService.eliminarImagen(imagenesDb.get(i).getUrl());
                            }
                        }
                        nuevaImagen.setUrl(nombreImagen);
                        nuevaImagen.setProducto(producto);
                        nuevasImagenes.add(nuevaImagen);
                        System.out.println("Nueva imagen: " + nuevaImagen);
                    }else {
                        nuevasImagenes.add(imagenesDb.get(i));
                        System.out.println("Archivo vacío, guardando imagen de bd: "+imagenesDb.get(i));
                    }


            }

            System.out.println("=============================================");
            // Reemplaza las imágenes existentes con las nuevas imágenes
            producto.setImagenes(nuevasImagenes);
            System.out.println("Imagenes actualizadas: " + producto.getImagenes());
        }

        // Guarda el producto actualizado en la base de datos
        try {
            productoService.actualizarProducto(producto);
            System.out.println("Producto actualizado: " + producto);
            log.info("Producto actualizado {}", producto);
        } catch (Exception e) {
            // Maneja el error de manera adecuada, por ejemplo, mostrando un mensaje de error al usuario
            System.out.println("Error al actualizar el producto: " + e.getMessage());
            log.error("Error al actualizar el producto: {}", e.getMessage());
        }

        return "redirect:/admin/productos";
    }


    @GetMapping("/eliminarProducto/{id}")
    public String eliminarProducto(@PathVariable Long id) {
        Producto productodb = productoService.buscarProductoPorId(id);

        List<ProductoImagen> imagenes = productodb.getImagenes();

        for (ProductoImagen imagen : imagenes) {
            if (!imagen.getUrl().equals("default.png")) {
                subirArchivoService.eliminarImagen(imagen.getUrl());
            }
        }

        log.info("Producto eliminado {}", productodb);
        productoService.eliminarProducto(id);
        return "redirect:/admin/productos";
    }




}
