package com.example.envios_bios.controladores;



import java.security.Principal;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class ControladorInicio {
    
    @GetMapping
    public String mostarInicio(Principal principal){
        if (principal == null || principal instanceof AnonymousAuthenticationToken) {
            return "index";
        } else {
            return "redirect:/bienvenida";
        }
    }

    @GetMapping("/bienvenida")
    public String bienvenida(){
        return "bienvenida";
    }
}
