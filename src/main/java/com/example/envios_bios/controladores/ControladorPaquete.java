package com.example.envios_bios.controladores;

import java.time.LocalDateTime;
import java.util.List;

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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

import com.example.envios_bios.dominio.Categoria;
import com.example.envios_bios.dominio.Cliente;
import com.example.envios_bios.dominio.EstadoRastreo;
import com.example.envios_bios.dominio.Paquete;
import com.example.envios_bios.excepciones.ExcepcionEnviosBios;
import com.example.envios_bios.servicios.IServicioCliente;
import com.example.envios_bios.servicios.IServicioPaquete;
import com.example.envios_bios.servicios.ServicioCategoria;
import com.example.envios_bios.servicios.ServicioEstadoRastreo;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/paquetes")
public class ControladorPaquete {

  @Autowired
  private ServicioCategoria servicioCategoria;

  @Autowired
  private IServicioPaquete servicioPaquete;

  @Autowired
  private IServicioCliente servicioCliente;

  @Autowired
  private ServicioEstadoRastreo servicioEstadoRastreo;

  @GetMapping
  public String mostrarPaquetes(
      @RequestParam(required = false) String cedulaCliente,
      @RequestParam(required = false) String fechaRegistro,
      @RequestParam(required = false) String estadoRastreo,
      Pageable pageable,
      Model model) {

    // Llamar al servicio con paginación y filtros
    Page<Paquete> paquetesPage = servicioPaquete.buscarConPaginacion(cedulaCliente, fechaRegistro, estadoRastreo,
        pageable);

    // Añadir los paquetes y la información de paginación al modelo
    model.addAttribute("paquetes", paquetesPage.getContent());
    model.addAttribute("totalPages", paquetesPage.getTotalPages());
    model.addAttribute("currentPage", paquetesPage.getNumber());
    model.addAttribute("pageSize", paquetesPage.getSize());

    // Obtener los estados de rastreo filtrados
    List<String> estadosFiltrados = List.of("a levantar", "levantado", "en reparto", "entregado", "devuelto");
    List<EstadoRastreo> estadosRastreo = servicioEstadoRastreo.listar()
        .stream()
        .filter(estado -> estadosFiltrados.contains(estado.getDescripcion()))
        .toList();

    model.addAttribute("estadosRastreo", estadosRastreo);

    return "paquetes/paquetes";
  }

  @GetMapping("/agregar")
public String AgregarPaquete(Model model, Authentication authentication) {
    // Crear un objeto paquete vacío para el formulario
    Paquete paquete = new Paquete();
    paquete.setFechaHoraRegistro(LocalDateTime.now());
    model.addAttribute("paquete", paquete);

    // Obtener el usuario autenticado
    if (authentication != null) {
        String nombreUsuario = authentication.getName();

        // Si el usuario logueado tiene el rol "cliente"
        if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("cliente"))) {
            Cliente cliente = servicioCliente.obtener(nombreUsuario); // Obtener el cliente logueado
            model.addAttribute("cliente", cliente); // Agregar el cliente al modelo
        } 
        // Si el usuario logueado tiene el rol "empleado"
        else if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("empleado"))) {
            List<Cliente> clientes = servicioPaquete.listarClientes(); // Listar todos los clientes
            model.addAttribute("clientes", clientes); // Agregar la lista de clientes al modelo
        }

        // Cargar las listas necesarias para los dropdowns
        List<Categoria> categorias = servicioCategoria.listar();
        List<EstadoRastreo> estadosRastreo = servicioEstadoRastreo.listar();

        model.addAttribute("categorias", categorias);
        model.addAttribute("estadosRastreo", estadosRastreo);

        // Pasar el valor del botón de acción al formulario
        model.addAttribute("textoBoton", "Agregar");
    }

    return "paquetes/agregar";
}

@PostMapping("/agregar")
public String agregarPaquete(@ModelAttribute("paquete")  @Valid Paquete paquete, 
                             RedirectAttributes redirectAttributes, 
                             Authentication authentication, BindingResult result) {
    try {
        // Verifica si el usuario es cliente o empleado
        if (authentication != null) {
            String nombreUsuario = authentication.getName();

            // Si el usuario es un cliente, asignar su nombreUsuario al paquete
            if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("cliente"))) {
                Cliente cliente = servicioCliente.obtener(nombreUsuario);
                paquete.setCliente(cliente);
            }
            // Si el usuario es un empleado, ya debería haber seleccionado un cliente desde el dropdown
            // por lo que no es necesario hacer nada en este caso
        }
        if (result.hasErrors()) {

          return "paquetes/agregar";
      }

        // guarda el paquete en la base de datos
        servicioPaquete.agregarPaquete(paquete);

        // Agrega mensaje de éxito
        redirectAttributes.addFlashAttribute("mensaje", "Paquete agregado correctamente.");
        return "redirect:/paquetes";
    } catch (ExcepcionEnviosBios e) {
        // Si ocurre una excepción personalizada, mostrar mensaje de error
        redirectAttributes.addFlashAttribute("mensajeError", e.getMessage());
        return "redirect:/agregar";
    } catch (Exception e) {
        // En caso de un error general, agregar un mensaje genérico
        redirectAttributes.addFlashAttribute("mensajeError", "Ocurrió un error al agregar el paquete.");
        return "redirect:/paquetes";
    }
}

