package com.slamracing.proyecto.service;

import com.slamracing.proyecto.model.DetallePedido;
import com.slamracing.proyecto.repository.DetallePedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DetallePedidoService {

    @Autowired
    private DetallePedidoRepository detallePedidoRepository;

    public void guardarDetallePedido(DetallePedido detallePedido) {
        detallePedidoRepository.save(detallePedido);
    }

    public DetallePedido buscarDetallePedidoPorId(Long id) {
        return detallePedidoRepository.findById(id).orElse(null);
    }

    public void eliminarDetallePedidoPorId(Long id) {
        detallePedidoRepository.deleteById(id);
    }
}
