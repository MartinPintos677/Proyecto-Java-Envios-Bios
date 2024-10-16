package com.example.envios_bios.controladores;

import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.envios_bios.dominio.Cliente;
import com.example.envios_bios.excepciones.ExcepcionEnviosBios;
import com.example.envios_bios.servicios.IServicioCliente;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.security.core.Authentication;

@Controller
@RequestMapping("/clientes")
public class ControladorCliente {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private IServicioCliente servicioCliente;

    @GetMapping("/registrarcliente")
    public String mostrarRegistroCliente(@ModelAttribute Cliente cliente) {
        return "clientes/registro";
    }

    @PostMapping("/registrarcliente")
    public String procesarRegistroCliente(@ModelAttribute @Valid Cliente cliente, BindingResult result, Model model, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            return "clientes/registro";
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

            return "clientes/registro";
        }
    }

    @GetMapping
    public String mostrarModificar(Principal principal, Model model) {
        try {
            if (principal == null || principal instanceof AnonymousAuthenticationToken) {
                throw new ExcepcionEnviosBios("Debes ingresar a tu cuenta para poder ver tu información.");
            }

            Cliente cliente = servicioCliente.obtener(principal.getName());

            model.addAttribute("cliente", cliente);
            model.addAttribute("contrasenaFalsa", UUID.randomUUID().toString());

            return "clientes/modificar";
        } catch (ExcepcionEnviosBios e) {
            model.addAttribute("mensaje", "¡ERROR! " + e.getMessage());

            return "clientes/modificar";
        }
    }

    @PostMapping
    public String procesarModificar(Principal principal, @ModelAttribute @Valid Cliente cliente, BindingResult result, String contrasenaFalsa, Model model, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            model.addAttribute("contrasenaFalsa", UUID.randomUUID().toString());

            return "clientes/modificar";
        }

        try {
            if (principal == null || principal instanceof AnonymousAuthenticationToken) {
                throw new ExcepcionEnviosBios("Debes ingresar a tu cuenta para poder modificarla.");
            }

            cliente.setNombreUsuario(principal.getName());

            if (cliente.getRepetirClaveDeAcceso() == null || !cliente.getRepetirClaveDeAcceso().equals(cliente.getClaveDeAcceso())) {
                model.addAttribute("contrasenaFalsa", UUID.randomUUID().toString());

                throw new ExcepcionEnviosBios("Las contraseñas no coinciden.");
            }

            Cliente bdCli = servicioCliente.obtener(principal.getName());

            if (contrasenaFalsa.equals(cliente.getClaveDeAcceso())) {
                cliente.setClaveDeAcceso(bdCli.getClaveDeAcceso());
            } else {
                cliente.setClaveDeAcceso(passwordEncoder.encode(cliente.getClaveDeAcceso()));
            }

            servicioCliente.modificar(cliente);

            attributes.addFlashAttribute("mensaje", "Cliente modificado con éxito.");

            return "redirect:/clientes";
        } catch (ExcepcionEnviosBios e) {
            model.addAttribute("mensaje", "¡ERROR! " + e.getMessage());

            return "clientes/modificar";
        }
    }

    @GetMapping("/eliminar")
    public String mostrarEliminar() {
        return "clientes/eliminar";
    }

    @PostMapping("/eliminar")
    public String procesarEliminar(HttpServletRequest request, Model model, RedirectAttributes attributes) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
                throw new ExcepcionEnviosBios("Debes ingresar a tu cuenta para poder eliminarla.");
            }

            servicioCliente.eliminar(authentication.getName());

            new SecurityContextLogoutHandler().logout(request, null, null);
            SecurityContextHolder.getContext().setAuthentication(null);

            attributes.addFlashAttribute("mensaje", "Cliente eliminado con éxito.");

            return "redirect:/";
        } catch (ExcepcionEnviosBios e) {
            model.addAttribute("mensaje", "¡ERROR! " + e.getMessage());

            return "clientes/eliminar";
        }
    }
    
}