@GetMapping("/modificar")
public String mostrarModificar(@RequestParam("idPaquete") Long idPaquete, Model model) {
    Paquete paquete = servicioPaquete.obtenerPaquetePorId(idPaquete); // Buscamos el paquete por ID

    if (paquete != null) {
        model.addAttribute("paquete", paquete);
        
        // Cargar la lista de clientes para el dropdown
        List<Cliente> clientes = servicioPaquete.listarClientes();
        model.addAttribute("clientes", clientes);

        // Cargar la lista de categorías para el dropdown
        List<Categoria> categorias = servicioCategoria.listar();
        model.addAttribute("categorias", categorias);

        // Obtenemos la lista de estados de rastreo para el dropdown
        List<EstadoRastreo> estadosRastreo = servicioEstadoRastreo.listar();
        model.addAttribute("estadosRastreo", estadosRastreo);
        
        model.addAttribute("textoBoton", "Modificar");
        model.addAttribute("modoModificacion", true);  // Estamos en modo modificación
    } else {
        model.addAttribute("mensaje", "¡ERROR! No se encontró el paquete con el id " + idPaquete + ".");
    }

    return "paquetes/modificar";  
}
@PostMapping("/modificar")
public String procesarModificar(@RequestParam("idPaquete") Long idPaquete, 
                                      @RequestParam("estadoRastreo") Integer idRastreo, 
                                      RedirectAttributes attributes, Model model) {
    try {
        Paquete paquete = servicioPaquete.obtenerPaquetePorId(idPaquete);

        if (paquete == null) {
            attributes.addFlashAttribute("mensaje", "¡ERROR! No se encontró el paquete con el ID " + idPaquete);
            return "redirect:/paquetes";
        }

        // Obtener el estado de rastreo seleccionado en el dropdown
        EstadoRastreo estadoRastreo = servicioEstadoRastreo.obtener(idRastreo);
        if (estadoRastreo == null) {
            attributes.addFlashAttribute("mensaje", "¡ERROR! Estado de rastreo inválido.");
            return "redirect:/paquetes";
        }

        // Actualizar solo el campo estadoRastreo
        paquete.setEstadoRastreo(estadoRastreo);
        servicioPaquete.modificarPaquete(paquete);  // Guarda los cambios

        attributes.addFlashAttribute("mensaje", "Estado del paquete modificado con éxito.");
        return "redirect:/paquetes";

    } catch (ExcepcionEnviosBios e) {
        model.addAttribute("mensaje", "¡ERROR! " + e.getMessage());
        return "paquetes/modificar";
    } catch (Exception e) {
        model.addAttribute("mensaje", "Ocurrió un error al modificar el estado del paquete.");
        return "paquetes/modificar";
    }
}

  @GetMapping("/eliminar")
  public String mostrarEliminar(@RequestParam Long idPaquete, Model model) {
    Paquete paquete = servicioPaquete.obtenerPaquetePorId(idPaquete);
    if (paquete != null) {
      model.addAttribute("paquete", paquete);
    } else {
      model.addAttribute("mensaje", "¡ERROR! No se encontró el paquete con el id " + idPaquete + ".");
    }
    return "paquetes/eliminar"; 
  }

  @PostMapping("/eliminar")
  public String procesarEliminar(@RequestParam Long idPaquete, Model model, RedirectAttributes redirectAttributes) {
    try {
      servicioPaquete.eliminarPaquete(idPaquete);
      redirectAttributes.addFlashAttribute("mensaje", "¡Paquete eliminado exitosamente!");
      return "redirect:/paquetes";
    } catch (ExcepcionEnviosBios e) {
      model.addAttribute("mensaje", "¡ERROR! " + e.getMessage());
      return "paquetes/paquetes"; // Regresar a la lista en caso de error
    }
  }

  @GetMapping("/{idPaquete}")
  public String mostrarDetalle(@PathVariable("idPaquete") Long idPaquete, Model model) {
    Paquete paquete = servicioPaquete.obtenerPaquetePorId(idPaquete);
    if (paquete != null) {
      model.addAttribute("paquete", paquete);
    } else {
      model.addAttribute("mensaje", "¡ERROR! No se encontró el paquete con el id " + idPaquete + ".");
    }
    return "paquetes/detalle";
  }
}
