package com.example.envios_bios.controladores;

import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.envios_bios.dominio.Cliente;
import com.example.envios_bios.dominio.Paquete;
import com.example.envios_bios.excepciones.ExcepcionEnviosBios;
import com.example.envios_bios.servicios.IServicioCliente;
import com.example.envios_bios.servicios.IServicioPaquete;

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

    @Autowired
    private IServicioPaquete servicioPaquete;

    @GetMapping("/registrarcliente")
    public String mostrarRegistroCliente(@ModelAttribute Cliente cliente) {
        return "clientes/registro";
    }

    @PostMapping("/registrarcliente")
    public String procesarRegistroCliente(@ModelAttribute @Valid Cliente cliente, BindingResult result, Model model,
            RedirectAttributes attributes) {
        if (result.hasErrors()) {
            return "clientes/registro";
        }

        try {
            if (!cliente.getClaveDeAcceso().equals(cliente.getRepetirClaveDeAcceso())) {
                model.addAttribute("mensaje", "¡ERROR! Las contraseñas no coinciden ");
                return "clientes/registro";
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
    public String procesarModificar(Principal principal, @ModelAttribute @Valid Cliente cliente, BindingResult result,
            String contrasenaFalsa, Model model, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            model.addAttribute("contrasenaFalsa", UUID.randomUUID().toString());

            return "clientes/modificar";
        }

        try {
            if (principal == null || principal instanceof AnonymousAuthenticationToken) {
                throw new ExcepcionEnviosBios("Debes ingresar a tu cuenta para poder modificarla.");
            }

            cliente.setNombreUsuario(principal.getName());

            if (cliente.getRepetirClaveDeAcceso() == null
                    || !cliente.getRepetirClaveDeAcceso().equals(cliente.getClaveDeAcceso())) {
                model.addAttribute("contrasenaFalsa", UUID.randomUUID().toString());

                throw new ExcepcionEnviosBios("Las contraseñas no coinciden.");
            }

            Cliente bdCli = servicioCliente.obtener(principal.getName());

            System.out.println("Contraseña en bd: " + bdCli.getClaveDeAcceso());

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
    public String mostrarEliminar(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Cliente cli = servicioCliente.obtener(authentication.getName());
        model.addAttribute("cliente", cli);
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

    @GetMapping("/listarpaquetes")
    public String mostrarPaquetes(@RequestParam(required = false) String destinatario, Pageable pageable, Model model,
            Principal principal) {
        Page<Paquete> paquetesPage;

        if (destinatario != null && destinatario.matches("\\d+")) {
            // Si `destinatario` contiene solo dígitos, buscar por ID de paquete
            Long idPaquete = Long.parseLong(destinatario);
            paquetesPage = servicioPaquete.buscarPorIdClienteYIdPaquete(principal.getName(), idPaquete, pageable);
        } else {
            // De lo contrario, buscar por nombre del destinatario
            paquetesPage = servicioPaquete.listarPaquetesCliente(principal.getName(), destinatario, pageable);
        }

        // Agregar los resultados y la paginación al modelo
        model.addAttribute("paquetes", paquetesPage.getContent());
        model.addAttribute("totalPages", paquetesPage.getTotalPages());
        model.addAttribute("currentPage", paquetesPage.getNumber());
        model.addAttribute("pageSize", paquetesPage.getSize());

        return "clientes/paquetes";
    }

}
