package com.slamracing.proyecto.service;

import com.slamracing.proyecto.model.Pago;
import com.slamracing.proyecto.repository.VentasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VentasService {

    @Autowired
    private VentasRepository ventasRepository;

    public List<Pago> listarVentas() {
        return ventasRepository.findAll();
    }
}
