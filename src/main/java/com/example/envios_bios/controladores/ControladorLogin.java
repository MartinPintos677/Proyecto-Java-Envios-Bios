package com.example.envios_bios.controladores;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;



@Controller
public class ControladorLogin {
    @GetMapping("/login")
    public String mostrarLogin() {
        return "login";  
    }

    @GetMapping("/pantalla-principal")
    public String mostrarPantallaPrincipal() {
        return "pantalla-principal"; 
    }
}
