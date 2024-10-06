package com.example.envios_bios.controladores;

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

// import com.envios_bios.dominio.Cliente;
import com.example.envios_bios.dominio.Paquete;
import com.example.envios_bios.excepciones.ExcepcionEnviosBios;
import com.example.envios_bios.servicios.IServicioPaquete;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/paquetes")
public class ControladorPaquete {

  @Autowired
  private IServicioPaquete servicioPaquete;

  @GetMapping
  public String mostrarPaquetes(
      @RequestParam(required = false) String criterio,
      @RequestParam(required = false) String cedulaCliente,
      @RequestParam(required = false) String fechaRegistro,
      @RequestParam(required = false) String estadoRastreo,
      Model model) {

    List<Paquete> paquetes = servicioPaquete.buscar(criterio, cedulaCliente, fechaRegistro, estadoRastreo);

    model.addAttribute("paquetes", paquetes);

    // Opcional: también pasar la lista de posibles estados de rastreo para el
    // dropdown
    List<String> estadosRastreo = List.of("Registrado", "En tránsito", "Entregado", "Devuelto");
    model.addAttribute("estadosRastreo", estadosRastreo);

    return "paquetes/paquetes";
  }

  @GetMapping("/agregar")
  public String mostrarAgregar(Model model) {
    // Crear un objeto Paquete vacío para el formulario
    model.addAttribute("clientes", servicioPaquete.listarClientes());

    Paquete paquete = new Paquete();
    model.addAttribute("paquete", paquete);
    model.addAttribute("textoBoton", "Agregar Paquete");
    return "paquetes/agregar"; // Vista que contiene el formulario para agregar un paquete
  }

  @PostMapping("/agregar")
  public String agregarPaquete(@ModelAttribute("paquete") @Valid Paquete paquete,
      RedirectAttributes redirectAttributes,
      BindingResult result,
      Model model) {

    if (result.hasErrors()) {
      return "paquetes/agregar";
    }
    try {
      servicioPaquete.agregarPaquete(paquete);
    } catch (ExcepcionEnviosBios e) {
      model.addAttribute("error", e.getMessage());
      return "paquetes/agregar";
    }

    redirectAttributes.addFlashAttribute("mensaje", "¡Paquete agregado exitosamente!");
    return "redirect:/paquetes";
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
    return "paquetes/detalle"; // Vista para mostrar el detalle del paquete
  }
}
