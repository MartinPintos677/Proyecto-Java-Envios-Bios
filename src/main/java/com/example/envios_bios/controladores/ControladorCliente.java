package com.example.envios_bios.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.envios_bios.dominio.Cliente;
import com.example.envios_bios.excepciones.ExcepcionEnviosBios;
import com.example.envios_bios.servicios.IServicioCliente;

import jakarta.validation.Valid;

@Controller
public class ControladorCliente {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private IServicioCliente servicioCliente;

    @GetMapping("/registrarcliente")
    public String mostrarRegistroCliente(@ModelAttribute Cliente cliente) {
        return "cliente/registro";
    }

    @PostMapping("/registrarcliente")
    public String procesarRegistroCliente(@ModelAttribute @Valid Cliente cliente, BindingResult result, Model model, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            return "cliente/registro";
        }

        try {
            if (cliente.getRepetirClaveDeAcceso() == null || !cliente.getRepetirClaveDeAcceso().equals(cliente.getClaveDeAcceso())) {
                throw new ExcepcionEnviosBios("Las contraseñas no coinciden.");
            }

            cliente.setClaveDeAcceso(passwordEncoder.encode(cliente.getClaveDeAcceso()));

            servicioCliente.registrarse(cliente);

            attributes.addFlashAttribute("mensaje", "Cliente registrado con éxito.");

            return "redirect:/";

        } catch (ExcepcionEnviosBios e) {
            model.addAttribute("mensaje", "¡ERROR! " + e.getMessage());

            return "cliente/registro";
        }
    }
    
}
