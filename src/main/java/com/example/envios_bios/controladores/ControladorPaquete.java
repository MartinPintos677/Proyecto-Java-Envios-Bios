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
  public String mostrarFormularioAgregarPaquete(
      @RequestParam(value = "cedula", required = false) String cedula,
      @RequestParam(value = "buscarCliente", required = false) String buscarCliente,
      Model model, Authentication authentication) {

    // Crear un objeto paquete vacío para el formulario
    Paquete paquete = new Paquete();
    paquete.setFechaHoraRegistro(LocalDateTime.now());
    model.addAttribute("paquete", paquete);

    // Obtener el usuario autenticado
    if (authentication != null) {
      String nombreUsuario = authentication.getName();

      // Si es un cliente autenticado
      if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("cliente"))) {
        Cliente cliente = servicioCliente.obtener(nombreUsuario);
        model.addAttribute("cliente", cliente); // Agrega el cliente al modelo

        // Si es un empleado autenticado
      } else if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("empleado"))) {

        // Si se realizó una búsqueda de cliente por cédula
        if (buscarCliente != null && cedula != null) {
          Cliente cliente = servicioCliente.obtener(cedula);

          if (cliente != null) {
            model.addAttribute("cliente", cliente); // Agregar cliente si se encuentra
          } else {
            model.addAttribute("error", "Cliente no encontrado con la cédula proporcionada.");
          }
        }
      }

      // Cargar listas para los dropdowns
      List<Categoria> categorias = servicioCategoria.listar();
      List<EstadoRastreo> estadosRastreo = servicioEstadoRastreo.listar();
      model.addAttribute("categorias", categorias);
      model.addAttribute("estadosRastreo", estadosRastreo);
    }

    // Pasar el valor del botón de acción al formulario
    model.addAttribute("textoBoton", "Agregar");

    return "paquetes/agregar";
  }

  @PostMapping("/agregar")
  public String agregarPaquete(@ModelAttribute("paquete") Paquete paquete, Model model,
      RedirectAttributes redirectAttributes) {
    // Aquí iría la lógica para guardar el paquete
    try {
      servicioPaquete.agregarPaquete(paquete);

      // Mensaje de éxito
      redirectAttributes.addFlashAttribute("mensaje", "Paquete agregado correctamente.");
      return "redirect:/paquetes";
    } catch (ExcepcionEnviosBios e) {
      model.addAttribute("mensaje", e.getMessage());
      // recarga los datos del formulario
      List<Categoria> categorias = servicioCategoria.listar();
      List<EstadoRastreo> estadosRastreo = servicioEstadoRastreo.listar();
      model.addAttribute("categorias", categorias);
      model.addAttribute("estadosRastreo", estadosRastreo);
      return "paquetes/agregar";
    }
  }

  @GetMapping("/modificar")
  public String mostrarModificar(@RequestParam Long idPaquete, Model model) {
    Paquete paquete = servicioPaquete.obtenerPaquetePorId(idPaquete);
    if (paquete != null) {
      model.addAttribute("paquete", paquete);
    } else {
      model.addAttribute("mensaje", "¡ERROR! No se encontró el paquete con el id " + idPaquete + ".");
    }
    return "paquetes/modificar"; // Vista que contiene el formulario para modificar un paquete
  }

  @PostMapping("/modificar")
  public String procesarModificar(@ModelAttribute("paquete") @Valid Paquete paquete,
      BindingResult result,
      Model model,
      RedirectAttributes redirectAttributes) {

    if (result.hasErrors()) {
      return "paquetes/modificar";
    }
    try {
      servicioPaquete.modificarPaquete(paquete);
      redirectAttributes.addFlashAttribute("mensaje", "¡Paquete modificado exitosamente!");
      return "redirect:/paquetes";
    } catch (ExcepcionEnviosBios e) {
      model.addAttribute("mensaje", "¡ERROR! " + e.getMessage());
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
    return "paquetes/eliminar"; // Vista que contiene el formulario para eliminar un paquete
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
