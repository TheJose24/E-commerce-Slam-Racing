package com.slamracing.proyecto.service;

import com.slamracing.proyecto.model.DetallePedido;
import com.slamracing.proyecto.model.Pedido;
import com.slamracing.proyecto.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PedidoService {

    @Autowired
    PedidoRepository pedidoRepository;

    public void guardarPedido(Pedido pedido) {
        pedidoRepository.save(pedido);
    }

    public Pedido buscarPedidoPorId(Long id) {
        return pedidoRepository.findById(id).orElse(null);
    }

}
