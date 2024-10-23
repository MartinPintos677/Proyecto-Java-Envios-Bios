package com.example.envios_bios.controladores;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.example.envios_bios.dominio.Empleado;
import com.example.envios_bios.servicios.IServicioEmpleado;
import com.example.envios_bios.servicios.IServicioSucursales;

import jakarta.validation.Valid;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.PostMapping;
import com.example.envios_bios.excepciones.ExcepcionEnviosBios;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequestMapping("/empleados")
public class ControladorEmpleado {

    @Autowired
    private IServicioSucursales servicioSucursales;

    @Autowired
    private IServicioEmpleado servicioEmpleado;

    @GetMapping
    public String mostrarEmpleado(@RequestParam(required = false) String criterio, Pageable pageable, Model model) {
        if (criterio == null) {
            criterio = ""; // Evitar nulos
        }

        // Llamar al servicio para obtener los empleados con paginación
        Page<Empleado> empleadosPage = servicioEmpleado.buscarConPaginacion(criterio, pageable);

        // Agregar los resultados y la información de paginación al modelo
        model.addAttribute("empleado", empleadosPage.getContent()); // Lista de empleados
        model.addAttribute("totalPages", empleadosPage.getTotalPages());
        model.addAttribute("currentPage", empleadosPage.getNumber());
        model.addAttribute("pageSize", empleadosPage.getSize());
        model.addAttribute("criterio", criterio); // Para mantener el criterio de búsqueda

        return "empleados/empleados"; // Renderiza la vista de empleados
    }

    @GetMapping("/agregar")
    public String mostrarAgregar(@ModelAttribute Empleado empleado, Model model) {
        model.addAttribute("sucursales", servicioSucursales.listar());

        return "empleados/agregar";
    }

    @PostMapping("/agregar")
    public String agregarEmpleado(@ModelAttribute @Valid Empleado empleado, BindingResult result, Model model,
            RedirectAttributes attributes) {
        model.addAttribute("sucursales", servicioSucursales.listar());// Para que no se pierdan cuando tiremos los
                                                                      // errores

        if (result.hasErrors()) {// Si la validacion tiene errores
            return "empleados/agregar";// Devolvemos la misma vista, pero con los errores
        }
        try {
            servicioEmpleado.agregar(empleado);// Intentamos agregar el empleado

            attributes.addFlashAttribute("mensaje", "Empleado agregado con éxito.");

            return "redirect:/empleados"; // Redireciona a /empleados

        } catch (ExcepcionEnviosBios e) {
            model.addAttribute("mensaje", "¡ERROR! " + e.getMessage());

            return "empleados/agregar";
        }
    }

    @GetMapping("/{nombreUsuario}")
    public String mostrarDetalle(@PathVariable("nombreUsuario") String nombreUsuario, Model model) {
        Empleado empleado = servicioEmpleado.obtener(nombreUsuario);

        if (empleado != null) {
            model.addAttribute("empleado", empleado);
        } else {
            model.addAttribute("mensaje", "¡ERROR! No se encontró el empleado con el nombre " + nombreUsuario + ".");
        }

        return "empleados/detalle";
    }

    @GetMapping("/modificar")
    public String mostrarModificar(String nombreUsuario, Model model) {
        model.addAttribute("sucursales", servicioSucursales.listar());

        Empleado empleado = servicioEmpleado.obtener(nombreUsuario);

        if (empleado != null) {
            model.addAttribute("empleado", empleado);
            model.addAttribute("contrasenaFalsa", UUID.randomUUID().toString());// Le damos una contraseña falsa
        } else {
            model.addAttribute("mensaje", "¡ERROR! No se encontró el empleado con el nombre " + nombreUsuario + ".");
        }

        return "empleados/modificar";
    }

    @PostMapping("/modificar")
    public String procesarModificar(@ModelAttribute @Valid Empleado empleado, BindingResult result,
            String contrasenaFalsa, Model model, RedirectAttributes attributes) {
        model.addAttribute("sucursales", servicioSucursales.listar());

        if (result.hasErrors()) {
            model.addAttribute("contrasenaFalsa", UUID.randomUUID().toString());// Le damos una nueva contraseña falsa
            return "empleados/modificar";
        }

        try {

            Empleado bdEmp = servicioEmpleado.obtener(empleado.getNombreUsuario());

            if (contrasenaFalsa.equals(empleado.getClaveDeAcceso())) { // Si la contraseña es igual a la que le dimos,
                                                                       // es porque no cambió
                empleado.setClaveDeAcceso(bdEmp.getClaveDeAcceso());
            }

            servicioEmpleado.modificar(empleado);

            attributes.addFlashAttribute("mensaje", "Empleado modificado con éxito.");

            return "redirect:/empleados";

        } catch (ExcepcionEnviosBios e) {
            model.addAttribute("mensaje", "¡ERROR! " + e.getMessage());

            return "empleados/modificar";
        }
    }

    @GetMapping("/eliminar")
    public String mostrarEliminar(String nombreUsuario, Model model) {
        Empleado empleado = servicioEmpleado.obtener(nombreUsuario);

        if (empleado != null) {
            model.addAttribute("empleado", empleado);
        } else {
            model.addAttribute("mensaje", "¡ERROR! No se encontró el empleado con el nombre " + nombreUsuario + ".");
        }

        return "empleados/eliminar";
    }

    @PostMapping("/eliminar")
    public String procesarEliminar(String nombreUsuario, Model model, RedirectAttributes attributes) {
        try {
            servicioEmpleado.eliminar(nombreUsuario);

            attributes.addFlashAttribute("mensaje", "Empleado eliminado con éxito.");

            return "redirect:/empleados";
        } catch (ExcepcionEnviosBios e) {
            model.addAttribute("mensaje", "¡ERROR! " + e.getMessage());

            return "empleados/eliminar";
        }
    }

}
