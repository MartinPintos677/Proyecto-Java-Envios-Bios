package com.example.envios_bios.controladores;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ControladorRegistro {
    
    @GetMapping("/registro")
    public String mostrarFormularioRegistro() {
        return "registro"; 
    }
}
