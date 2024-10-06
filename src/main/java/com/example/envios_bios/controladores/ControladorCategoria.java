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
import com.example.envios_bios.dominio.Categoria;
import com.example.envios_bios.excepciones.ExcepcionEnviosBios;
import com.example.envios_bios.servicios.IServicioCategorias;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/categorias")
public class ControladorCategoria {

    @Autowired
    private IServicioCategorias servicioCategorias;

    @GetMapping
    public String mostrarCategorias(@RequestParam(required = false) String criterio, Model model) {
        List<Categoria> categorias = servicioCategorias.buscar(criterio);

        model.addAttribute("categorias", categorias);

        return "categorias/categorias";
    }

    @GetMapping("/agregar")
    public String mostrarAgregar(Model model) {
        // Crear un objeto categoría vacío para el formulario
    Categoria categoria = new Categoria();
    model.addAttribute("categoria", categoria);
    // Pasar el valor del botón de acción al formulario
    model.addAttribute("textoBoton", "Agregar Categoría");
    return "categorias/agregar";
    }
       
    
    @PostMapping("/agregar")
    public String agregarCategoria(@ModelAttribute("categoria") @Valid Categoria categoria, RedirectAttributes redirectAttributes, BindingResult result, Model model) {
        
        if (result.hasErrors()) {
            return "categorias/agregar";
        }
        try { 
            servicioCategorias.agregar(categoria);
        }
        catch (ExcepcionEnviosBios e) {
            model.addAttribute("error", e.getMessage());
            return "categorias/agregar";

        }
        // Añadir un mensaje de éxito a los atributos redireccionados
        redirectAttributes.addFlashAttribute("mensaje", "¡Categoría agregada exitosamente!");
        
        // Redireccionar a la lista de categorías
        return "redirect:/categorias";
    }

    @GetMapping("/modificar")
    public String mostrarModificar(Integer idCat, Model model) {
    model.addAttribute("categorias", servicioCategorias.listar());

        Categoria categoria = servicioCategorias.obtener(idCat);

        if (categoria != null) {
            model.addAttribute("categoria", categoria);
        } else {
            model.addAttribute("mensaje", "¡ERROR! No se encontró la categoria con el id " + idCat + ".");
        }

        return "categorias/modificar";
}

@PostMapping("/modificar")
    public String procesarModificar(@ModelAttribute @Valid Categoria categoria, BindingResult result, Model model, RedirectAttributes attributes) {
        model.addAttribute("categorias", servicioCategorias.listar());
        System.out.println(categoria);
        if (result.hasErrors()) {
            return "categorias/modificar";
        }
        try {
            
            servicioCategorias.modificar(categoria);
            attributes.addFlashAttribute("mensaje", "Categoria modificada con éxito.");
            return "redirect:/categorias";
    
        }
        catch (ExcepcionEnviosBios e) {
            model.addAttribute("mensaje", "¡ERROR! " + e.getMessage());

            return "categorias/modificar";
        
        }
    }

    @GetMapping("/eliminar")
    public String mostrarEliminar(Integer idCat, Model model) {
        Categoria categoria = servicioCategorias.obtener(idCat);

        if (categoria != null) {
            model.addAttribute("categorias", categoria);
        } else {
            model.addAttribute("mensaje", "¡ERROR! No se encontró la categoria con el código " + idCat + ".");
        }

        return "categorias/eliminar";
    }

    @PostMapping("/eliminar")
    public String procesarEliminar(Integer idCat, Model model, RedirectAttributes attributes) {
        try {
            servicioCategorias.eliminar(idCat);           

            attributes.addFlashAttribute("mensaje", "Categoria eliminada con éxito.");

            return "redirect:/categorias";
        } catch (ExcepcionEnviosBios e) {
            model.addAttribute("mensaje", "¡ERROR! " + e.getMessage());

            return "categorias/categorias";
        }
    }

    @GetMapping("/{idCat}")
    public String mostrarDetalle(@PathVariable("idCat") Integer idCat, Model model) {
        Categoria categoria = servicioCategorias.obtener(idCat);

        if (categoria != null) {
            model.addAttribute("categorias", categoria);
        } else {
            model.addAttribute("mensaje", "¡ERROR! No se encontró la categoría con el código " + idCat + ".");
        }

        return "categorias/detalle";
    }
}