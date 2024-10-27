package com.example.envios_bios.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.example.envios_bios.dominio.EstadoRastreo;
import com.example.envios_bios.excepciones.ExcepcionEnviosBios;
import com.example.envios_bios.servicios.IServicioEstadoRastreo;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Controller
@RequestMapping("/estadosRastreos")
public class ControladorEstadoRastreo {
    @Autowired
    private IServicioEstadoRastreo servicioEstadoRastreo;

    @GetMapping
    public String mostrarEstados(@RequestParam(required = false) String criterio, Pageable pageable, Model model) {
        if (criterio == null) {
            criterio = ""; // Evitar nulos
        }
        Page<EstadoRastreo> rastreosP = servicioEstadoRastreo.buscarPaginado(criterio, pageable);
        model.addAttribute("rastreo", rastreosP.getContent());
        model.addAttribute("page", rastreosP);
        model.addAttribute("criterio", criterio);
        return "estadosRastreos/estadosRastreos";
    }

    @GetMapping("/agregar")
    public String mostrarAgregar(Model model) {
        // Crear un objeto estado vacío para el formulario
        EstadoRastreo rastreo = new EstadoRastreo();
        model.addAttribute("rastreo", rastreo);
        // Pasar el valor del botón de acción al formulario
        model.addAttribute("textoBoton", "Agregar Estado de Rastreo");
        return "estadosRastreos/agregar";
    }

    @PostMapping("/agregar")
    public String agregarEstadoRastreo(@ModelAttribute("rastreo") @Valid EstadoRastreo rastreo,
            BindingResult result, RedirectAttributes redirectAttributes, Model model) {

        if (result.hasErrors()) {

            return "estadosRastreos/agregar";
        }

        try {
            servicioEstadoRastreo.agregar(rastreo);
            redirectAttributes.addFlashAttribute("mensaje", "Estado de rastreo agregado exitosamente!");
            return "redirect:/estadosRastreos";
        } catch (ExcepcionEnviosBios e) {
            model.addAttribute("mensaje", e.getMessage());
            return "estadosRastreos/agregar";
        }
    }

    @GetMapping("/modificar")
    public String mostrarModificar(@RequestParam("idRastreo") Integer idRastreo, Model model) {
        EstadoRastreo rastreo = servicioEstadoRastreo.obtener(idRastreo);

        if (rastreo != null) {
            model.addAttribute("rastreo", rastreo); // Cambiar a 'rastreo'
            model.addAttribute("textoBoton", "Modificar Estado de Rastreo");
        } else {
            model.addAttribute("mensaje", "¡ERROR! No se encontró el Estado de Rastreo con el id " + idRastreo + ".");
        }

        return "estadosRastreos/modificar";
    }

    @PostMapping("/modificar")
    public String procesarModificar(@ModelAttribute("rastreo") @Valid EstadoRastreo rastreo, BindingResult result,
            Model model, RedirectAttributes attributes) {

        if (result.hasErrors()) {
            model.addAttribute("textoBoton", "Modificar Estado de Rastreo");
            return "estadosRastreos/modificar";
        }

        try {
            servicioEstadoRastreo.modificar(rastreo);
            attributes.addFlashAttribute("mensaje", "Estado de Rastreo modificado con éxito.");
            return "redirect:/estadosRastreos";
        } catch (ExcepcionEnviosBios e) {
            model.addAttribute("mensajeError", "¡ERROR! " + e.getMessage());
            model.addAttribute("textoBoton", "Modificar Estado de Rastreo");
            return "estadosRastreos/modificar";
        }
    }

    @GetMapping("/eliminar")
    public String mostrarEliminar(Integer idRastreo, Model model) {
        EstadoRastreo rastreo = servicioEstadoRastreo.obtener(idRastreo);

        if (rastreo != null) {
            model.addAttribute("rastreos", rastreo);
        } else {
            model.addAttribute("mensaje",
                    "¡ERROR! No se encontró el Estado de Rastreo con el código " + idRastreo + ".");
        }

        return "estadosRastreos/eliminar";
    }

    @PostMapping("/eliminar")
    public String procesarEliminar(Integer idRastreo, Model model, RedirectAttributes attributes) {
        try {
            servicioEstadoRastreo.eliminar(idRastreo);
            attributes.addFlashAttribute("mensaje", "Estado de Rastreo eliminado o desactivado con éxito.");
            return "redirect:/estadosRastreos";

        } catch (ExcepcionEnviosBios e) {
            attributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/estadosRastreos";
    }

    @GetMapping("/{idRastreo}")
    public String mostrarDetalle(@PathVariable("idRastreo") Integer idRastreo, Model model) {
        EstadoRastreo rastreo = servicioEstadoRastreo.obtener(idRastreo);

        if (rastreo != null) {
            model.addAttribute("rastreos", rastreo);
        } else {
            model.addAttribute("mensaje",
                    "¡ERROR! No se encontró el Estado de Rastreo con el código " + idRastreo + ".");
        }

        return "estadosRastreos/detalle";
    }
}
