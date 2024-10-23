package com.example.envios_bios.controladores;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
import com.example.envios_bios.dominio.Sucursal;
import com.example.envios_bios.excepciones.ExcepcionEnviosBios;
import com.example.envios_bios.servicios.IServicioSucursales;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/sucursales")
public class ControladorSucursales {

    @Autowired
    private IServicioSucursales servicioSucursales;

    @GetMapping
    public String mostrarSucursales(@RequestParam(required = false) String criterio, Pageable pageable, Model model) {
        if (criterio == null) {
            criterio = ""; // Evitar nulos
        }

        // Llamar al servicio para obtener las sucursales paginadas
        Page<Sucursal> sucursalesP = servicioSucursales.buscarPaginado(criterio, pageable);

        // Agregar los resultados y los datos de la página al modelo
        model.addAttribute("sucursales", sucursalesP.getContent()); // La lista de sucursales
        model.addAttribute("page", sucursalesP); // El objeto de la página para la paginación
        model.addAttribute("criterio", criterio); // Mantener el criterio de búsqueda

        return "sucursales/sucursales"; // Renderiza la vista de sucursales
    }

    @GetMapping("/agregar")
    public String mostrarAgregar(Model model) {
        // Crear un objeto sucursal vacío para el formulario
        Sucursal sucursal = new Sucursal();
        model.addAttribute("sucursal", sucursal);
        // Pasar el valor del botón de acción al formulario
        model.addAttribute("textoBoton", "Agregar Sucursal");
        return "sucursales/agregar";
    }

    @PostMapping("/agregar")
    public String agregarSucursal(@ModelAttribute("sucursal") @Valid Sucursal sucursal, BindingResult result,
            RedirectAttributes redirectAttributes, Model model) {

        if (result.hasErrors()) {
            return "sucursales/agregar";
        }
        try {
            servicioSucursales.agregar(sucursal);

            // Añadir un mensaje de éxito a los atributos redireccionados
            redirectAttributes.addFlashAttribute("mensaje", "Sucursal agregada exitosamente!");

            // Redireccionar a la lista de sucursales
            return "redirect:/sucursales";
        } catch (ExcepcionEnviosBios e) {
            model.addAttribute("mensaje", e.getMessage());
            return "sucursales/agregar";
        }
    }

    @GetMapping("/modificar")
    public String mostrarModificar(Long numero, Model model) {
        model.addAttribute("categorias", servicioSucursales.listar());

        Sucursal sucursal = servicioSucursales.obtener(numero);

        if (sucursal != null) {
            model.addAttribute("sucursal", sucursal);
        } else {
            model.addAttribute("mensaje", "¡ERROR! No se encontró la sucursal con el id " + numero + ".");
        }

        return "sucursales/modificar";
    }

    @PostMapping("/modificar")
    public String procesarModificar(@ModelAttribute @Valid Sucursal sucursal, BindingResult result, Model model,
            RedirectAttributes attributes) {
        model.addAttribute("sucursales", servicioSucursales.listar());

        if (result.hasErrors()) {
            return "sucursales/modificar";
        }
        try {

            servicioSucursales.modificar(sucursal);
            attributes.addFlashAttribute("mensaje", "Sucursal modificada con éxito.");
            return "redirect:/sucursales";
        } catch (ExcepcionEnviosBios e) {
            model.addAttribute("mensaje", "¡ERROR! " + e.getMessage());

            return "sucursales/modificar";
        }
    }

    @GetMapping("/eliminar")
    public String mostrarEliminar(Long numero, Model model) {
        Sucursal sucursal = servicioSucursales.obtener(numero);

        if (sucursal != null) {
            model.addAttribute("sucursales", sucursal);
        } else {
            model.addAttribute("mensaje", "¡ERROR! No se encontró la sucursal con el código " + numero + ".");
        }

        return "sucursales/eliminar";
    }

    @PostMapping("/eliminar")
    public String procesarEliminar(Long numero, Model model, RedirectAttributes attributes) {
        try {
            servicioSucursales.eliminar(numero);
            attributes.addFlashAttribute("mensaje", "Sucursal eliminada con éxito.");
            return "redirect:/sucursales";
        } catch (ExcepcionEnviosBios e) {
            attributes.addFlashAttribute("mensaje", "¡ERROR! " + e.getMessage());
            return "redirect:/sucursales";
        }
    }

    @GetMapping("/{numero}")
    public String mostrarDetalle(@PathVariable("numero") Long numero, Model model) {
        Sucursal sucursal = servicioSucursales.obtener(numero);

        if (sucursal != null) {
            model.addAttribute("sucursales", sucursal);
        } else {
            model.addAttribute("mensaje", "¡ERROR! No se encontró la sucursal con el código " + numero + ".");
        }

        return "sucursales/detalle";
    }
}
