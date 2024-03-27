package com.slamracing.proyecto.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/cuentas")
public class LoginController {

    @GetMapping("/iniciarSesion")
    public String iniciarSesion() {
        return "login";
    }

    @GetMapping("/registrarse")
    public String registrarse() {
        return "login";
    }
}
